package fr.baba.deltashield.checks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.baba.deltashield.core.Hack;
import fr.baba.deltashield.utils.PlayerUtil;

public class InvalidC implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void move(PlayerMoveEvent e){
		Player p = e.getPlayer();
		
		Location from = e.getFrom();
		Location to = e.getTo();
		
		if(!p.isOnGround() && PlayerUtil.blocksAroundBelow(p) && PlayerUtil.getDistanceToGround(p) > 1){
			if(from.getX() != to.getX() && from.getZ() != to.getZ()){
				if(p.getVelocity().getX() == 0 && p.getVelocity().getZ() == 0){
					//p.sendMessage("X : " + p.getVelocity().getX());
					//p.sendMessage("Z : " + p.getVelocity().getZ());
					
					Hack.Check(p, "invalid", "c", from);
				}
			}
		}
	}
}
