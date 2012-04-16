package com.ehaqui.EhBans;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ehaqui.EhBans.util.EhUtil;
import com.ehaqui.EhBans.util.log;

public class EhBansManager{

	public static EhBans plugin;
	
	static String banReson = "";
    
    public EhBansManager(EhBans instance) {
        plugin = instance;
    }
	

    /**
     * Checa se o Player esta Banido
     * 
     * @param playerName - Nome do Player
     * @param playerIP   - IP do Player
     * 
     * @return true/false
     */
    public static boolean checkBan(String playerName, String playerIP) {
 	
    	if(isIpBanned(playerIP)){
    		log.info("Banido - Player Conectado com um IP Banido");
    		return true;
    	}
    	
    	if(isNickBanned(playerName)){
    		log.info("Banido - Player conectando com Nick Banido");
    		return true;
    	}
    	
    	return false;
    }
    

    /**
     * Checa IP Banido
     * 
     */
    public static boolean isIpBanned(String ip) {
        PreparedStatement pst = null;
        try {
        	String query = String.format("SELECT * FROM `%sbans` " +
        					"WHERE type = 1 AND ip = '%s' " +
        					"AND (length = '0' OR ends > UNIX_TIMESTAMP()) " +
        					"AND RemoveType IS NULL", 
        					plugin.mysql_prefix, ip);
            
        	Statement st =  plugin.sql.createStatement();
			ResultSet res = st.executeQuery(query);
        	
			if(plugin.debug){
				log.info(query);
			}
			
			if(res.next()){
				return true;
			}else{
				return false;
			}

        } catch (SQLException ex) {
        	log.aviso(ex.getMessage());
            return false;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
    
    

    /** 
     * Checa Nick banido
     * 
     */
    public static boolean isNickBanned(String nick) {
        PreparedStatement pst = null;
        try {
        	String query = String.format("SELECT * FROM `%sbans` " +
        					"WHERE type = 1 AND name = '%s' " +
        					"AND (length = '0' OR ends > UNIX_TIMESTAMP()) " +
        					"AND RemoveType IS NULL", 
        					plugin.mysql_prefix, nick);
            
        	Statement st =  plugin.sql.createStatement();
			ResultSet res = st.executeQuery(query);
        	
			if(plugin.debug){
				log.info(query);
			}
			
			if(res.next()){
				return true;
			}else{
				return false;
			}

        } catch (SQLException ex) {
        	log.aviso(ex.getMessage());
            return false;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
    
    
    
    /**
     * Adiciona Barramento ao SourceBans
     * 
     * @param nick - Nick do Player
     * @param ip   - IP do Player
     */
    public static void addBlockBan(String nick, String ip) {
        PreparedStatement pst 	= null;
        String srvIP 			= null;
        
        try{
            srvIP 	= InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
        	System.out.println("Exception caught ="+e.getMessage());
        }
        int srvPort 		= plugin.getServer().getPort();
        
        try {
        	String query = String.format("INSERT INTO %sbanlog (sid ,time ,name ,bid) " +
        					"VALUES ((SELECT sid FROM %sservers WHERE ip = '%s' AND port = '%s' LIMIT 0,1), UNIX_TIMESTAMP(), '%s', (SELECT bid FROM %sbans WHERE (name = '%s' OR ip = '%s') AND RemoveType IS NULL LIMIT 0,1))", 
        					plugin.mysql_prefix, 
        					plugin.mysql_prefix, 
        					srvIP, 
        					srvPort, 
        					nick, 
        					plugin.mysql_prefix, 
        					nick, 
        					ip
        					);
            
        	Statement st =  plugin.sql.createStatement();
        	st.executeUpdate(query);

        } catch (SQLException ex){
        	log.aviso(ex.getMessage());
        	
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
    
    
    
    /**
     * Adiciona o ban
     * 
     * @param sender
     * @param args
     * @return
     */
    public static boolean addBan(CommandSender sender, String[] args) 
    {
    	List<String> argumentos = new ArrayList<String>(Arrays.asList(args));
    	
    	String unmatchedPlayerName = argumentos.get(0); argumentos.remove(0);
    	
    	Player player = plugin.getServer().getPlayer(unmatchedPlayerName);
    
    	
    	String playerName 		= player != null ? player.getName() : unmatchedPlayerName;
        String playerIP 		= player != null ? player.getAddress().toString().substring(1).split(":")[0] : "";
    	
    	int tempo 				= (argumentos.size() >= 1) ? formatBan(argumentos.get(0)) : 0;
    	
    	if(argumentos.size() >= 1)
    			argumentos.remove(0);
    	
    	String razao 			= EhUtil.colorize(combineString(argumentos, " "));
    	banReson 				= razao;
    	
    	String admName 			= sender.getName();
    	String admIP			= "";
    	
    	if(sender instanceof Player)
    	{
    		Player adm 			= (Player) sender;
    		admIP 				= adm.getAddress().toString().substring(1).split(":")[0];
    	}
    	
        String srvIP 			= null;
        int srvPort 			= plugin.getServer().getPort();

        try{
            srvIP 	= InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
        	System.out.println("Exception caught ="+e.getMessage());
        }
    	
        PreparedStatement pst 	= null;

    	try {
    		String query = String.format("INSERT INTO %sbans " +
    				"(ip, 	authid, name, created, 			ends, 					length, reason, aid, 														adminIp, sid, 																		country , type) VALUES " +
					"('%s', '%s', 	'%s', UNIX_TIMESTAMP(), UNIX_TIMESTAMP() + %d, 	%d, 	'%s', IFNULL((SELECT aid FROM %sadmins WHERE user_mc = '%s'),'0'), '%s',	 (SELECT sid FROM %sservers WHERE ip = '%s' AND port = '%s' LIMIT 0,1), 	' '		, '1')", 
					plugin.mysql_prefix, playerIP, playerName, playerName, tempo, tempo, razao, plugin.mysql_prefix, admName, admIP, plugin.mysql_prefix, srvIP, srvPort);
            
        	Statement st =  plugin.sql.createStatement();
        	st.executeUpdate(query);
        	
        	if(plugin.debug){
				log.info(query);
			}
        	
        	if(player != null)
        	{
        		player.kickPlayer(banReson);
        		plugin.lastBanReson = banReson;
        	}

        	EhUtil.broadcastMessage("");
        	EhUtil.broadcastMessage(plugin.plugin_prefix + "por &c" + admName);
        	EhUtil.broadcastMessage("&8-------------------------------");
        	EhUtil.broadcastMessage("&cPlayer: &e" + playerName);
        	EhUtil.broadcastMessage("&cRazao: &e" + razao);
        	EhUtil.broadcastMessage("");

        	return true;

        } catch (SQLException ex) {
        	log.aviso(ex.getMessage());
        	
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                }
            }
        }
    	
    	
		return false;

    }
    
    public static int formatBan(String duracao)
    {
    	// Ban 0
    	int time = 0;
    	
    	// Minutos
    	if(duracao.contains("mi")){
    		time = Integer.parseInt(duracao.replaceAll("mi", "")) * 60;
    	}
    	
    	// Horas
    	else if(duracao.contains("h")){
    		time = Integer.parseInt(duracao.replaceAll("h", "")) * 60 * 60;
    	}
    	
    	// Dias
    	else if(duracao.contains("d")){
    		time = Integer.parseInt(duracao.replaceAll("d", "")) * 60 * 60 * 24;
    	}
    	
    	// Dias
    	else if(duracao.contains("s")){
    		time = Integer.parseInt(duracao.replaceAll("s", "")) * 60 * 60 * 24 * 7;
    	}
    	
    	// Mes
    	else if(duracao.contains("m")){
    		time = Integer.parseInt(duracao.replaceAll("m", "")) * 60 * 60 * 24 * 30;
    	}
    	
    	log.debug("tempo: " + time);
		
    	return time;
    }
    
    
    /*
	 *  Adiciona o ban
	 */
    public static void removeBan(CommandSender sender, String playerName, String[] args) {
    	ResultSet rs 			= null;
    	String bid 				= null;
    	Statement st 			= null;
    	
    	String query 			= "";
    	String unbanReason 		= arrayToString(args).replaceFirst(playerName, "");
    	
    	try {
    		query = String.format("SELECT `bid` FROM %sbans WHERE (`type` = 1 AND `name` = '%s') AND (`length` = '0' OR `ends` > UNIX_TIMESTAMP()) AND `RemoveType` IS NULL;",
    				plugin.mysql_prefix,
    				playerName);
    		
    		st =  plugin.sql.createStatement();
    		
            rs = st.executeQuery(query);
    	    
            if(!rs.next())
            {
           	 	EhUtil.sendMessage(sender, "&cPlayer informado nao Esta Banido");
           	 	return;
            }
            
            bid = rs.getString("bid");
    	 
    	}catch (SQLException ex) {
        	if (ex.getErrorCode() != 1062) {   //Duplicate nick
        		log.aviso(ex.getMessage());
        	}
    	}
            
    	try {
	    	query = String.format("UPDATE %sbans SET `RemovedBy` = (SELECT `aid` FROM %sadmins WHERE `user_mc` = '%s'), `RemoveType` = 'U', `RemovedOn` = UNIX_TIMESTAMP(), `ureason` = '%s' WHERE `bid` = %s;",
	    			plugin.mysql_prefix,
	    			plugin.mysql_prefix,
	    			sender.getName(),
	    			unbanReason,
	    			bid);
	    	 
	    	st =  plugin.sql.createStatement();
	    	
	    	st.executeUpdate(query);
            
    	}
    	catch (SQLException exs) 
    	{
    		if (exs.getErrorCode() != 1062) {   //Duplicate nick
    			log.aviso(exs.getMessage());
    		}
    	}
    	finally
    	{
    		if (st != null) 
    		{
    			try 
    			{
    				st.close();
    			} catch (SQLException exs){}
    		}
    	}
     	
    	log.aviso(playerName + " Desbanido por " + sender.getName());
    	
    	EhUtil.broadcastMessage(plugin.formatBanMessage(plugin.messageUnban, sender.getName(), playerName, banReson));
    	
    }
    
    
    
    /**
     * Adiciona IP do Player ao Historico de IPs
     * 
     * @param nick
     * @param ip
     */
	public static void addIpToHistory(String nick, String ip) 
	{
        PreparedStatement pst = null;
        
        try {
            pst = plugin.sql_mc.prepareStatement("INSERT INTO `mc_history_ip` (player, ip) VALUES("
                    + "?,"
                    + "?"
                    + ");");
            
            pst.setString(1, nick);
            pst.setString(2, ip);
            
            pst.executeUpdate();
            
       } catch (SQLException ex) {
            if (ex.getErrorCode() != 1062) {   //Duplicate nick
	           	log.aviso(ex.getMessage());
            }
       } finally {
    	   if (pst != null) {
    		   try {
    			   pst.close();
    		   } catch (SQLException ex) {
    			   
    		   }
    	   }
       }
    }
	
	
	
	/**
	 * 
	 * @param arguments
	 * @param seperator
	 * @return
	 */
	protected static String combineString(final List<String> arguments, final String seperator) 
	{
    	final StringBuilder reason = new StringBuilder();
    	
        try {
        	for (final String argument : arguments) 
        	{
        		reason.append(argument);
        		reason.append(seperator);
        	}
        	
        	reason.deleteCharAt(reason.length() - seperator.length());
        	
        	return reason.toString();
        	
        } catch (final StringIndexOutOfBoundsException e) {
        	return "Sem Razao Especificada";
        }
	}
    
	
	
	/**
	 * 
	 * @param stringarray
	 * @return
	 */
    public static String arrayToString(String[] stringarray)
    {
    	String str = "";
    	
    	for (int i = 0; i < stringarray.length; i++) 
    	{
    		str = str + " " + stringarray[i];
    	}
    	return str;
    }
    
}
