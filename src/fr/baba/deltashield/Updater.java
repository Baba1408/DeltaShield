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

public class Updater {
	static String prefix = "§2[§aDeltaShield§a] ";

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
			    } else if ((P[1] < S[1])){
			    	x = true;
			    } else x = P[2] < S[2];
			    
			    if(x){
			    	main.getLogger().info("The plugin is not up to date, a new version of the plugin is available on Spigot!"
			    			+ "(Current : " + main.getDescription().getVersion() + " / Latest : " + version + ")");

			    	main.setUpdateMSG(prefix + "§eAn §6update§e of the plugin is §6available§e!");
			    	
			    	if(main.getConfig().getBoolean("plugin-updater.auto-download")) Download();
			    } else main.getLogger().info("The plugin is currently updated!");
			}
		});
	}
	
	private static int[] toReadable(String v) {
		return Arrays.stream(v.split("\\.")).mapToInt(Integer::parseInt).toArray();
	}
	
	public static void Download() {
		Main main = Main.getPlugin(Main.class);
		File file = new File("plugins/DeltaShield.update");
		URL url = null;
		
		main.getLogger().info("Automatic download activated, download of the update...");
		
		try {
			url = new URL("https://api.spiget.org/v2/resources/101756/download");
		} catch (MalformedURLException e) {
			main.getLogger().info("Error while download plugin file !");
			main.getLogger().info("No action required, the file has not been saved.\nError :\n");
			e.printStackTrace();
			return;
		}
		
		main.getLogger().info("Installation...");
		
		try {
			FileUtils.copyURLToFile(url, file);
		} catch (IOException e) {
			main.getLogger().info("Error while saving plugin file !\nError :\n");
			e.printStackTrace();
			return;
		}
		
		main.getLogger().info("The plugin has been updated, but requires a server restart or a plugin restart, an administrator must be present to perform this action.");
		main.setUpdateMSG(prefix + "§eAn update §6has been downloaded§e, an administrator must be present to perform a §6reboot of plugin/server§e.");
	}
}
