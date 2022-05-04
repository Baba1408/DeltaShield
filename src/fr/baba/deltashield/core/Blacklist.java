package fr.baba.deltashield.core;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.baba.deltashield.Config;
import fr.baba.deltashield.utils.PlayerUtil;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class Blacklist {

	public static void Check(Player p, String check, String type, String arg, String proof){
		//Main main = Main.getPlugin(Main.class);
		int ptype = Config.getBlacklist().getInt("blacklist." + check + ".punishment.type");
		
		switch (ptype){
			case 1:
				p.sendMessage(Config.getMessages().getString("blacklist." + check + ".warn").replace("&", "§"));
				break;
			case 2:
				String matrix = Config.getMessages().getString("blacklist." + check + ".matrix").replace("&", "§");
				
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1, true));
				
				PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE,
						ChatSerializer.a("{\"text\":\"" + matrix + "\",\"obfuscated\":true,\"color\":\"dark_red\"}"), 0, 60, 20);
				
				PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
						ChatSerializer.a("{\"text\":\"" + matrix + "\",\"obfuscated\":true,\"color\":\"dark_red\"}"), 0, 60, 20);
				
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(subtitle);
				
				p.sendMessage("§4§k" + matrix);
				p.sendMessage("§4§k" + matrix);
				p.sendMessage("§4§k" + matrix);
				break;
			case 3:
				String command = Config.getBlacklist().getString("blacklist." + check + ".punishment.command")
					.replace("&", "§")
					.replace("%player%", p.getName())
					.replace("%uuid%", p.getUniqueId().toString())
					.replace("%check%", check)
					.replace("%type%", type.substring(0, 1).toUpperCase() + type.substring(1))
					.replace("%arg%", arg)
					.replace("%proof%", proof);
				
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
				break;
		}
		
		String alert = Config.getMessages().getString("alerts." + check)
				.replace("&", "§")
				.replace("%player%", p.getName())
				.replace("%uuid%", p.getUniqueId().toString())
				.replace("%check%", check)
				.replace("%type%", type)
				.replace("%arg%", arg)
				.replace("%spoof%", proof);
		
		PlayerUtil.sendAlert(alert, null);
	}
}
