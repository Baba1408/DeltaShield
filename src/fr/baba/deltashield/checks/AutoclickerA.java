package fr.baba.deltashield.checks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.baba.deltashield.Main;
import fr.baba.deltashield.core.Hack;

public class AutoclickerA implements Listener {
	static Map<UUID, Integer> cpsl = new HashMap<>();
	static Map<UUID, Integer> cpsr = new HashMap<>();
	
	public static void launch() {
		Main main = Main.getPlugin(Main.class);
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(main, new Runnable() {
			public void run() {
				System.out.println("Check player's CPS...");
				
				for(Player p : Bukkit.getOnlinePlayers()){
					UUID uuid = p.getUniqueId();
					
					if(!cpsl.isEmpty()){
						System.out.println(uuid + " CPS : " + cpsl.get(uuid));
						if(cpsl.get(uuid) > 300) Hack.Check(Bukkit.getPlayer(uuid), "autoclicker", "a", null, null);
						cpsl.clear();
					}
					
					if(!cpsr.isEmpty()){
						//System.out.println(uuid + " CPS : " + cpsr.get(uuid));
						if(cpsr.get(uuid) > 300) Hack.Check(Bukkit.getPlayer(uuid), "autoclicker", "a", null, null);
						cpsr.clear();
					}
				}
			}
		}, 200, 200);
	}
	
	@EventHandler
	public void click(PlayerInteractEvent e){
		UUID uuid = e.getPlayer().getUniqueId();
		
		//if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.PHYSICAL){
			if(cpsl.containsKey(uuid)){
				cpsl.put(uuid, cpsl.get(uuid) + 1);
			} else cpsl.put(uuid, 1);
			//} else if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			//if(cpsr.containsKey(uuid)){
				//cpsr.put(uuid, cpsr.get(uuid) + 1);
				//} else cpsr.put(uuid, 1);
			//}
	}
}
