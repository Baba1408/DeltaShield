package fr.baba.deltashield.checks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.baba.deltashield.core.Hack;

public class InvalidD implements Listener {

	@EventHandler
	public void move(PlayerMoveEvent e){
		Player p = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		
		//XYZ
		if(from.distance(to) != 0 && from.distance(to) == Math.round(from.distance(to))){
			Hack.Check(p, "invalid", "d", "XYZ | Dist: " + from.distance(to), from);
		}
		
		//Y
		Double y = null;
		if(from.getY() > to.getY()){
			y = from.getY() - to.getY();
		} else if(to.getY() < from.getY()){
			y = to.getY() - from.getY();
		}
		
		if(y != null && !p.isInsideVehicle() && y == Math.round(y)) Hack.Check(p, "invalid", "d", "Y | Dist: " + from.distance(to) + " | DistY: " + y, from);
		
		//XZ
		//from.setY(0);
		//to.setY(0);
		//if(from.distance(to) == Math.round(from.distance(to))){
		//Hack.Check(p, "invalid", "d", from);
		//}
	}
}
