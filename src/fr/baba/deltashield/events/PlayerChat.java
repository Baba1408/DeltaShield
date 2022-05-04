package fr.baba.deltashield.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.baba.deltashield.Config;
import fr.baba.deltashield.Main;
import fr.baba.deltashield.core.Blacklist;

public class PlayerChat implements Listener {
	private Main main;
	
	public PlayerChat(Main main) {
		this.main = main;
	}

	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void command(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		String chat = e.getMessage().toLowerCase().substring(2);
		
		if(!p.hasPermission("deltashield.bypass.chats")){
			
			//Too short
			if(Config.getBlacklist().getInt("blacklist.chats.block-too-short") >= chat.length()){
				e.setCancelled(true);
				p.sendMessage(Config.getMessages().getString("blacklist.chats.too-short")
						.replace("&", "§"));
				return;
			}
			
			//Startwith
			for(String w : main.getChatS()){
				if(chat.startsWith(w)){
					e.setCancelled(true);
					Blacklist.Check(p, "chats", "startwith", w, chat);
					return;
				}
			}
					
			//Contains
			for(String w : main.getChatC()){
				if(chat.contains(w)){
					e.setCancelled(true);
					Blacklist.Check(p, "chats", "contains", w, chat);
					return;
				}
			}
		}
	}
}
