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

public class StealB implements Listener {
	Map<UUID, Instant> i = new HashMap<>();
	Map<UUID, Duration> d = new HashMap<>();
	
	@EventHandler
	public void click(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		UUID uuid = p.getUniqueId();
		
		if(i.containsKey(uuid)){
			Duration du = Duration.between(i.get(p.getUniqueId()), Instant.now());
			if(d.containsKey(uuid) && du.toMillis() == d.get(uuid).toMillis()) Hack.Check(p, "steal", "b", "Duration: " + du.toMillis() + "ms", null);
			d.put(uuid, du);
		}
		
		i.put(uuid, Instant.now());
	}
}
