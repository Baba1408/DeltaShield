package fr.baba.deltashield.checks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import fr.baba.deltashield.Main;
import fr.baba.deltashield.core.Hack;

public class SpeedA implements Listener {
	Map<UUID, Boolean> x = new HashMap<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void move(PlayerMoveEvent e){
		Player p = e.getPlayer();
		
		if(p.isOnGround() && e.getFrom().distance(e.getTo()) > 1){
			if(!p.hasPotionEffect(PotionEffectType.SPEED)){
				if(x.get(p.getUniqueId()) == null){
					Hack.Check(p, "speed", "a", e.getFrom());
				}
			}
		}
	}
	
	public void damage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Main main = Main.getPlugin(Main.class);
			Player p = (Player) e.getEntity();
			UUID uuid = p.getUniqueId();
			
			if(x.get(uuid) == null){
				x.put(uuid, true);
				
				Bukkit.getScheduler().runTaskLater(main, new Runnable() {
					public void run() {
						x.remove(uuid);
					}
				}, 20L);
			}
		}
	}
}
