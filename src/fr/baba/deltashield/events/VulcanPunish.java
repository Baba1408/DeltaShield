package fr.baba.deltashield.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.baba.deltashield.Config;
import fr.baba.deltashield.Main;
import fr.baba.deltashield.core.Hack;
import fr.baba.deltashield.utils.Alert;
import me.frep.vulcan.api.event.VulcanPunishEvent;

public class VulcanPunish implements Listener {

	@EventHandler
	public void punish(VulcanPunishEvent e){
		Main main = Main.getPlugin(Main.class);
		Hack.setRecentPunish(e.getPlayer().getUniqueId());
		
		Alert.sendHotbar(Config.getMessages().getString("alerts.hack-vulcan-hotbar")
				.replace("&", "§")
				.replace("%player%", e.getPlayer().getName())
				.replace("%check%", e.getCheck().getName().substring(0, 1).toUpperCase() + e.getCheck().getName().substring(1))
				.replace("%type%", "" + Character.toUpperCase(e.getCheck().getType()))
				.replace("%vl%", "" + e.getCheck().getVl())
				.replace("%max-vl%", "" + e.getCheck().getMaxVl())
				.replace("%desc%", e.getCheck().getDescription()), main.getConfig().getBoolean("modules.checks.hotbar.bypass-alerts-disabled"));
	}
}
