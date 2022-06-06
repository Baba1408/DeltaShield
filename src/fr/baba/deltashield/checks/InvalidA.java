package fr.baba.deltashield.checks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.baba.deltashield.core.Hack;

public class InvalidA implements Listener {
	Map<UUID, Double> ldist = new HashMap<>();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void move(PlayerMoveEvent event){
		Player p = event.getPlayer();
		UUID uuid = event.getPlayer().getUniqueId();
		
		double dist = event.getFrom().distance(event.getTo());
		
		if(ldist.get(uuid) != null){
			if(ldist.get(uuid) == dist){
				if(!p.getAllowFlight() && p.getGameMode() != GameMode.SPECTATOR && !p.isInsideVehicle()){
					Hack.Check(event.getPlayer(), "invalid", "a", "Dist: " + dist, event.getFrom());
				}
			}
		}
		
		if(p.getLocation().getBlock().getType() == Material.AIR && dist != 0.06272000215528806 && dist != 0.03136000107764403){
			if(p.isOnGround()){
				if(dist >= 0.29) ldist.put(uuid, dist);
			} else if(dist != 0) ldist.put(uuid, dist);
		}
	}
}
