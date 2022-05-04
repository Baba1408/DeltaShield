package fr.baba.deltashield.checks;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.baba.deltashield.core.Hack;

public class InvalidB implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void move(PlayerMoveEvent e){
		Player p = e.getPlayer();
		Location l = p.getLocation();
		Location l2 = l;
		l2.setY(l.getY() - 1);
		
		if(p.isOnGround()){
			if(p.getVelocity().getY() != -0.0784000015258789 && p.getVelocity().getY() != -0.1552320045166016){
				if(l.getBlock().getType() == Material.AIR && !l2.getBlock().getType().toString().endsWith("_STAIRS") && !l2.getBlock().getType().toString().endsWith("_SLAB2")){
					Hack.Check(p, "invalid", "b", e.getFrom());
				}
			}
		}
	}
}
