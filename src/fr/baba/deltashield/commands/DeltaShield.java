package fr.baba.deltashield.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import fr.baba.deltashield.Config;
import fr.baba.deltashield.Main;

public class DeltaShield implements CommandExecutor, TabCompleter {
	static ArrayList<String> help = new ArrayList<>(Arrays.asList("Help#Display the usage menu for the plugin",
			"Alerts#Enable/disable moderation alerts",
			"Reload#Reload the plugin configuration",
			"Restart#Restart the plugin completely §c(requires PlugMan)"));
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length <= 1){
			List<String> completions = new ArrayList<>();
			List<String> commands = new ArrayList<>();
			
			commands.add("help");
			commands.add("reload");
			commands.add("alerts");
			//commands.add("punish");
			
			StringUtil.copyPartialMatches(args[0], commands, completions);
			Collections.sort(completions);
	        return completions;
		} else return null;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String msg, String[] args) {
		Main main = Main.getPlugin(Main.class);
		
		if(!s.hasPermission("deltashield.use")) return false;
		
		if(args.length >= 1){
			if(args[0].equalsIgnoreCase("alerts")){
				if(s instanceof Player){
					Player p = (Player) s;
					UUID uuid = p.getUniqueId();
					
					if(main.getAlerts().get(uuid) != null){
						main.getAlerts().remove(uuid);
						s.sendMessage(Config.getMessages().getString("messages.alerts-enabled").replace("&", "§"));
					} else {
						main.getAlerts().put(uuid, false);
						s.sendMessage(Config.getMessages().getString("messages.alerts-disabled").replace("&", "§"));
					}
				} else s.sendMessage("§cYou must be a player to run this command");
			} else if(args[0].equalsIgnoreCase("reload")){
				s.sendMessage("§bReloading configuration...");
				main.reload();
				main.reloadConfig();
			} else if(args[0].equalsIgnoreCase("restart")){
				if(Bukkit.getPluginManager().getPlugin("PlugMan") != null){
					if(Bukkit.getPluginManager().getPlugin("PlugMan").isEnabled()){
						s.sendMessage("§bRestarting plugin...");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman reload DeltaShield");
					} else s.sendMessage("§cPlugMan must be enabled to run this command");
				} else s.sendMessage("§cThis function requires the plugin PlugMan");
			} else help(s);
		} else help(s);
		
		return false;
	}
	
	public static void help(CommandSender s) {
		Main main = Main.getPlugin(Main.class);
		
		//if(help.isEmpty()){
			//help.add("Help#Display the usage menu for the plugin");
			//help.add("Alerts#Enable/disable moderation alerts");
			//help.add("Reload#Reload the plugin configuration");
			//help.add("Restart#Restart the plugin completely §c(requires PlugMan)");
		//}
		
		String line = "§8§m-----------------------------------";
		String msg = line + "\n";
		for(String w : help){
			String[] x = w.split("#");
			msg = msg + "§6" + x[0] + "§r §8§l•§r §e" + x[1] + "§r\n";
		}
		
		msg = msg + line;
		s.sendMessage(msg);
		
		
		String version;
		String ver;
		
		try {
			HttpsURLConnection con = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=101756").openConnection();
			con.setRequestMethod("GET");
			version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
			ver = "§8(§6Version §7: §e%current%§7/§e%latest%§8)"
					.replace("%current%", main.getDescription().getVersion())
					.replace("%latest%", version);
			s.sendMessage(ver);
		} catch (Exception ex) {
			s.sendMessage("§cFailed to check for updates !");
			return;
		}
	}
}
