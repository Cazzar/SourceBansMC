package com.ehaqui.EhBans.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ehaqui.EhBans.EhBans;

public class EhUtil {

	public static EhBans plugin;

    public EhUtil(EhBans instance) 
    {
        plugin = instance;
    }
    
    
    /*
	 *  Tranforma o ID de Cores para Cores
	 */
	public static String colorize(String message) 
	{
        for (ChatColor color : ChatColor.values()) 
        {
            message = message.replaceAll(String.format("&%c", color.getChar()), color.toString());
        }

        return message;
    }
	

    public static void sendMessage(Player sender, String message)
    {
    	String[] messages = message.split("\n");
    			 
    	for(int x = 0; x < messages.length; x++) 
    	{
    		sender.sendMessage(colorize(messages[x]));
    	}
    }
    
	public static void sendMessage(CommandSender sender, String message) 
	{
		String[] messages = message.split("\n");
		 
    	for(int x = 0; x < messages.length; x++) 
    	{
    		if(sender instanceof Player)
    		{
    			sender.sendMessage(colorize(messages[x]));
    		}
    		else
    		{
    			String colorsStr = "([§|&][0-9|a-f])";
        		messages[x] = messages[x].replaceAll(colorsStr, "");
        		
        		log.info(messages[x]); 
    		}
    	}
	}
	
	public static void sendMessage(Player sender, String message, boolean prefix) 
	{
		if(prefix)
		{
			String[] messages = message.split("\n");
			 
	    	for(int x = 0; x < messages.length; x++) 
	    	{
	    		sender.sendMessage(colorize(plugin.plugin_prefix + " " +messages[x]));
	    	}
		}
		else
		{
			String[] messages = message.split("\n");
			 
	    	for(int x = 0; x < messages.length; x++) 
	    	{
	    		sender.sendMessage(colorize(messages[x]));
	    	}
		}
	}
    
    public static void broadcastMessage(String message)
    {
    	String[] messages = message.split("\n");
		 
    	for(int x = 0; x < messages.length; x++) 
    	{
    		plugin.getServer().broadcastMessage(colorize(messages[x])); 
    	}
    }
    
    public static boolean hasPermission(Player player, String permission)
    {
    	String prefixPermission = plugin.prefixPermission;
    	permission = prefixPermission + "." + permission;
    	
    	
        if(plugin.permission != null) 
        {
        	if(plugin.permission.has(player, permission))
        	{
        		return true;
        	}
        	else
        	{
        		return false;
        	}
        	
        }
        else
        {
        	if(player.hasPermission(permission))
        	{
        		return true;
        	}
        	else
        	{
        		return false;
        	}
        }
    }


	public static boolean hasPermission(CommandSender sender, String permission) 
	{
		if(!(sender instanceof Player))
		{
			return true;
		}
		else
		{
			Player player = (Player) sender;
			
			String prefixPermission = plugin.prefixPermission;
	    	permission = prefixPermission + "." + permission;
	    	
	    	
	        if(plugin.permission != null) 
	        {
	        	if(plugin.permission.has(player, permission))
	        	{
	        		return true;
	        	}
	        	else
	        	{
	        		return false;
	        	}
	        	
	        }
	        else
	        {
	        	if(player.hasPermission(permission))
	        	{
	        		return true;
	        	}
	        	else
	        	{
	        		return false;
	        	}
	        }
		}
	}
	
	public static boolean hasPermission(CommandSender sender, String permission, boolean broadcast) 
	{
		boolean result;
		
		if(!(sender instanceof Player))
		{
			result = true;
		}
		else
		{
			Player player = (Player) sender;
			
			String prefixPermission = plugin.prefixPermission;
	    	permission = prefixPermission + "." + permission;
	    	
	    	
	        if(plugin.permission != null) 
	        {
	        	if(plugin.permission.has(player, permission))
	        	{
	        		result = true;
	        	}
	        	else
	        	{
	        		result = false;
	        	}
	        	
	        }
	        else
	        {
	        	if(player.hasPermission(permission))
	        	{
	        		result = true;
	        	}
	        	else
	        	{
	        		result = false;
	        	}
	        }
		}
		
		if(!result && broadcast)
		{
			sendMessage(sender, "&cVoce nao tem Permissao\n* Node: " + permission);
		}
		
		return result;
	}
}
