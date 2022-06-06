package fr.baba.deltashield.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class PlayerUtil {
	static Map<UUID, Instant> d = new HashMap<>();
	
	public static void setLastDamage(UUID uuid) {
		d.put(uuid, Instant.now());
	}
	
	public static boolean isNoLongerDamage(Player p, int x) {
		if(!d.containsKey(p.getUniqueId())) return false;
		Duration du = Duration.between(d.get(p.getUniqueId()), Instant.now());
		return du.toMillis() > x;
	}
	
	public static boolean isLongerDamage(Player p, int x) {
		if(!d.containsKey(p.getUniqueId())) return false;
		Duration du = Duration.between(d.get(p.getUniqueId()), Instant.now());
		return du.toMillis() < x;
	}
	
	public static int getDistanceToGround(Player p) {
		final Location loc = p.getLocation().clone();
		final double y = loc.getBlockY();
		int distance = 0;
		for(double i = y; i >= 0; i--){
			loc.setY(i);
			if(loc.getBlock().getType().isSolid()){
				break;
			}
			distance++;
		}
		return distance;
	}
	
	public static Material getBlockToGround(Player p) {
		final Location loc = p.getLocation().clone();
		final double y = loc.getBlockY();
		for(double i = y; i >= 0; i--){
			loc.setY(i);
			if(loc.getBlock().getType().isSolid()){
				return loc.getBlock().getType();
			}
		}
		return null;
	}
	
	public static boolean isOnGround(Player p) {
		final Location loc = p.getLocation();
		loc.setY(loc.getY() - 1);
		final Block block = loc.getWorld().getBlockAt(loc);
		return !block.getType().equals(Material.AIR);
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
	
	public static boolean blocksAroundAbove2(Location l) {
		//final Location l = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 2, p.getLocation().getZ());
		//final Block b = l.getBlock();
		final Block b = l.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP);
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
