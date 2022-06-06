package fr.baba.deltashield;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import fr.baba.deltashield.utils.Alert;

public class Updater {
	static String prefix = "§2[§aDeltaShield§2] ";

	public static void Check() {
		Main main = Main.getPlugin(Main.class);
		
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			String version;
			
			try {
				HttpsURLConnection con = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=101756").openConnection();
				con.setRequestMethod("GET");
				version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
			} catch (Exception ex) {
				main.getLogger().info("Failed to check for updates on spigot.");
				return;
			}
			
			if (version != null && !version.isEmpty()){
				int[] P = toReadable(main.getDescription().getVersion());
			    int[] S = toReadable(version);
			    
			    Boolean x = false;
			    
			    if (P[0] < S[0]){
			    	x = true;
			    } else if(P[1] < S[1]){
			    	x = true;
			    } else x = P[2] < S[2];
			    
			    if(x){
			    	if(main.getConfig().getBoolean("plugin-updater.send-alert-on-join")){
			    		main.getLogger().info("The plugin is not up to date, a new version of the plugin is available on Spigot!"
				    			+ "(Current : " + main.getDescription().getVersion() + " / Latest : " + version + ")");

				    	main.setUpdateMSG(prefix + "§eAn §6update§e of the plugin is §6available§e!");
			    	}
			    	
			    	if(main.getConfig().getBoolean("plugin-updater.auto-download.enabled")) Download();
			    } else main.getLogger().info("The plugin is currently updated!");
			}
		});
	}
	
	private static int[] toReadable(String v) {
		return Arrays.stream(v.split("\\.")).mapToInt(Integer::parseInt).toArray();
	}
	
	public static void Download() {
		Main main = Main.getPlugin(Main.class);
		//File file = new File("plugins/DeltaShield.update");
		URL url = null;
		
		main.getLogger().info("Automatic download activated, download of the update...");
		Alert.sendInfo(prefix + "§bDownloading update...");
		
		try {
			url = new URL("https://api.spiget.org/v2/resources/101756/download");
		} catch (MalformedURLException e) {
			main.getLogger().info("Error while download plugin file !");
			main.getLogger().info("No action required, the file has not been saved.\nError :\n");
			e.printStackTrace();
			return;
		}
		
		main.getLogger().info("Installation...");
		
		if(main.getConfig().getBoolean("plugin-updater.auto-download.auto-restart")){
			if(Bukkit.getPluginManager().getPlugin("PlugMan") != null){
				if(Bukkit.getPluginManager().getPlugin("PlugMan").isEnabled()){
					try {
						FileUtils.copyURLToFile(url, new File("plugins/DeltaShield.jar"));
					} catch (IOException e) {
						main.getLogger().info("Error while saving plugin file !\nError :\n");
						e.printStackTrace();
					}
					
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman reload DeltaShield");
				} else {
					try {
						FileUtils.copyURLToFile(url, new File("plugins/DeltaShield.update"));
					} catch (IOException e) {
						main.getLogger().info("Error while saving plugin file !\nError :\n");
						e.printStackTrace();
					}
					
					String msg = prefix + "§eAn update §6has been downloaded§e, an administrator must be present to delete the plugin file and rename the §6.update file to §e.jar and §6perform a plugin/server restart§e.";
					main.getLogger().info("An update has been downloaded, an administrator must be present to delete the plugin file and rename the .update file to .jar and perform a plugin/server restart.");
					main.setUpdateMSG(msg);
					Alert.sendInfo(msg);
				}
			}
		}
	}
}
