package com.ehaqui.EhBans;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    		log.info(plugin.t.s("BAN_BANNED_IP"));
    		return true;
    	}
    	
    	if(isNickBanned(playerName)){
    		log.info(plugin.t.s("BAN_BANNED_NICK"));
    		return true;
    	}
    	
    	return false;
    }
    

    /**
     * Checa IP Banido
     * 
     */
    public static boolean isIpBanned(String ip) {
        try {
        	String query = String.format("SELECT * FROM `%sbans` WHERE type = 1 AND ip = '%s' AND (length = '0' OR ends > UNIX_TIMESTAMP()) AND RemoveType IS NULL", 
        					plugin.mysql_prefix, 
        					ip);
        	
        	log.debug(query);
        	
			ResultSet res = plugin.db.query(query);

			if(res.next()){
				return true;
			}else{
				return false;
			}

        } catch (SQLException ex) {
        	log.aviso(ex.getMessage());
            return false;
        }
    }
    
    

    /** 
     * Checa Nick banido
     * 
     */
    public static boolean isNickBanned(String nick) {
        try {
        	String query = String.format("SELECT * FROM `%sbans` WHERE type = 1 AND name = '%s' AND (length = '0' OR ends > UNIX_TIMESTAMP()) AND RemoveType IS NULL", 
        					plugin.mysql_prefix, 
        					nick);
        	
        	log.debug(query);
        	
			ResultSet res = plugin.db.query(query);
			
			if(res.next()){
				return true;
			}else{
				return false;
			}

        } catch (SQLException ex) {
        	log.aviso(ex.getMessage());
            return false;
        }
    }
    
    
    
    /**
     * Adiciona Barramento ao SourceBans
     * 
     * @param nick - Nick do Player
     * @param ip   - IP do Player
     */
    public static void addBlockBan(String nick, String ip) 
    {
        String srvIP 	= plugin.getServer().getIp();
        int srvPort 	= plugin.getServer().getPort();
        
        String query = String.format("INSERT INTO %sbanlog (sid ,time ,name ,bid) VALUES ((SELECT sid FROM %sservers WHERE ip = '%s' AND port = '%s' LIMIT 0,1), UNIX_TIMESTAMP(), '%s', (SELECT bid FROM %sbans WHERE (name = '%s' OR ip = '%s') AND RemoveType IS NULL LIMIT 0,1))", 
        				plugin.mysql_prefix, 
        				plugin.mysql_prefix, 
        				srvIP, 
        				srvPort, 
        				nick, 
        				plugin.mysql_prefix, 
        				nick, 
        				ip
        				);
        
        log.debug(query);
        	
        plugin.db.update(query);
    }
    
    
    
    /**
     * Adiciona o ban
     * 
     * /ban Player Tempo Razao
     * 
     * @param sender
     * @param args
     * @return
     */
    public static boolean addBan(CommandSender admin, String[] args) 
    {
        Player player;
        String playerName;
        String playerIP;
        
        int tempo;
        String razao;
        
        String admName;
        String admIP = "";
        
        String srvIP;
        int srvPort;
        
    	String unmatchedPlayerName = args[0];
    	player = plugin.getServer().getPlayer(unmatchedPlayerName);
    	playerName 		= player != null ? player.getName() : unmatchedPlayerName;
    	
        playerIP 		= plugin.banIP ? 
                                player != null ? 
                                        player.getAddress().toString().substring(1).split(":")[0] : 
                                        "" : 
                                "";
               
    	
    	tempo 			= (args.length > 1) ? getTime(args[1]) : 0;   
    	
    	args[0] = ""; 
    	
    	if((tempo != 0)) 
    	    args[1] = "";
    	
    	if(args.length > 1)
    	    if(args[1].equals("0"))
    	        args[1] = "";
    	            
    	        
    	
    	razao 			= banReson = parseReson(args);
    	
    	admName 		= admin.getName();
    	
    	if(admin instanceof Player)
    	{
    		Player adm 	= (Player) admin;
    		admIP 		= adm.getAddress().toString().substring(1).split(":")[0];
    	}
    	
        srvIP 			= plugin.getServer().getIp();
        srvPort 		= plugin.getServer().getPort();
    	
        PreparedStatement pst 	= null;
        
    	try {
    		String query = String.format("INSERT INTO %sbans " +
    				"(ip, 	authid, name, created, 			ends, 					length, reason, aid, 														adminIp, sid, 																		country , type) VALUES " +
					"('%s', '%s', 	'%s', UNIX_TIMESTAMP(), UNIX_TIMESTAMP() + %d, 	%d, 	'%s', IFNULL((SELECT aid FROM %sadmins WHERE user_mc = '%s'),'0'), '%s',	 (SELECT sid FROM %sservers WHERE ip = '%s' AND port = '%s' LIMIT 0,1), 	' '		, '1')", 
					plugin.mysql_prefix, playerIP, playerName, playerName, tempo, tempo, razao, plugin.mysql_prefix, admName, admIP, plugin.mysql_prefix, srvIP, srvPort);
            
    		log.debug(query);
    		
        	Statement st =  plugin.db.getConnection().createStatement();
        	st.executeUpdate(query);
        	
        	if(player != null)
        	{
        	    plugin.lastBanReson = plugin.t.s("BAN_BANNED") + " " +plugin.t.s("BAN_MESSAGE_2") + banReson;
        		player.kickPlayer(plugin.lastBanReson);
        	}

        	EhUtil.broadcastMessage("");
        	EhUtil.broadcastMessage(plugin.t.s("BAN_MESSAGE_HEADER"), plugin.pluginPrefix, "&c" + admName);
        	EhUtil.broadcastMessage("&8-------------------------------");
        	EhUtil.broadcastMessage("&c" + plugin.t.s("BAN_MESSAGE_1") + "&e" + playerName);
        	EhUtil.broadcastMessage("&c" + plugin.t.s("BAN_MESSAGE_2") + "&e" + razao);
        	EhUtil.broadcastMessage("&c" + plugin.t.s("BAN_MESSAGE_3"), "&e" + getDate(tempo));
        	EhUtil.broadcastMessage("");

        	return true;

        } catch (SQLException ex) {
            if(ex.getErrorCode() == 1048)
            {
                log.aviso(plugin.t.s("ERROR_SOURCEBANS_NOTSETUP1"));
                log.aviso(plugin.t.s("ERROR_SOURCEBANS_NOTSETUP2"), srvIP, srvPort);
                log.aviso(plugin.t.s("ERROR_SOURCEBANS_NOTSETUP3"));
                
                EhUtil.sendMessage(admin, "&c" + plugin.t.s("ERROR_SOURCEBANS_NOTSETUP1"), true);
            }
        	
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
    
    public static int getTime(String duracao)
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
    	
    	// Segundos
    	else if (isInteger(duracao))
    	{
    	    time = Integer.valueOf(duracao);
    	}
    	
    	return time;
    }
    
    
    /*
	 *  Adiciona o ban
	 */
    public static void removeBan(CommandSender admin, String[] args) 
    {
        String playerName = args[0]; args[0] = "";
        
    	ResultSet rs 			= null;
    	String bid 				= null;
    	
    	String query 			= "";
    	
    	String unbanReason 		= parseReson(args);
    	
    	try {
    		query = String.format("SELECT `bid` FROM %sbans WHERE (`type` = 1 AND `name` = '%s') AND (`length` = '0' OR `ends` > UNIX_TIMESTAMP()) AND `RemoveType` IS NULL;",
    				plugin.mysql_prefix,
    				playerName);
    		
    		log.debug(query);
    		    		
            rs = plugin.db.query(query);
    	    
            if(!rs.next())
            {
           	 	EhUtil.sendMessage(admin, "&c" + plugin.t.s("UNBAN_PLAYERNOBANNED"));
           	 	return;
            }
            
            bid = rs.getString("bid");
    	 
    	}catch (SQLException ex) {
        	log.aviso(ex.getMessage());
    	}
          
    	query = String.format("UPDATE %sbans SET `RemovedBy` = (SELECT `aid` FROM %sadmins WHERE `user_mc` = '%s'), `RemoveType` = 'U', `RemovedOn` = UNIX_TIMESTAMP(), `ureason` = '%s' WHERE `bid` = %s;",
        		plugin.mysql_prefix,
        		plugin.mysql_prefix,
        		admin.getName(),
        		unbanReason,
        		bid);
        
        log.debug(query);
        
        plugin.db.update(query);
     	
    	log.aviso(playerName + " " + plugin.t.s("UNBAN_BY") + " " + admin.getName());
    	EhUtil.broadcastMessage(plugin.formatBanMessage(plugin.messageUnban, admin.getName(), playerName, unbanReason));
    }
    
	
	
	/**
	 * 
	 * @param stringarray
	 * @return
	 */
    public static String parseReson(String[] stringarray)
    {
    	String str = "";
    	
    	for (int i = 0; i < stringarray.length; i++) 
    	{
    		str = str + " " + stringarray[i];
    	}
    	
    	str = str.trim();
    	
    	if(str.isEmpty())
    	    str = plugin.t.s("BAN_NOREASON");
    	    
    	return str;
    }
    
    
    static String getDate(int seconds)
    {
        String date;
        
        if(seconds > 0)
        {
            long milisec = seconds * 1000;
            long now = System.currentTimeMillis();
            
            long time = now + milisec;
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date resultdate = new Date(time);
            
            date = "Até " + sdf.format(resultdate);
        }
        else
        {
            date = "Permanente";
        }
        
        return date;
        
    }
    
    
    /**
     * Verifica se o Parametro é Numerico
     * 
     * @param input
     * @return
     */
    public static boolean isInteger( String input )
    {
        try
        {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e)
        {
            return false;
        }
    }
}
