package fr.baba.deltashield.checks;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import fr.baba.deltashield.core.Hack;
import fr.baba.deltashield.utils.PlayerUtil;

public class InvalidE implements Listener {
	Map<UUID, Instant> d = new HashMap<>();
	Map<UUID, Boolean> c = new HashMap<>();
	ArrayList<Double> v = new ArrayList<>(Arrays.asList(0.41999998688697815, 
			0.33319999363422426,
			0.24813599859093927,
			0.1647732818260721,
			0.08307781780646906,
			0.11760000228882461,
			0.24813599859094637,
			0.164773281826065,
			0.3331999936342236,
			0.2481359985909457,
			0.16477328182606632,
			0.08307781780646728,
			0.0830778178064655,
			0.24813599859095348,
			0.16477328182605788));

	@EventHandler
	public void move(PlayerMoveEvent e){
		Location from = e.getFrom();
		Location to = e.getTo();
		
		if(to.getY() > from.getY()){
			Double x = to.getY() - from.getY();
			
			if(!v.contains(x)){
				Player p = e.getPlayer();
				
				if(!p.hasPotionEffect(PotionEffectType.JUMP) && !p.getAllowFlight() && p.getGameMode() != GameMode.SPECTATOR && !p.isInsideVehicle()){
					if(p.getLocation().getBlock().getType() == Material.AIR){
						if(!p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().toString().startsWith("STATIONARY")){
							if(PlayerUtil.blocksAroundAbove2(to) && PlayerUtil.blocksAround(p)){
								if(PlayerUtil.getBlockToGround(p) != Material.SLIME_BLOCK){
									//if(!d.containsKey(p.getUniqueId())) d.put(p.getUniqueId(), Instant.now().minusSeconds(10));
									//Duration du = Duration.between(d.get(p.getUniqueId()), Instant.now());
									if((x == 0.5 || x == 0.125 || x == 0.25 || x == 0.375) && !c.containsKey(p.getUniqueId()) && !PlayerUtil.blocksAroundBelow(p)){
										
									} else if(PlayerUtil.isNoLongerDamage(p, 600)) Hack.Check(p, "invalid", "e", "DistY: " + x, from);
								} else c.put(p.getUniqueId(), true);
							}
						}
					}
				}
			}
		} else if(c.containsKey(e.getPlayer().getUniqueId())) c.remove(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void damage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			d.put(p.getUniqueId(), Instant.now());
		}
	}
}
