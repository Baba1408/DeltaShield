package fr.baba.deltashield.fixs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class TabCompletePacket {

	public static void launch(Plugin main) {
		if(Bukkit.getPluginManager().getPlugin("ProtocolLib") == null){
			ConsoleCommandSender s = Bukkit.getServer().getConsoleSender();
			s.sendMessage("ProtocolLib is not present on the server, anti tab completion will not be activated, please put ProtocolLib and reload the plugin");
			return;
		}
		
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		
		//System.out.println(manager.getPacketListeners().toString());
		
		//if(manager.getPacketListeners().toString().contains("DeltaShield")) return;
		
		manager.addPacketListener(new PacketAdapter(main, new PacketType[] {PacketType.Play.Client.TAB_COMPLETE}){
		
			@SuppressWarnings("rawtypes")
			public void onPacketReceiving(PacketEvent event){
				if ((event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) 
					&& (!event.getPlayer().hasPermission("deltashield.tabcompletion"))
					&& (((String)event.getPacket().getStrings().read(0)).startsWith("/"))
	        		&& (((String)event.getPacket().getStrings().read(0)).split(" ").length == 1)
	        		&& (main.getConfig().getInt("anti-tab-completion.minimum-length") == -1 || event.getPacket().getStrings().read(0).length() < main.getConfig().getInt("anti-tab-completion.minimum-length"))){
					
					//System.out.println("" + event.getPacket().getStrings().read(0).length());
					
					event.setCancelled(true);
					
					List<?> list = new ArrayList();
					List<?> extra = new ArrayList();
					
					String[] tabList = new String[list.size() + extra.size()];
					
					for (int index = 0; index < list.size(); index++) {
						tabList[index] = ((String)list.get(index));
					}
					
					for (int index = 0; index < extra.size(); index++) {
						tabList[(index + list.size())] = ('/' + (String)extra.get(index));
					}
					
					PacketContainer tabComplete = manager.createPacket(PacketType.Play.Server.TAB_COMPLETE);
					tabComplete.getStringArrays().write(0, tabList);
					
					try {
						manager.sendServerPacket(event.getPlayer(), tabComplete);
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
	    });
	}
}
