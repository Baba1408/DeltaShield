package fr.baba.deltashield.fixs;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Spectator implements Listener {
	static String path = Bukkit.getServer().getClass().getPackage().getName();
	static String serverVersion = path.substring(path.lastIndexOf(".") + 1);
	Boolean spec = false;
	
	@EventHandler
	public void gm(PlayerGameModeChangeEvent e){
		int x = 0;
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.getGameMode() == GameMode.SPECTATOR){
				x++;
			}
		}
		
		if(x < 1){
			if(spec) spec = false;
		} else if(x >= 1) if(!spec) spec = true;
	}
	
	@EventHandler
	public void move(PlayerMoveEvent e){
		if(spec){
			Player p = e.getPlayer();
			
			if(p.getGameMode() != GameMode.SPECTATOR){
				for(Player s : Bukkit.getOnlinePlayers()){
					if(s.getSpectatorTarget() != null && s.getSpectatorTarget() == p){
						Location pl = p.getLocation();
						
						try {
							Class<?> PacketPlayOutPosition = Class.forName("net.minecraft.server." + serverVersion + ".PacketPlayOutPosition");
							Constructor<?> playOutPositionConstructor = PacketPlayOutPosition.getConstructor(
			                	double.class, double.class, double.class, float.class, float.class, Set.class);
							Object posPacket = playOutPositionConstructor.newInstance(
			        			pl.getX(), pl.getY(), pl.getZ(), pl.getYaw(), pl.getPitch(), Collections.emptySet());

							sendPacket(s, posPacket);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	private static void sendPacket(Player player, Object packet) throws Exception {
        Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
        Object craftPlayerObject = craftPlayer.cast(player);

        Method getHandleMethod = craftPlayer.getMethod("getHandle");
        Object handle = getHandleMethod.invoke(craftPlayerObject);
        Object pc = handle.getClass().getField("playerConnection").get(handle);

        Class<?> Packet = Class.forName("net.minecraft.server." + serverVersion + ".Packet");
        Method sendPacketMethod = pc.getClass().getMethod("sendPacket", Packet);

        sendPacketMethod.invoke(pc, packet);
    }
}
