package fr.baba.deltashield.checks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import fr.baba.deltashield.core.Hack;

public class InventoryA implements Listener {
	static Map<UUID, Location> map = new HashMap<>();
	
	
	public void click(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		
		if(!map.containsKey(p.getUniqueId())){
			map.put(p.getUniqueId(), p.getLocation());
		} else {
			if(p.getLocation() != map.get(p.getUniqueId())) Hack.Check(p, "inventory", "a", null, map.get(p.getUniqueId()));
			map.put(p.getUniqueId(), p.getLocation());
		}
	}
	
	
	public void close(InventoryCloseEvent e){
		if(map.containsKey(e.getPlayer().getUniqueId())) map.remove(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void ClickInventory(InventoryClickEvent event){
		Player p = (Player) event.getWhoClicked();
		
		if(p.getGameMode() != GameMode.CREATIVE){
			if(p.isSprinting() | p.isSneaking()){
				Hack.Check(p, "inv", "a", null, null);
			}
		}
	}
}
