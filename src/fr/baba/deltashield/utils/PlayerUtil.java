package fr.baba.deltashield.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import fr.baba.deltashield.Main;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerUtil {
	public static void sendAlert(String a, String d) {
		Main main = Main.getPlugin(Main.class);
		
		TextComponent m = new TextComponent(a);
		if(d != null && !d.isEmpty()) m.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(d).create()));
		
		for(Player s : Bukkit.getOnlinePlayers()){
			if(s.hasPermission("deltashield.alerts")){
				if(main.getAlerts().get(s.getUniqueId()) == null){
					s.spigot().sendMessage(m);
				}
			}
		}
	}
	
	public static int getDistanceToGround(Player p) {
		final Location loc = p.getLocation().clone();
		final double y = loc.getBlockY();
		int distance = 0;
		for (double i = y; i >= 0; i--) {
			loc.setY(i);
			if (loc.getBlock().getType().isSolid()) {
				break;
			}
			distance++;
		}
		return distance;
	}
	
	public static boolean isOnGround(Player p) {
		final Location loc = p.getLocation();
		loc.setY(loc.getY() - 1);
		final Block block = loc.getWorld().getBlockAt(loc);
		return !block.getType().equals(Material.AIR);
	}
	
	public static boolean blocksAroundBelow(Player p) {
		final Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
		return b.getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.WEST).getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.NORTH).getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.EAST).getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.SOUTH).getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.NORTH_EAST).getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.NORTH_WEST).getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.SOUTH_EAST).getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.SOUTH_WEST).getType().equals(Material.AIR);
	}
	
	public static boolean blocksAround(Player p) {
		final Block b = p.getLocation().getBlock();
		return b.getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.WEST).getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.NORTH).getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.EAST).getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.SOUTH).getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.NORTH_EAST).getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.NORTH_WEST).getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.SOUTH_EAST).getType().equals(Material.AIR)
				&& b.getRelative(BlockFace.SOUTH_WEST).getType().equals(Material.AIR);
	}
}
