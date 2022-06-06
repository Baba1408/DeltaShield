package fr.baba.deltashield.checks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.baba.deltashield.core.Hack;
import fr.baba.deltashield.utils.PlayerUtil;

public class FlightC implements Listener {
	Map<UUID, Boolean> a = new HashMap<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void move(PlayerMoveEvent e){
		Location from = e.getFrom();
		Location to = e.getTo();
		Player p = e.getPlayer();
		
		if(to.getY() > from.getY()){
			if(a.containsKey(p.getUniqueId())){
				a.remove(p.getUniqueId());
				if(!p.getAllowFlight() && !p.isInsideVehicle()) {
					if(PlayerUtil.blocksAround(p) && PlayerUtil.blocksAroundBelow(p)){
						if(PlayerUtil.isNoLongerDamage(p, 20)) Hack.Check(p, "flight", "c", "OnGround: " + p.isOnGround(), from);
					}
				}
			}
		} else if(!a.containsKey(p.getUniqueId())) a.put(p.getUniqueId(), true);
	}
}
