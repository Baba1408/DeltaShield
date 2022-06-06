package fr.baba.deltashield.checks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.baba.deltashield.core.Hack;
import fr.baba.deltashield.utils.PlayerUtil;

public class FlightA implements Listener {
	Map<UUID, Boolean> map = new HashMap<>();

	@EventHandler
	public void move(PlayerMoveEvent event){
		Player p = event.getPlayer();
		UUID uuid = event.getPlayer().getUniqueId();
		
		Location from = event.getFrom();
		Location to = event.getTo();
		
		if(p.getLocation().getBlock().getType() == Material.AIR && !p.isInsideVehicle() && p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR){
			double y = to.getY() - from.getY();
			if(!p.getAllowFlight() && to.getY() >= from.getY() && y < 0.0001){
				if(map.get(uuid) != null){
					if(PlayerUtil.blocksAroundBelow(p) && PlayerUtil.blocksAround(p)){
						Hack.Check(p, "flight", "a", "Ydif: " + y, event.getFrom());
					}
				}
				
				map.put(uuid, true);
			} else if(map.get(uuid) != null) map.remove(uuid);
		}
	}
}
