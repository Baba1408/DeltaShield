package fr.baba.deltashield.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.baba.deltashield.Config;
import fr.baba.deltashield.Main;
import fr.baba.deltashield.utils.PlayerUtil;

public class Hack {
	static Map<UUID, Map<String, Integer>> volume = new HashMap<>();
	static Map<UUID, Boolean> cannot = new HashMap<>();
	
	public static void start() {
		Main main = Main.getPlugin(Main.class);
		int interval = Config.getChecks().getInt("reset-violations.interval") * 1200;
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(main, new Runnable() {
			public void run() {
				if(!volume.isEmpty()){
					volume.clear();
					if(!Config.getChecks().getBoolean("reset-violations.silent")) PlayerUtil.sendAlert(Config.getChecks().getString("reset-violations.message")
							.replace("&", "§"), null);
				}
			}
		}, interval, interval);
	}
	
	public static void Check(Player player, String check, String type, Location last){
		if(player.hasPermission("deltashield.bypass.hack")) return;
		Main main = Main.getPlugin(Main.class);
		Boolean punish = false;
		String place = "checks." + check + "." + type + ".";
		
		UUID uuid = player.getUniqueId();
		String map = check + type;
		
		if(cannot.get(uuid) != null) return;
		
		Map<String, Integer> x = new HashMap<>();
		
		if(volume.get(uuid) == null){
			x.put(map, 1);
			volume.put(uuid, x);
		} else if(volume.get(uuid).get(map) != null) {
			x = volume.get(uuid);
			x.put(map, volume.get(uuid).get(map) + 1);
			volume.put(uuid, x);
			
			if(volume.get(uuid).get(map) >= Config.getChecks().getInt(place + "max-violations")) punish = true;
		} else {
			x = volume.get(uuid);
			x.put(map, 1);
			volume.put(uuid, x);
		}
		
		String alert = Config.getMessages().getString("alerts.hack")
				.replace("&", "§")
				.replace("%player%", player.getName())
				.replace("%check%", check.substring(0, 1).toUpperCase() + check.substring(1))
				.replace("%type%", type.toUpperCase())
				.replace("%vl%", volume.get(uuid).get(map).toString())
				.replace("%max-vl%", "" + Config.getChecks().getInt(place + "max-violations"))
				.replace("%desc%", Config.getChecks().getString("checks." + check + "." + type + ".description"));
		
		String desc = "";
		for(String s : Config.getMessages().getStringList("alerts.hack-hover")){
			desc = desc + s + "\n";
		}
		
		desc = desc.substring(0, desc.length() - 1)
				.replace("&", "§")
				.replace("%player%", player.getName())
				.replace("%check%", check.substring(0, 1).toUpperCase() + check.substring(1))
				.replace("%type%", type.toUpperCase())
				.replace("%vl%", volume.get(uuid).get(map).toString())
				.replace("%max-vl%", "" + Config.getChecks().getInt(place + "max-violations"))
				.replace("%desc%", Config.getChecks().getString("checks." + check + "." + type + ".description"))
				.replace("%infos%", "No information provided");
		
		PlayerUtil.sendAlert(alert, desc);
		
		
		if(Config.getChecks().getInt(place + "cancel") >= 1 && volume.get(uuid).get(map) % Config.getChecks().getInt(place + "cancel") == 0){
			player.teleport(last);
		}
		
		
		if(punish == true){
			volume.remove(uuid);
			cannot.put(uuid, true);
			
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
					cannot.remove(uuid);
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
				}
			}, 60L);
		}
	}
}
