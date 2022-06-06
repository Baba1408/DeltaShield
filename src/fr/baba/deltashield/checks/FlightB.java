package fr.baba.deltashield.checks;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.baba.deltashield.core.Hack;

public class FlightB implements Listener {
	
	@EventHandler
	public void move(PlayerMoveEvent event){
		Location from = event.getFrom();
		Location to = event.getTo();
		
		if(from.getY() > to.getY()){
			double disty = from.getY() - to.getY();
			if(disty > 0.1699 && disty < 0.1701){
				Hack.Check(event.getPlayer(), "flight", "b", null, event.getFrom());
			}
		}
	}
}
