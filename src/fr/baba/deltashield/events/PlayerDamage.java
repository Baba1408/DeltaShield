package fr.baba.deltashield.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import fr.baba.deltashield.utils.PlayerUtil;

public class PlayerDamage implements Listener {

	@EventHandler
	public void damage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			PlayerUtil.setLastDamage(p.getUniqueId());
		}
	}
}
