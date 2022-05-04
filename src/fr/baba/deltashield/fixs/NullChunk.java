package fr.baba.deltashield.fixs;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class NullChunk implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent e){
		Location to = e.getTo();
		World world = to.getWorld();
		Chunk chunk = to.getChunk();

		if(chunk == null || !world.isChunkLoaded(chunk)){
			e.setCancelled(true);
		}
	}
}
