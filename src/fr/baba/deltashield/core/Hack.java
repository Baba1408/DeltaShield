package fr.baba.deltashield.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.baba.deltashield.Config;
import fr.baba.deltashield.Main;
import fr.baba.deltashield.utils.Alert;
import fr.baba.deltashield.utils.sendWebhook;

public class Hack {
	static Map<UUID, Map<String, Integer>> vl = new HashMap<>();
	static Map<UUID, Boolean> cannot = new HashMap<>();
	
	public static void start() {
		Main main = Main.getPlugin(Main.class);
		int interval = main.getConfig().getInt("modules.checks.reset-violations.interval") * 1200;
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(main, new Runnable() {
			public void run() {
				if(!vl.isEmpty()){
					vl.clear();
					if(!main.getConfig().getBoolean("modules.checks.reset-violations.silent")) Alert.sendAlert(Config.getMessages().getString("messages.reset-violations")
							.replace("&", "§"));
				}
			}
		}, interval, interval);
	}
	
	public static void Check(Player player, String check, String type, String infos, Location from){
		if(player.hasPermission("deltashield.bypass.hack")) return;
		Main main = Main.getPlugin(Main.class);
		Boolean punish = false;
		String place = "checks." + check + "." + type + ".";
		
		UUID uuid = player.getUniqueId();
		String map = check + type;
		
		if(cannot.get(uuid) != null) return;
		
		Map<String, Integer> x = new HashMap<>();
		
		if(vl.get(uuid) == null){
			x.put(map, 1);
			vl.put(uuid, x);
		} else if(vl.get(uuid).get(map) != null) {
			x = vl.get(uuid);
			x.put(map, vl.get(uuid).get(map) + 1);
			vl.put(uuid, x);
			
			if(vl.get(uuid).get(map) >= Config.getChecks().getInt(place + "max-violations") && Config.getChecks().getBoolean(place + ".punishable")) punish = true;
		} else {
			x = vl.get(uuid);
			x.put(map, 1);
			vl.put(uuid, x);
		}
		
		String alert = Config.getMessages().getString("alerts.hack")
				.replace("&", "§")
				.replace("%player%", player.getName())
				.replace("%check%", check.substring(0, 1).toUpperCase() + check.substring(1))
				.replace("%type%", type.toUpperCase())
				.replace("%vl%", vl.get(uuid).get(map).toString())
				.replace("%max-vl%", "" + Config.getChecks().getInt(place + "max-violations"))
				.replace("%desc%", Config.getChecks().getString("checks." + check + "." + type + ".description"));
		
		String desc = "";
		for(String s : Config.getMessages().getStringList("alerts.hack-hover")) desc = desc + s + "\n";
		
		desc = desc.substring(0, desc.length() - 1)
				.replace("&", "§")
				.replace("%player%", player.getName())
				.replace("%check%", check.substring(0, 1).toUpperCase() + check.substring(1))
				.replace("%type%", type.toUpperCase())
				.replace("%vl%", vl.get(uuid).get(map).toString())
				.replace("%max-vl%", "" + Config.getChecks().getInt(place + "max-violations"))
				.replace("%desc%", Config.getChecks().getString("checks." + check + "." + type + ".description"));
		
		if(infos != null && !infos.isEmpty()){
			desc = desc.replace("%infos%", infos);
		} else desc = desc.replace("%infos%", "No information provided");
		
		if(from != null && Config.getChecks().get(place + "cancel") != null && Config.getChecks().getInt(place + "cancel") >= 1 && vl.get(uuid).get(map) % Config.getChecks().getInt(place + "cancel") == 0){
			player.teleport(from);
		}
		
		if(vl.get(uuid).get(map) % main.getConfig().getInt("modules.checks.alert-interval") == 0 || punish == true){
			Alert.sendComponentAlert(alert, desc, "tp " + player.getName());
			if(main.getConfig().getBoolean("webhook.hack.flagged.enabled")) sendWebhook.Flagged(player, check, type, infos, vl.get(uuid).get(map).toString());
		}
		
		if(punish == true){
			cannot.put(uuid, true);
			
			int delay = 0;
			if(!Config.getChecks().getBoolean(place + "bypass-delay")) delay = main.getConfig().getInt("modules.checks.punishment-delay") * 20;
			
			if(Config.getChecks().getString(place + "punishment-command") != null){
				place = place + "punishment-command";
			} else if(Config.getChecks().getString("checks.punishment-command") != null){
				place = "checks.punishment-command";
			} else {
				System.out.print("An error occurred in the plugin configuration, please check it");
				return;
			}
			
			String command = Config.getChecks().getString(place)
					.replace("&", "§")
					.replace("%player%", player.getName())
					.replace("%uuid%", player.getUniqueId().toString())
					.replace("%check%", check)
					.replace("%type%", type);
			
			System.out.print("Preparing to execute command : " + command);
			
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
				public void run() {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
					Alert.sendAlert(Config.getMessages().getString("alerts.hack-punished")
							.replace("&", "§")
							.replace("%player%", player.getName())
							.replace("%check%", check.substring(0, 1).toUpperCase() + check.substring(1))
							.replace("%type%", type.toUpperCase())
							.replace("%vl%", vl.get(uuid).get(map).toString())
							.replace("%max-vl%", "" + Config.getChecks().getInt("checks." + check + "." + type + ".max-violations"))
							.replace("%desc%", Config.getChecks().getString("checks." + check + "." + type + ".description")));
					Alert.sendHotbar(Config.getMessages().getString("alerts.hack-hotbar")
							.replace("&", "§")
							.replace("%player%", player.getName())
							.replace("%check%", check.substring(0, 1).toUpperCase() + check.substring(1))
							.replace("%type%", type.toUpperCase())
							.replace("%vl%", vl.get(uuid).get(map).toString())
							.replace("%max-vl%", "" + Config.getChecks().getInt("checks." + check + "." + type + ".max-violations"))
							.replace("%desc%", Config.getChecks().getString("checks." + check + "." + type + ".description")), main.getConfig().getBoolean("modules.checks.hotbar.bypass-alerts-disabled"));
					sendWebhook.Punished(player, check, type, infos, vl.get(uuid).get(map).toString());
					vl.remove(uuid);
					cannot.remove(uuid);
				}
			}, delay);
		}
	}
	
	public static void setRecentPunish(UUID uuid) {
		cannot.put(uuid, true);
		Main main = Main.getPlugin(Main.class);
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			public void run() {
				cannot.remove(uuid);
			}
		}, main.getConfig().getInt("modules.checks.punishment-delay") * 20);
	}
}
