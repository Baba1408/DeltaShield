package fr.baba.deltashield;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	private static Main main = Main.getPlugin(Main.class);

	public static File checksfile, messagesfile, blacklistfile;
	public static FileConfiguration checkscfg, messagescfg, blacklistcfg;
	
	public static void setup() {
		if(!main.getDataFolder().exists()) main.getDataFolder().mkdir();
		
		setupChecks();
		setupMessages();
		setupBlacklist();
		
		//SetupConfig(checksfile, checkscfg, "checks");
		//SetupConfig(messagesfile, messagescfg, "messages");
		//SetupConfig(blacklistfile, blacklistcfg, "blacklist");
	}
	
	public static void SetupConfig(File file, FileConfiguration config, String name) {
		ConsoleCommandSender s = Bukkit.getServer().getConsoleSender();
		file = new File(main.getDataFolder(), name + ".yml");
		
		if(main.getConfig().get("auto-config-updater." + name + ".enabled") != null && main.getConfig().getBoolean("auto-config-updater." + name + ".enabled")){
			URL url = null;
			
			try {
				url = new URL(main.getLanguage(name));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				s.sendMessage("§cInvalid URL !");
			}
			
			try {
				FileUtils.copyURLToFile(url, file);
			} catch (IOException e) {
				e.printStackTrace();
				s.sendMessage("§cError while download config file !");
				main.saveResource(name + ".yml", false);
			}
		} else if(!file.exists()){
			main.saveResource(name + ".yml", false);
		}
		
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public static void setupChecks() {
		String config = "checks";
		ConsoleCommandSender s = Bukkit.getServer().getConsoleSender();
		checksfile = new File(main.getDataFolder(), config + ".yml");
		
		if(main.getConfig().get("auto-config-updater." + config + ".enabled") != null && main.getConfig().getBoolean("auto-config-updater." + config + ".enabled")){
			URL url = null;
			
			try {
				url = new URL(main.getLanguage(config));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				s.sendMessage("§cInvalid URL !");
				//return;
			}
			
			//YamlConfiguration configt;
			//configt = YamlConfiguration.loadConfiguration(FileUtils.toFile(url));
			
			//if(main.getConfig().getBoolean("auto-config-updater." + config + ".ignore-plugin-version") || (configt.get("config-version") != null || configt.get("config-version") == main.getDescription().getVersion())){
				try {
					FileUtils.copyURLToFile(url, checksfile);
				} catch (IOException e) {
					e.printStackTrace();
					s.sendMessage("§cError while download config file !");
					main.saveResource(config + ".yml", false);
				}
				//}
		} else if(!checksfile.exists()){
			main.saveResource(config + ".yml", false);
		}// else {
		
		checkscfg = YamlConfiguration.loadConfiguration(checksfile);
			
		@SuppressWarnings("deprecation")
		FileConfiguration checksdefault = YamlConfiguration.loadConfiguration(main.getResource("checks.yml"));
			
		Map<String, String> c = new HashMap<>();
		c.put("climb", "a");
		c.put("flight", "a; b; c");
		c.put("groundspoof", "a");
		c.put("inventory", "a");
		c.put("invalid", "a; b; c; d; e");
		c.put("speed", "a");
			
		ArrayList<String> d = new ArrayList<>(Arrays.asList("description",
				"enabled",
				"punishable",
				"max-violations"));
			
		Boolean save = false;
			
		for(String w : c.keySet()){
			for(String v : c.get(w).split("; ")){
				String p = "checks." + w + "." + v + ".";
				if(getChecks().get(p + "enabled") == null){
					for(String de : d){
						getChecks().set(p + de, checksdefault.get(p + de));
						if(!save) save = true;
					}
				}
			}
		}
			
		if(save) saveChecks();
		//}
		
		checkscfg = YamlConfiguration.loadConfiguration(checksfile);
	}
	
	public static void setupMessages() {
		String config = "messages";
		ConsoleCommandSender s = Bukkit.getServer().getConsoleSender();
		messagesfile = new File(main.getDataFolder(), config + ".yml");
		
		if(main.getConfig().get("auto-config-updater." + config + ".enabled") != null && main.getConfig().getBoolean("auto-config-updater." + config + ".enabled")){
			URL url = null;
			
			try {
				url = new URL(main.getLanguage(config));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				s.sendMessage("§cInvalid URL !");
				//return;
			}
			
			//YamlConfiguration configt;
			//configt = YamlConfiguration.loadConfiguration(FileUtils.toFile(url));
			
			//if(main.getConfig().getBoolean("auto-config-updater." + config + ".ignore-plugin-version") || (configt.get("config-version") != null || configt.get("config-version") == main.getDescription().getVersion())){
				try {
					FileUtils.copyURLToFile(url, messagesfile);
				} catch (IOException e) {
					e.printStackTrace();
					s.sendMessage("§cError while download config file !");
					main.saveResource(config + ".yml", false);
				}
				//}
		} else if(!messagesfile.exists()){
			main.saveResource(config + ".yml", false);
		}
		
		messagescfg = YamlConfiguration.loadConfiguration(messagesfile);
	}
	
	public static void setupBlacklist() {
		String config = "blacklist";
		ConsoleCommandSender s = Bukkit.getServer().getConsoleSender();
		blacklistfile = new File(main.getDataFolder(), config + ".yml");
		
		if(main.getConfig().get("auto-config-updater." + config + ".enabled") != null && main.getConfig().getBoolean("auto-config-updater." + config + ".enabled")){
			URL url = null;
			
			try {
				url = new URL(main.getLanguage(config));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				s.sendMessage("§cInvalid URL !");
				//return;
			}
			
			//YamlConfiguration configt;
			//configt = YamlConfiguration.loadConfiguration(FileUtils.toFile(url));
			
			//if(main.getConfig().getBoolean("auto-config-updater." + config + ".ignore-plugin-version") || (configt.get("config-version") != null || configt.get("config-version") == main.getDescription().getVersion())){
				try {
					FileUtils.copyURLToFile(url, blacklistfile);
				} catch (IOException e) {
					e.printStackTrace();
					s.sendMessage("§cError while download config file !");
					main.saveResource(config + ".yml", false);
				}
				//}
		} else if(!blacklistfile.exists()){
			main.saveResource(config + ".yml", false);
		}
		
		blacklistcfg = YamlConfiguration.loadConfiguration(blacklistfile);
	}
	
	//Checks
	public static FileConfiguration getChecks() {
		return checkscfg;
	}
	
	public static void saveChecks() {
		try {
			checkscfg.save(checksfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reloadChecks() {
		checkscfg = YamlConfiguration.loadConfiguration(checksfile);
	}
	
	//Messages
	public static FileConfiguration getMessages() {
		return messagescfg;
	}
	
	public static void saveMessages() {
		try {
			messagescfg.save(messagesfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reloadMessages() {
		messagescfg = YamlConfiguration.loadConfiguration(messagesfile);
	}
	
	//Blacklist
	public static FileConfiguration getBlacklist() {
		return blacklistcfg;
	}
	
	public static void saveBlacklist() {
		try {
			blacklistcfg.save(blacklistfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reloadBlacklist() {
		blacklistcfg = YamlConfiguration.loadConfiguration(blacklistfile);
	}
}
