package fr.baba.deltashield.checks;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import fr.baba.deltashield.core.Hack;

public class SpeedA implements Listener {
	Map<UUID, Instant> d = new HashMap<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void move(PlayerMoveEvent e){
		Player p = e.getPlayer();
		
		if(p.isOnGround() && !p.getAllowFlight() && e.getFrom().distance(e.getTo()) > 0.62){
			if(e.getFrom().getY() == e.getTo().getY()){
				if(!p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().toString().endsWith("ICE")){
					if(p.getWalkSpeed() < 0.3 && !p.hasPotionEffect(PotionEffectType.SPEED)){
						if(!d.containsKey(p.getUniqueId())) d.put(p.getUniqueId(), Instant.now().minusSeconds(10));
						Duration du = Duration.between(d.get(p.getUniqueId()), Instant.now());
						if(du.toMillis() > 600){
							Hack.Check(p, "speed", "a", "Dist:" + e.getFrom().distance(e.getTo()) + " > 0.64 | WalkSpeed: " + p.getWalkSpeed(), e.getFrom());
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void damage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			d.put(p.getUniqueId(), Instant.now());
		}
	}
}
