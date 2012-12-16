package com.ehaqui.EhBans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ehaqui.EhBans.EhBans;
import com.ehaqui.EhBans.EhBansManager;
import com.ehaqui.EhBans.util.EhUtil;

public class EhCommands implements CommandExecutor{
	
	 public static EhBans plugin;

	 public EhCommands(EhBans instance) {
		 plugin = instance;
	 }
	 

	 public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	    boolean success = false;
			
	    if(command.getName().equalsIgnoreCase("ban")){
	    	
	    	if(!EhUtil.has(sender, "ban")){
	        	EhUtil.sendMessage(sender, "&c" + plugin.t.s("COMMAND_BAN_NOPERMISSION"));
	        	return true;
	    	}
	    	
	    	if(args.length > 0){
	    		onBanCommand(sender, args);
	    	}else{
		    	EhUtil.sendMessage(sender, plugin.pluginPrefix + " " + plugin.t.s("COMMAND") + " Ban  &8-----------------");
		    	EhUtil.sendMessage(sender, "&c/ban [player] [tempo] [razao]");
		    	EhUtil.sendMessage(sender, "");
		    	EhUtil.sendMessage(sender, "&f" + plugin.t.s("COMMAND_BAN_HELP1"));
		    	EhUtil.sendMessage(sender, "&f" + plugin.t.s("COMMAND_BAN_HELP2"));
	    	}
	    	
	    	success = true;
		   
	    }else if(command.getName().equalsIgnoreCase("unban")){
	    	
	    	if(!EhUtil.has(sender, "unban")){
	        	EhUtil.sendMessage(sender, "&c" + plugin.t.s("COMMAND_UNBAN_NOPERMISSION"));
	        	return true;
	    	}
	    	
	    	if(args.length > 0){
	    		onUnbanCommand(sender, args);
	    	}else{
	    		EhUtil.sendMessage(sender, plugin.pluginPrefix + " " + plugin.t.s("COMMAND") + " Unban  &8-----------------");
		    	EhUtil.sendMessage(sender, "&c/unban [player] [razao]");
		    	EhUtil.sendMessage(sender, "");

	    	}
	    	
	    	success = true;
	    	
	    }else if(command.getName().equalsIgnoreCase("kick")){
	    	
	    	if(!EhUtil.has(sender, "kick")){
	        	EhUtil.sendMessage(sender, "&c" + plugin.t.s("COMMAND_KICK_NOPERMISSION"));
	        	return true;
	    	}
	    	
	    	if(args.length > 0){
	    		onKickCommand(sender, args);
	    	}else{
	    		EhUtil.sendMessage(sender, plugin.pluginPrefix + " " + plugin.t.s("COMMAND") + " Kick  &8-----------------");
		    	EhUtil.sendMessage(sender, "&c/kick [player]");
		    	EhUtil.sendMessage(sender, "");

	    	}
	    	
	    	success = true;
	    	
	    }else if(command.getName().equalsIgnoreCase("fban")){
	    	
	    	if(!EhUtil.has(sender, "fban", false)){
	    		return true;
	    	}
	    	
	    	if(args.length == 0)
	    	{
	    		EhUtil.broadcastMessage("");
	    		EhUtil.broadcastMessage(plugin.t.s("BAN_MESSAGE_HEADER"), plugin.pluginPrefix, sender.getName());
	        	EhUtil.broadcastMessage("&8-------------------------------");
	        	EhUtil.broadcastMessage("&c"+ plugin.t.s("BAN_MESSAGE_1") +" &eSeuJuao");
	        	EhUtil.broadcastMessage("&c"+ plugin.t.s("BAN_MESSAGE_2") +" &eOrdem Aquiiiii!");
	        	EhUtil.broadcastMessage("");
	    	}
	    	else
	    	{
	    		EhUtil.broadcastMessage("");
	        	EhUtil.broadcastMessage(plugin.t.s("BAN_MESSAGE_HEADER"), plugin.pluginPrefix, sender.getName());
	        	EhUtil.broadcastMessage("&8-------------------------------");
	        	EhUtil.broadcastMessage("&c"+ plugin.t.s("BAN_MESSAGE_1") +" &eSeuJuao");
	        	EhUtil.broadcastMessage("&c"+ plugin.t.s("BAN_MESSAGE_2") +" &e%s", EhBansManager.parseReson(args));
	        	EhUtil.broadcastMessage("");
	    	}
	    	
	    	success = true;
	    	
	    }else if(command.getName().equalsIgnoreCase("ehbans") || command.getName().equalsIgnoreCase("ebans")){

	    	// Comando Mostrar Ajuda
	    	if (args.length == 0 || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) 
	    	{
				onBanHelpCommand(sender, args);
				
				success = true;
			
				
			// Comando SourceBans
            }else if(args[0].equalsIgnoreCase("sb")){
                
                if(!EhUtil.has(sender, "admin", false))
                {
                    success = true;
                    return true;
                }
                
                if(args[1].equalsIgnoreCase("info"))
                {
                    getSBInfo(sender, args);
                    success = true;
                }
                else
                    success = false;
                
                
            // Comando Mostrar Versao
			}else if(args[0].equalsIgnoreCase("reload")){
				
				if(!EhUtil.has(sender, "admin", false)){
                	success = true;
                	return true;
				}
				
            	onReloadCommand(sender, args);
                
                success = true;
			
                
			 // Comando Mostrar Versao
			}else if(args[0].equalsIgnoreCase("versao")){

				onVersionCommand(sender, args);
			     
				success = true;
			}
	    }
			
		
	    if (!success) {
	    	EhUtil.sendMessage(sender, "&c" + plugin.t.s("COMMAND_ERROR"), plugin.pluginCommand);
	    }
		     
		return true;
	}

	 
	 
	private void getSBInfo(CommandSender sender, String[] args) 
	{
	    
	    String srvIP     = plugin.getServer().getIp();
	    int srvPort      = plugin.getServer().getPort();
        
	    EhUtil.sendMessage(sender,"");
	    EhUtil.sendMessage(sender, true, "SourceBans Info &8-----------------");
	    EhUtil.sendMessage(sender,"");
	    EhUtil.sendMessage(sender,"&cIP:    &e%s", srvIP);
	    EhUtil.sendMessage(sender,"&cPorta: &e%s", srvPort);
	    EhUtil.sendMessage(sender,"");   
    }


    /*
	  * Comando Mostrar Uso do Chat
	  */
	public void onBanHelpCommand(CommandSender sender, String[] args) 
	{
    	EhUtil.sendMessage(sender,"");
    	EhUtil.sendMessage(sender, plugin.pluginPrefix + plugin.t.s("COMMAND_HELP") + " &8-----------------");
    	EhUtil.sendMessage(sender, "");
    	
    	if(EhUtil.has(sender, "ban"))
    		EhUtil.sendMessage(sender, "/ban [player] [tempo] [razao]  - &7" + plugin.t.s("COMMAND_BAN"));
    	
    	if(EhUtil.has(sender, "unban"))
    		EhUtil.sendMessage(sender, "/unban [player] [razao]  - &7" + plugin.t.s("COMMAND_UNBAN"));
    	
    	if(EhUtil.has(sender, "kick"))
    		EhUtil.sendMessage(sender, "/kick [player] <razao>  - &7" + plugin.t.s("COMMAND_KICK"));
    	
    	if(EhUtil.has(sender, "fban"))
    		EhUtil.sendMessage(sender, "/fban <razao>  - &7" + plugin.t.s("COMMAND_BAN_FAKE"));
    	
        if(EhUtil.has(sender, "reload"))
        	EhUtil.sendMessage(sender, "/ehban reload  - &7" + plugin.t.s("COMMAND_RELOAD_CONFIG"));
        
        EhUtil.sendMessage(sender, "/ehban versao - &7" + plugin.t.s("COMMAND_PLINFO"));
	}
	
   
    
    /*
     *  Comando Recarregar Configurações
     */
    public void onReloadCommand(CommandSender sender, String[] args) 
    {
    	plugin.loadConfiguration();

    	EhUtil.sendMessage(sender, plugin.pluginPrefix + "&4" + plugin.t.s("CONFIG_RELOAD_ALL"));
    }
    
    
    /*
     *  Bane o Player
     */
    public void onBanCommand(CommandSender sender, String[] args) 
    {
        EhBansManager.addBan(sender, args);
    }
    
    
    /*
     *  Desbane o Player
     */
    public void onUnbanCommand(CommandSender sender, String[] args) 
    {
        EhBansManager.removeBan(sender, args);
    }
    
    
    private void onKickCommand(CommandSender sender, String[] args) 
    {
    	Player player = plugin.getServer().getPlayer(args[0]);
    	
    	if(player != null)
    	{ 
    		args[0] = "";
    		
    		String playerName 	= player.getName();
    		String razao 		= EhUtil.colorize("&c"+ plugin.t.s("BAN_MESSAGE_2") +" &e" + EhBansManager.parseReson(args));
    		
    		if(args.length == 1)
    			razao = plugin.t.s("KICK_MESSAGE_DEFAULT");
    		
    		player.kickPlayer(razao);
    		
    		EhUtil.broadcastMessage("");
        	EhUtil.broadcastMessage(plugin.t.s("BAN_MESSAGE_HEADER"), plugin.pluginPrefix, sender.getName());
        	EhUtil.broadcastMessage("&8-------------------------------");
    		EhUtil.broadcastMessage("&c" + plugin.t.s("KICK_MESSAGE"), playerName);
    		
    		if(args.length > 2)
    			EhUtil.broadcastMessage(razao);
    		
        	EhUtil.broadcastMessage("");
    	}
    	else
    	{
    		EhUtil.sendMessage(sender, "&c" + plugin.t.s("KICK_PLAYEROFFLINE"));
    	}    	
    }
    
    public void onVersionCommand(CommandSender sender, String[] args) 
    {
    	EhUtil.sendMessage(sender,"");
    	EhUtil.sendMessage(sender, plugin.pluginPrefix + " Informacoes  &8-----------------");
    	EhUtil.sendMessage(sender, "");
    	EhUtil.sendMessage(sender, "Versao: " + plugin.pluginVersion);
    	EhUtil.sendMessage(sender, "Desenvolvido por Lucas Didur");
    	EhUtil.sendMessage(sender, "Site: http://ehaqui.com");
    	EhUtil.sendMessage(sender, "");
    }
    
    

}
