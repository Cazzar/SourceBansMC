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
	    	
	    	if(!EhUtil.hasPermission(sender, "ban")){
	        	EhUtil.sendMessage(sender, "&cVoce nao tem prmissao para Banir");
	        	return true;
	    	}
	    	
	    	if(args.length > 0){
	    		onBanCommand(sender, args);
	    	}else{
		    	EhUtil.sendMessage(sender, plugin.plugin_refix + " Comando Ban  &8-----------------");
		    	EhUtil.sendMessage(sender, "&c/ban [player] [tempo] [razao]");
		    	EhUtil.sendMessage(sender, "");
		    	EhUtil.sendMessage(sender, "&fTempo no formato: 1h = 1 hora, 1d = 1 dia, 1s = 1 semana");
		    	EhUtil.sendMessage(sender, "&fTempo no formato: 1mi = 1 min, 1m = 1 mês");
	    	}
	    	
	    	success = true;
		   
	    }else if(command.getName().equalsIgnoreCase("unban")){
	    	
	    	if(!EhUtil.hasPermission(sender, "unban")){
	        	EhUtil.sendMessage(sender, "&cVoce nao tem prmissao para Desbanir");
	        	return true;
	    	}
	    	
	    	if(args.length > 0){
	    		onUnbanCommand(sender, args);
	    	}else{
	    		EhUtil.sendMessage(sender, plugin.plugin_refix + " Comando Unban  &8-----------------");
		    	EhUtil.sendMessage(sender, "&c/unban [player] [razao]");
		    	EhUtil.sendMessage(sender, "");

	    	}
	    	
	    	success = true;
	    	
	    }else if(command.getName().equalsIgnoreCase("kick")){
	    	
	    	if(!EhUtil.hasPermission(sender, "kick")){
	        	EhUtil.sendMessage(sender, "&cVoce nao tem prmissao para Kicar");
	        	return true;
	    	}
	    	
	    	if(args.length > 0){
	    		onKickCommand(sender, args);
	    	}else{
	    		EhUtil.sendMessage(sender, plugin.plugin_refix + " Comando Unban  &8-----------------");
		    	EhUtil.sendMessage(sender, "&c/kick [player]");
		    	EhUtil.sendMessage(sender, "");

	    	}
	    	
	    	success = true;
	    	
	    }else if(command.getName().equalsIgnoreCase("fban")){
	    	
	    	if(!EhUtil.hasPermission(sender, "fban", true)){
	    		return true;
	    	}
	    	
	    	if(args.length == 0)
	    	{
	    		EhUtil.broadcastMessage("");
	        	EhUtil.broadcastMessage(plugin.plugin_refix + "por &c" + sender.getName());
	        	EhUtil.broadcastMessage("&8-------------------------------");
	        	EhUtil.broadcastMessage("&cPlayer: &eSeuJuao");
	        	EhUtil.broadcastMessage("&cRazao: &eNao pode Fazer isso!");
	        	EhUtil.broadcastMessage("");
	    	}
	    	else
	    	{
	    		EhUtil.broadcastMessage("");
	        	EhUtil.broadcastMessage(plugin.plugin_refix + "por &c" + sender.getName());
	        	EhUtil.broadcastMessage("&8-------------------------------");
	        	EhUtil.broadcastMessage("&cPlayer: &eSeuJuao");
	        	EhUtil.broadcastMessage("&cRazao: &e" + EhBansManager.arrayToString(args));
	        	EhUtil.broadcastMessage("");
	    	}
	    	
	    	success = true;
	    	
	    }else if(command.getName().equalsIgnoreCase("ehbans") || command.getName().equalsIgnoreCase("ebans")){

	    	// Comando Mostrar Ajuda
	    	if (args.length == 0 || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) 
	    	{
				onBanHelpCommand(sender, args);
				
				success = true;
				
		    // Comando Mostrar Versao
			}else if(args[0].equalsIgnoreCase("reload")){
				
				if(!EhUtil.hasPermission(sender, "admin", true)){
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
	    	EhUtil.sendMessage(sender, "&cArgumentos Invalidos! Use '/ebans help' para ver a lista de Comandos Validos.");
	    }
		     
		return true;
	}

	 
	 
	/*
	  * Comando Mostrar Uso do Chat
	  */
	public void onBanHelpCommand(CommandSender sender, String[] args) 
	{
    	EhUtil.sendMessage(sender,"");
    	EhUtil.sendMessage(sender, plugin.plugin_refix + " Comando Ajuda  &8-----------------");
    	EhUtil.sendMessage(sender, "");
    	
    	if(EhUtil.hasPermission(sender, "ban"))
    		EhUtil.sendMessage(sender, "/ban [player] [tempo] [razao]  - &7Ban Player");
    	
    	if(EhUtil.hasPermission(sender, "unban"))
    		EhUtil.sendMessage(sender, "/unban [player] [razao]  - &7UnBan Player");
    	
    	if(EhUtil.hasPermission(sender, "kick"))
    		EhUtil.sendMessage(sender, "/kick [player] <razao>  - &7Kicka o Player");
    	
    	if(EhUtil.hasPermission(sender, "fban"))
    		EhUtil.sendMessage(sender, "/fban <razao>  - &7Envia uma Mensagem Fake de Ban");
    	
        if(EhUtil.hasPermission(sender, "reload"))
        	EhUtil.sendMessage(sender, "/ehban reload  - &7Recarrega as configuraçoes");
        
        EhUtil.sendMessage(sender, "/ehban versao - &7Mostra a versao do Plugin");
	}
	
   
    
    /*
     *  Comando Recarregar Configurações
     */
    public void onReloadCommand(CommandSender sender, String[] args) 
    {
    	plugin.reloadConfiguration();

    	EhUtil.sendMessage(sender, plugin.plugin_refix + "&4As configuraçoes foram &2Recarregadas");
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
        EhBansManager.removeBan(sender, args[0], args);
    }
    
    
    private void onKickCommand(CommandSender sender, String[] args) 
    {
    	Player player = plugin.getServer().getPlayer(args[0]);
    	
    	if(player != null)
    	{ 
    		args[0] = "";
    		
    		String playerName 	= player.getName();
    		String razao 		= EhUtil.colorize("&cRazao: &e" + EhBansManager.arrayToString(args).trim());
    		
    		if(args.length == 1)
    			razao = "Você foi Kicado do Servidor";
    		
    		player.kickPlayer(razao);
    		
    		EhUtil.broadcastMessage("");
        	EhUtil.broadcastMessage(plugin.plugin_refix + "por &c" + sender.getName());
        	EhUtil.broadcastMessage("&8-------------------------------");
    		EhUtil.broadcastMessage(playerName + " &cfoi kicado do servidor");
    		
    		if(args.length > 2)
    			EhUtil.broadcastMessage(razao);
    		
        	EhUtil.broadcastMessage("");
    	}
    	else
    	{
    		EhUtil.sendMessage(sender, "&cO Player nao esta Online!");
    	}    	
    }
    
    public void onVersionCommand(CommandSender sender, String[] args) 
    {
    	EhUtil.sendMessage(sender,"");
    	EhUtil.sendMessage(sender, plugin.plugin_refix + " Informacoes  &8-----------------");
    	EhUtil.sendMessage(sender, "");
    	EhUtil.sendMessage(sender, "Versao: " + plugin.pluginVersion);
    	EhUtil.sendMessage(sender, "Desenvolvido por Lucas Didur");
    	EhUtil.sendMessage(sender, "Site: http://ehaqui.com");
    	EhUtil.sendMessage(sender, "");
    }
    
    

}
