package fr.baba.deltashield.checks;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;import org.bukkit.event.player.PlayerMoveEvent;

import fr.baba.deltashield.core.Hack;

public class ClimbA implements Listener {
	ArrayList<Double> v = new ArrayList<>(Arrays.asList(0.11760000228882461, 
			0.41999998688697815,
			0.33319999363422426,
			0.24813599859093927,
			0.1647732818260721,
			0.08307781780646906,
			0.3724841291914913,
			0.3784970419114586,
			0.0368480029601983));

	@EventHandler
	public void move(PlayerMoveEvent e){
		Location from = e.getFrom();
		
		if(from.getBlock().getType() == Material.LADDER || from.getBlock().getType() == Material.VINE){
			Location to = e.getTo();
			Player p = e.getPlayer();	
			
			Double x = null;
			if(to.getY() > from.getY()){
				x = to.getY() - from.getY();
				p.sendMessage("" + x);
				if(x != 0.11760000228882461 && !v.contains(x)) Hack.Check(p, "climb", "a", "DistY: " + x, from);
			}
		}
	}
}
