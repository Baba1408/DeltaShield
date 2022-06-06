package fr.baba.deltashield.checks;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.baba.deltashield.core.Hack;

public class StealA implements Listener {
	Map<UUID, Instant> i = new HashMap<>();
	
	@EventHandler
	public void click(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		UUID uuid = p.getUniqueId();
		
		if(i.containsKey(uuid)){
			Duration du = Duration.between(i.get(p.getUniqueId()), Instant.now());
			if(du.toMillis() < 5) Hack.Check(p, "steal", "a", "Du: " + du.toMillis() + "ms < 5ms", null);
		}
		
		i.put(uuid, Instant.now());
	}
}
