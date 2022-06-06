package fr.baba.deltashield.utils;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.baba.deltashield.Config;
import fr.baba.deltashield.Main;
import fr.baba.deltashield.Webhook;

public class sendWebhook {
	public static void Flagged(Player p, String check, String type, String infos, String vl) {
		Main main = Main.getPlugin(Main.class);
		String url = main.getConfig().getString("webhook.hack.flagged.url");
		
		try {
			@SuppressWarnings("unused")
			URL urlt = new URL(url);
		} catch (MalformedURLException e) {
			//e.printStackTrace();
			System.out.println("URL used for the Hack (flagged) Webhook is invalid!");
			return;
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
			public void run() {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		    	LocalDateTime now = LocalDateTime.now();
		    	Location l = p.getLocation();
				
				Webhook webhook = new Webhook(url);
				
				webhook.addEmbed(new Webhook.EmbedObject()
						.setTitle("DeltaAC - Cheat detection")
						.setThumbnail("https://www.spigotmc.org/data/resource_icons/101/101756.jpg?1651604708")
			            .setColor(Color.ORANGE)
			            .setAuthor(p.getName(), "", "https://mc-heads.net/avatar/" + p.getName().toLowerCase() + "/128")
			            .setDescription(Config.getMessages().getString("alerts.hack-webhook-desc")
			            		.replace("%player%", p.getName())
			            		.replace("%check%", check.substring(0, 1).toUpperCase() + check.substring(1))
			            		.replace("%type%", type.toUpperCase())
			            		.replace("%vl%", vl)
			            		.replace("%max-vl%", "" + Config.getChecks().getInt("checks." + check + "." + type + ".max-violations")))
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
	
	public static void Punished(Player p, String check, String type, String infos, String vl) {
		Main main = Main.getPlugin(Main.class);
		String url = main.getConfig().getString("webhook.hack.punished.url");
		
		try {
			@SuppressWarnings("unused")
			URL urlt = new URL(url);
		} catch (MalformedURLException e) {
			//e.printStackTrace();
			System.out.println("URL used for the Hack (punished) Webhook is invalid!");
			return;
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
			public void run() {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		    	LocalDateTime now = LocalDateTime.now();
		    	Location l = p.getLocation();
				
				Webhook webhook = new Webhook(url);
				
				webhook.addEmbed(new Webhook.EmbedObject()
						.setTitle("DeltaAC - Punishment application")
						.setThumbnail("https://www.spigotmc.org/data/resource_icons/101/101756.jpg?1651604708")
			            .setColor(Color.RED)
			            .setAuthor(p.getName(), "", "https://mc-heads.net/avatar/" + p.getName().toLowerCase() + "/128")
			            .setDescription(Config.getMessages().getString("alerts.hack-webhook-desc")
			            		.replace("%player%", p.getName())
			            		.replace("%check%", check.substring(0, 1).toUpperCase() + check.substring(1))
			            		.replace("%type%", type.toUpperCase())
			            		.replace("%vl%", vl)
			            		.replace("%max-vl%", "" + Config.getChecks().getInt("checks." + check + "." + type + ".max-violations")))
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
	
	public static void Blacklist(Player p, String check, String type, String arg, String proof) {
		Main main = Main.getPlugin(Main.class);
		String url = main.getConfig().getString("webhook.blacklist." + check + ".url");
		
		try {
			@SuppressWarnings("unused")
			URL urlt = new URL(url);
		} catch (MalformedURLException e) {
			//e.printStackTrace();
			System.out.println("URL used for the Blacklist Webhook is invalid!");
			return;
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
			public void run() {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		    	LocalDateTime now = LocalDateTime.now();
		    	Location l = p.getLocation();
				
				Webhook webhook = new Webhook(url);
				
				String check2 = check.substring(0, 1).toUpperCase() + check.substring(1);
				check2 = check2.substring(0, check2.length() - 1);
				
				webhook.addEmbed(new Webhook.EmbedObject()
						.setTitle("DeltaAC - Blacklist detection")
						.setThumbnail("https://www.spigotmc.org/data/resource_icons/101/101756.jpg?1651604708")
			            .setColor(Color.RED)
			            .setAuthor(p.getName(), "", "https://mc-heads.net/avatar/" + p.getName().toLowerCase() + "/128")
			            //.setDescription(Config.getMessages().getString("alerts.blacklist-webhook-desc")
			            	//.replace("%player%", p.getName())
			            	//.replace("%check%", check.substring(0, 1).toUpperCase() + check.substring(1))
			            	//.replace("%type%", type.substring(0, 1).toUpperCase() + type.substring(1))
			            	//.replace("%arg%", arg)
			            	//.replace("%proof%", proof))
			            .addField("UUID", p.getUniqueId().toString(), true)
			            .addField("Check", check2, true)
			            .addField("Type", type.substring(0, 1).toUpperCase() + type.substring(1), true)
			            .addField("Message", proof.replace(arg, "__" + arg + "__"), true)
			            .addField("Compromised", arg, true)
			            .addField("World", l.getWorld().getName(), true)
						.setFooter(dtf.format(now), ""));
				try {
					webhook.execute();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
