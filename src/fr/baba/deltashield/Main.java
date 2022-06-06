package fr.baba.deltashield;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.baba.deltashield.checks.ClimbA;
import fr.baba.deltashield.checks.FlightA;
import fr.baba.deltashield.checks.FlightB;
import fr.baba.deltashield.checks.GroundSpoofA;
import fr.baba.deltashield.checks.InventoryA;
import fr.baba.deltashield.checks.StealA;
import fr.baba.deltashield.checks.StealB;
import fr.baba.deltashield.checks.InvalidA;
import fr.baba.deltashield.checks.InvalidB;
import fr.baba.deltashield.checks.InvalidC;
import fr.baba.deltashield.checks.InvalidD;
import fr.baba.deltashield.checks.InvalidE;
import fr.baba.deltashield.checks.FlightC;
import fr.baba.deltashield.checks.SpeedA;
import fr.baba.deltashield.commands.DeltaShield;
import fr.baba.deltashield.core.Hack;
import fr.baba.deltashield.events.BlockBreak;
import fr.baba.deltashield.events.PlayerChat;
import fr.baba.deltashield.events.PlayerCommand;
import fr.baba.deltashield.events.PlayerDamage;
import fr.baba.deltashield.events.PlayerJoin;
import fr.baba.deltashield.events.VulcanPunish;
import fr.baba.deltashield.fixs.NullChunk;
import fr.baba.deltashield.fixs.Spectator;
import fr.baba.deltashield.fixs.TabCompletePacket;
import fr.baba.deltashield.utils.Alert;

public class Main extends JavaPlugin {
	String prefix = "§2[§aDeltaShield§2] ";
	String load = "		§3-§b ";
	
	List<String> bcommands = new ArrayList<>();
	List<String> bcommandc = new ArrayList<>();
	List<String> bchats = new ArrayList<>();
	List<String> bchatc = new ArrayList<>();
	
	HashMap<String, String> redirected = new HashMap<>();
	
	HashMap<UUID, Boolean> alerts = new HashMap<>();
	
	String updatemsg = null;
	
	@Override
	public void onEnable() {
		//PluginManager pm = getServer().getPluginManager();
		ConsoleCommandSender s = Bukkit.getServer().getConsoleSender();
		s.sendMessage("§3Loading plugin...");
		
			//Configs
		s.sendMessage(load + "Loading/download configs...");
		loadConfig();
		Config.setup();
		
			//Blacklist
		s.sendMessage(load + "Loading blacklist commands/chats...");
		for(String w : Config.getBlacklist().getStringList("blacklist.commands.startwith")) bcommands.add(w.toLowerCase());
		for(String w : Config.getBlacklist().getStringList("blacklist.commands.contains")) bcommandc.add(w.toLowerCase());
		for(String w : Config.getBlacklist().getStringList("blacklist.chats.startwith")) bchats.add(w.toLowerCase());
		for(String w : Config.getBlacklist().getStringList("blacklist.chats.contains")) bchatc.add(w.toLowerCase());
		
			//Redirections
		s.sendMessage(load + "Loading redirected commands...");
		for(String w : getConfig().getStringList("redirected-commands")){
			String w2[] = w.split("#");
			redirected.put(w2[0], w2[1]);
			//s.sendMessage(" 		" + w2[0] + ", to : " + w2[1]);
		}
		
			//Events - Fixs - checks
		load();
		
			//Commands
		s.sendMessage(load + "Loading Commands...");
		getCommand("deltashield").setExecutor(new DeltaShield());
		getCommand("deltashield").setTabCompleter(new DeltaShield());
		
		s.sendMessage(prefix + "§aSuccessfully loaded !");
		
		Alert.sendInfo(prefix + "§bPlugin successfully restarted !");
		Updater.Check();
	}
	
	public void reload() {
		if(getConfig().getBoolean("anti-tab-completion.enabled")) Bukkit.getScheduler().runTask(this, () -> TabCompletePacket.launch(this));
		
		bcommands.clear();
		bcommandc.clear();
		bchats.clear();
		bchatc.clear();
		
		for(String w : Config.getBlacklist().getStringList("blacklist.commands.startwith")) bcommands.add(w.toLowerCase());
		for(String w : Config.getBlacklist().getStringList("blacklist.commands.contains")) bcommandc.add(w.toLowerCase());
		for(String w : Config.getBlacklist().getStringList("blacklist.chats.startwith")) bchats.add(w.toLowerCase());
		for(String w : Config.getBlacklist().getStringList("blacklist.chats.contains")) bchatc.add(w.toLowerCase());
	}
	
	public void load() {
		PluginManager pm = getServer().getPluginManager();
		ConsoleCommandSender s = Bukkit.getServer().getConsoleSender();
		
			//Events
		s.sendMessage(load + "Loading Events...");
		pm.registerEvents(new PlayerJoin(this), this);
		
		if(getConfig().getBoolean("modules.blacklist.enabled")){
			Bukkit.getScheduler().runTask(this, () -> pm.registerEvents(new PlayerCommand(this), this));
			Bukkit.getScheduler().runTask(this, () -> pm.registerEvents(new PlayerChat(this), this));
		}
		
		Bukkit.getScheduler().runTask(this, () -> pm.registerEvents(new PlayerDamage(), this));
		if(Config.getChecks().getBoolean("anti-xray.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new BlockBreak(), this));
		
			//Others
		if(getConfig().getBoolean("anti-tab-completion.enabled")) Bukkit.getScheduler().runTask(this, () -> TabCompletePacket.launch(this));
		
			//Fixs
		if(getConfig().getBoolean("fixs.null-chunk")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new NullChunk(), this));
		if(getConfig().getBoolean("fixs.spectator")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new Spectator(), this));
		
			//Checks
		if(Config.getChecks().getBoolean("modules.checks.reset-violations.enabled")) Hack.start();
		
		if(getConfig().getBoolean("modules.checks.hotbar.vulcan-punishment")){
			if(pm.getPlugin("Vulcan") != null && pm.getPlugin("Vulcan").isEnabled()){
				pm.registerEvents(new VulcanPunish(), this);
			} else {
				s.sendMessage("§cVulcan cannot be hooked with the plugin because it is not present on the server or is not activated!");
			}
		}
		
		//if(Config.getChecks().getBoolean("checks.autoclicker.a.enabled")){
			//Bukkit.getScheduler().runTask(this, () -> pm.registerEvents(new AutoclickerA(), this));
		//pm.registerEvents(new AutoclickerA(), this);
			//AutoclickerA.launch();
		//}
		if(getConfig().getBoolean("modules.checks.enabled")){
			if(Config.getChecks().getBoolean("checks.climb.a.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new ClimbA(), this));
			if(Config.getChecks().getBoolean("checks.flight.a.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new FlightA(), this));
			if(Config.getChecks().getBoolean("checks.flight.b.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new FlightB(), this));
			if(Config.getChecks().getBoolean("checks.flight.c.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new FlightC(), this));
			if(Config.getChecks().getBoolean("checks.groundspoof.a.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new GroundSpoofA(), this));
			if(Config.getChecks().getBoolean("checks.inventory.a.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new InventoryA(), this));
			if(Config.getChecks().getBoolean("checks.invalid.a.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new InvalidA(), this));
			if(Config.getChecks().getBoolean("checks.invalid.b.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new InvalidB(), this));
			if(Config.getChecks().getBoolean("checks.invalid.c.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new InvalidC(), this));
			if(Config.getChecks().getBoolean("checks.invalid.d.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new InvalidD(), this));
			if(Config.getChecks().getBoolean("checks.invalid.e.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new InvalidE(), this));
			if(Config.getChecks().getBoolean("checks.speed.a.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new SpeedA(), this));
			if(Config.getChecks().getBoolean("checks.steal.a.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new StealA(), this));
			if(Config.getChecks().getBoolean("checks.steal.b.enabled")) Bukkit.getScheduler().runTaskAsynchronously(this, () -> pm.registerEvents(new StealB(), this));
		}
	}
	
	File file = new File(getDataFolder(), "config.yml");
	
	public void loadConfig() {
		if(!file.exists()){
			saveDefaultConfig();
		}
	}

	public String getLanguage(String config){
		String language = getConfig().getString("auto-config-updater." + config + ".language");
		String url = null;
		
		if(language.equalsIgnoreCase("custom")){
			url = getConfig().getString("auto-config-updater." + config + ".custom");
		} else if(config == "checks"){
			if(language.equalsIgnoreCase("en")){
				url = "https://gist.githubusercontent.com/Baba1408/f78b1596269e3023e0b312c6d66c5ab9/raw/checks.yml";
			} else if(language.equalsIgnoreCase("fr")){
				url = "https://gist.githubusercontent.com/Baba1408/7f6fb644257fa8761c9daf456c2ed21c/raw/checks.yml";
			}
		} else if(config == "messages"){
			if(language.equalsIgnoreCase("en")){
				url = "https://gist.githubusercontent.com/Baba1408/011bd992d0f4d73c081b3fcd2f1cbef9/raw/messages.yml";
			} else if(language.equalsIgnoreCase("fr")){
				url = "https://gist.githubusercontent.com/Baba1408/8c0d945d91adb95ce580758afff98e32/raw/messages.yml";
			}
		} else if(config == "blacklist"){
			if(language.equalsIgnoreCase("en")){
				url = "https://gist.githubusercontent.com/Baba1408/bf62f59df36a942fa872f164633bfbd4/raw/blacklist.yml";
			} else if(language.equalsIgnoreCase("fr")){
				url = "https://gist.githubusercontent.com/Baba1408/d3d68496ea118a66991922e2d30f6481/raw/blacklist.yml";
			}
		}
		
		return url;
	}
	
	public void setUpdateMSG(String msg){
		updatemsg = msg;
	}
	
	public String getUpdateMSG(){
		return updatemsg;
	}
	
	public List<String> getCommandsS(){
		return bcommands;
	}

	public List<String> getCommandsC(){
		return bcommandc;
	}
	
	public List<String> getChatS(){
		return bchats;
	}
	
	public List<String> getChatC(){
		return bchatc;
	}
	
	public Map<String, String> getRedirected(){
		return (Map<String, String>) redirected;
	}
	
	public Map<UUID, Boolean> getAlerts(){
		return (Map<UUID, Boolean>) alerts;
	}
}
