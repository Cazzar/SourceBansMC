package com.ehaqui.EhBans.listener;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.ehaqui.EhBans.EhBans;
import com.ehaqui.EhBans.EhBansManager;
import com.ehaqui.EhBans.util.EhUtil;
import com.ehaqui.EhBans.util.LogHelper;


public class EhPlayerListener implements Listener {

    public static EhBans plugin;
    
    public EhPlayerListener(EhBans instance) {
        plugin = instance;
    }
    
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) 
    {
    	Player player = event.getPlayer();
		
    	// Nao kicar se o Player ja estiver Online
    	if(player.isOnline()) {
        	player.kickPlayer("Voce ja esta Online");
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Voce ja esta Online");  
            return;
        } 
    	
    	
        String playerName 		= event.getPlayer().getName();
        String playerIP         = event.getAddress().getHostAddress();
		String playerHostname 	= "";
		
		if (playerIP == "" || playerIP == null) 
		{
			LogHelper.severe("Warning! Couldn't load " + playerName + "'s IP. Possibly plugins conflict");
			return;
		}
		
		if(plugin.debug)
		{
			try{
				playerHostname = InetAddress.getByName(playerIP).getHostName();
			}catch (UnknownHostException e) {}
	
			LogHelper.info(playerName + " connected. Detected ip: " + playerIP + " Detected hostname: " + playerHostname);
		}
		
    	if(EhBansManager.checkBan(playerName, playerIP))
    	{
        	EhBansManager.addBlockBan(playerName, playerIP);

        	event.disallow(Result.KICK_OTHER, EhUtil.colorize(plugin.messageBan));
		}
    }
    
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) 
    {    
    }

    
    
    // Remove "x left the game" 
    @EventHandler
    public void onPlayerKick (PlayerKickEvent event) 
    {
		if((event.getReason()).equals(plugin.lastBanReason))
		{
			event.setLeaveMessage(null);
		}
	}
    

}
