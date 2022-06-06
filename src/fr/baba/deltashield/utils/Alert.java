package fr.baba.deltashield.utils;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.baba.deltashield.Config;
import fr.baba.deltashield.Main;
import fr.baba.deltashield.Webhook;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Alert {
	public static void sendComponentAlert(String a, String d, String c) {
		Main main = Main.getPlugin(Main.class);
		
		TextComponent m = new TextComponent(a);
		if(d != null && !d.isEmpty()) m.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(d).create()));
		if(c != null && !c.isEmpty()) m.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + c));
		
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.hasPermission("deltashield.alerts")){
				if(!main.getAlerts().containsKey(p.getUniqueId())){
					p.spigot().sendMessage(m);
				}
			}
		}
	}
	
	public static void sendAlert(String a) {
		Main main = Main.getPlugin(Main.class);
		
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.hasPermission("deltashield.alerts")){
				if(!main.getAlerts().containsKey(p.getUniqueId())){
					p.sendMessage(a);
				}
			}
		}
	}
	
	public static void sendHotbar(String a, Boolean b) {
		Main main = Main.getPlugin(Main.class);
		
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.hasPermission("deltashield.alerts")){
				if(b || !main.getAlerts().containsKey(p.getUniqueId())){
					TitleManager.sendActionBar(p, a);
				}
			}
		}
	}
	
	public static void sendInfo(String a) {
		for(Player s : Bukkit.getOnlinePlayers()){
			if(s.hasPermission("deltashield.updates")){
				s.sendMessage(a);
			}
		}
	}
	
	public static void sendWebhook(Player p, String check, String type, String infos, String vl) {
		Main main = Main.getPlugin(Main.class);
		Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
			public void run() {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		    	LocalDateTime now = LocalDateTime.now();
		    	Location l = p.getLocation();
				
				Webhook webhook = new Webhook(main.getConfig().getString("webhook.hack.flagged.url"));
				
				webhook.addEmbed(new Webhook.EmbedObject()
						.setTitle("DeltaAC - Cheat detection")
						.setThumbnail("https://cdn.discordapp.com/icons/814965740095799296/314c445d3f8673f4ea567ce969b9a9ec.png?size=256")
			            .setColor(Color.ORANGE)
			            .setAuthor(p.getName(), "", "https://mc-heads.net/avatar/" + p.getName() + "/128")
			            .setDescription(Config.getChecks().getString("checks.webhook-desc")
			            		.replace("%player%", p.getName())
			            		.replace("%check%", check.substring(0, 1).toUpperCase() + check.substring(1))
			            		.replace("%type%", type.toUpperCase())
			            		.replace("%vl%", vl)
			            		.replace("%max-vl%", "" + Config.getChecks().getInt("checks." + check + "." + type + ".max-violations")))
			            //.addField("Name", p.getName(), true)
			            .addField("UUID", p.getUniqueId().toString(), true)
			            .addField("Description", Config.getChecks().getString("checks." + check + "." + type + ".description"), true)
			            .addField("Informations", infos, true)
			            .addField("Ping", "" + ((CraftPlayer) p).getHandle().ping, true) //MinecraftServer.getServer().recentTps
			            .addField("Location", l.getWorld().getName() + ", " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ(), true)
						.setFooter(dtf.format(now), ""));
				try {
					webhook.execute();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}
		});
	}
}
