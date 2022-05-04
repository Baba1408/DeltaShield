package fr.baba.deltashield.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import fr.baba.deltashield.Main;
import fr.baba.deltashield.core.Blacklist;

public class PlayerCommand implements Listener {
	private Main main;
	
	public PlayerCommand(Main main) {
		this.main = main;
	}

	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void command(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		String command = e.getMessage().toLowerCase().substring(1);
		String[] args = command.split(" ");
		
		if(!p.hasPermission("deltashield.bypass.commands")){
			
			//Startwith
			for(String w : main.getCommandsS()){
				if(command.startsWith(w)){
					e.setCancelled(true);
					Blacklist.Check(p, "commands", "startwith", w, command);
					return;
				}
			}
		
			//Contains
			for(String w : main.getCommandsC()){
				if(command.contains(w)){
					e.setCancelled(true);
					Blacklist.Check(p, "commands", "contains", w, command);
					return;
				}
			}
		}
		
		//Redirection
		if(main.getRedirected().get(args[0]) != null){
			//p.sendMessage("Execute : " + command);
			String redir = null;
			if(command.length() > args[0].length()){
				redir = main.getRedirected().get(args[0]) + " " + command.substring(args[0].length() + 1);
			} else redir = main.getRedirected().get(args[0]);
			//p.sendMessage("Redirected : " + redir);
			p.performCommand(redir);
			e.setCancelled(true);
		}
	}
}
