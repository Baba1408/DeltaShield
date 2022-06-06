package fr.baba.deltashield.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

	@EventHandler
	public void bb(BlockBreakEvent e){
		if(e.getPlayer() != null){
			
		}
	}
}
