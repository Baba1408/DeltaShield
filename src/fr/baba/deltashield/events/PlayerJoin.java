package fr.baba.deltashield.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.baba.deltashield.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerJoin implements Listener {
	private Main main;

	public PlayerJoin(Main main) {
		this.main = main;
	}

	@EventHandler
	public void join(PlayerJoinEvent e){
		Player p = e.getPlayer();
		
		if(p.hasPermission("deltashield.updates")){
			if(main.getUpdateMSG() != null && !main.getUpdateMSG().isEmpty()){
				p.sendMessage(main.getUpdateMSG());
				if(main.getConfig().getBoolean("plugin-updater.suggest-spigot-link")){
					TextComponent component = new TextComponent("§6Spigot link :§e Click here to download it!");
			        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/deltashield.101756/"));
			        p.spigot().sendMessage(component);
				}
			}
		}
	}
}
