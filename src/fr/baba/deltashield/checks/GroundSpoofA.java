package fr.baba.deltashield.checks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.baba.deltashield.core.Hack;
import fr.baba.deltashield.utils.PlayerUtil;

public class GroundSpoofA implements Listener {
	Map<UUID, Integer> vl = new HashMap<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void move(PlayerMoveEvent e){
		Player p = e.getPlayer();
		UUID uuid = e.getPlayer().getUniqueId();
		Location to = e.getTo();
		Location from = e.getFrom();
		
		double diff = to.toVector().distance(from.toVector());
		
		if(p.isOnGround() && diff > 0.0 && !PlayerUtil.isOnGround(p) && PlayerUtil.getDistanceToGround(p) >= 2 && to.getY() < from.getY()){
			if(vl.get(uuid) != null && vl.get(uuid) >= 3) Hack.Check(p, "groundspoof", "a", null, e.getFrom());
			
			if(vl.get(uuid) == null){
				vl.put(uuid, 1);
			} else vl.put(uuid, vl.get(uuid) + 1);
		} else if(vl.get(uuid) != null){
			if(vl.get(uuid) >= 3 && p.getLocation().getBlock().getType() == Material.AIR) p.damage(2);
			vl.remove(uuid);
		}
	}
}
