package com.ehaqui.EhBans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.permission.Permission;

import com.ehaqui.EhBans.commands.EhCommands;
import com.ehaqui.EhBans.listener.EhPlayerListener;
import com.ehaqui.EhBans.util.EhUtil;
import com.ehaqui.EhBans.util.log;

public class EhBans extends JavaPlugin {
	    
    public Permission permission 		= null;

	public Connection sql 				= null;
	public Connection sql_mc			= null;
    
    public String plugin_prefix 	   = "&7[&6Eh &aBans&7] &f";
    
    public String pluginName 			= "";
    public  String pluginVersion 		= "";
    
    public String prefixPermission		= "ehbans";
    
	public EhBansManager EhBansManager 	= new EhBansManager(this);
	public EhUtil EhUtil 				= new EhUtil(this);
	public log logManager				= new log(this);
	
    // Configuração
    public boolean debug 				= false;
    
    	// Config SourceBans
	public String mysql_host			= "localhost";
	public String mysql_port			= "3306";
	public String mysql_username		= "root";
	public String mysql_password		= "";
	public String mysql_database 		= "ehaqui_sourcebans";
	public String mysql_prefix			= "sb_";
	
		// Config Log IP
	public String mysql_host_mc			= "localhost";
	public String mysql_port_mc			= "3306";
	public String mysql_username_mc		= "root";
	public String mysql_password_mc		= "";
	public String mysql_database_mc		= "ehaqui_minecraft";
	
	public String messageBan			= "&cVoce foi Banido";
	public String messageUnban			= "Player &c&l{player} &rfoi desbanido por &c&l{admin}";

	public String lastBanReson			= "";
	
	
	public void onEnable() 
	{
    	pluginName 			= "[" +getDescription().getName() + "] ";
        pluginVersion 		= getDescription().getVersion();
		
		loadConfiguration();
		
		// permission
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
		
    	PluginManager pm = getServer().getPluginManager();
    	
        pm.registerEvents(new EhPlayerListener(this), this);
        
        log.info( pluginName +" "+ pluginVersion +" started");
        
        // Register command executor.
        getCommand("ban").setExecutor(new EhCommands(this));
        getCommand("unban").setExecutor(new EhCommands(this));
        getCommand("kick").setExecutor(new EhCommands(this));
        
        getCommand("fban").setExecutor(new EhCommands(this));
        getCommand("ehbans").setExecutor(new EhCommands(this));
        
        // Setup MySQL
        setupMySQL();
        
    }
    


	public void onDisable() {
    	
    	if(sql != null){
    		try {
    			sql.close();
            } catch (SQLException ex) {
            	
            }
		}
    	
    	if(sql_mc != null){
    		try {
    			sql_mc.close();
            } catch (SQLException ex) {
            	
            }
		}
    	
    }

    
    public void setupMySQL() {
    	   	
		String driver 	= "com.mysql.jdbc.Driver";
		String url 		= "jdbc:mysql://"+ mysql_host +":"+ mysql_port +"/";
		String db 		= mysql_database;
		String user 	= mysql_username;
		String pass 	= mysql_password;

		try{
			Class.forName(driver).newInstance();
			sql = DriverManager.getConnection(url+db, user, pass);
			
			if(!sql.isClosed()){
				log.info("MySQL Databese SourceBans connection is established!");
			}else{
				log.aviso("MySQL Databese SourceBans connection failed.");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		
		String url_minecraft 		= "jdbc:mysql://"+ mysql_host_mc +":"+ mysql_port_mc +"/";
		String db_minecraft 		= mysql_database_mc;
		String user_minecraft 		= mysql_username_mc;
		String pass_minecraft		= mysql_password_mc;

		try{
			Class.forName(driver).newInstance();
			sql_mc = DriverManager.getConnection(url_minecraft+db_minecraft, user_minecraft, pass_minecraft);
			
			if(!sql.isClosed()){
				log.info("MySQL Databese Minecraft connection is established!");
			}else{
				log.aviso("MySQL Databese Minecraft connection failed.");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		/*
		Statement st;
		
		try {
			st = sql.createStatement();
			
			st.executeUpdate("USE "+ mysql_database_mc +"; CREATE TABLE IF NOT EXISTS `mc_history_ip` (" +
					"`ip_id` int(11) NOT NULL auto_increment, " +
					"`player_id` int(11) NOT NULL, " +
					"`ip` varchar(40) NOT NULL, " +
					"PRIMARY KEY  (`ip_id`));");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
		
    }
    
    
    
    //Handling configuration
  	public void loadConfiguration(){

  		getConfig().addDefault("debug", debug);
  		
  		getConfig().addDefault("database.sourcebans.host", mysql_host);
  		getConfig().addDefault("database.sourcebans.port", mysql_port);
  		getConfig().addDefault("database.sourcebans.username", mysql_username);
  		getConfig().addDefault("database.sourcebans.password", mysql_password);
  		getConfig().addDefault("database.sourcebans.database", mysql_database);
  		getConfig().addDefault("database.sourcebans.prefix", mysql_prefix);
  		
  		getConfig().addDefault("database.minecraft.host", mysql_host_mc);
  		getConfig().addDefault("database.minecraft.port", mysql_port_mc);
  		getConfig().addDefault("database.minecraft.username", mysql_username_mc);
  		getConfig().addDefault("database.minecraft.password", mysql_password_mc);
  		getConfig().addDefault("database.minecraft.database", mysql_database_mc);

  		getConfig().addDefault("message.ban", messageBan);
  		getConfig().addDefault("message.unban", messageUnban);
  		
  		
  		
  		getConfig().options().copyDefaults(true);
  		saveConfig();
  		
  		debug							= getConfig().getBoolean("debug");
  		
  		mysql_host						= getConfig().getString("database.sourcebans.host");
  		mysql_port						= getConfig().getString("database.sourcebans.port");
  		mysql_username					= getConfig().getString("database.sourcebans.username");
  		mysql_password					= getConfig().getString("database.sourcebans.password");
  		mysql_database					= getConfig().getString("database.sourcebans.database");
  		mysql_prefix					= getConfig().getString("database.sourcebans.prefix");
  		
  		mysql_host_mc					= getConfig().getString("database.minecraft.host");
  		mysql_port_mc					= getConfig().getString("database.minecraft.port");
  		mysql_username_mc				= getConfig().getString("database.minecraft.username");
  		mysql_password_mc				= getConfig().getString("database.minecraft.password");
  		mysql_database_mc				= getConfig().getString("database.minecraft.database");
  		
  		messageBan						= getConfig().getString("message.ban");
  		messageUnban					= getConfig().getString("message.unban");
  		
  	}
  	
  	//Handling configuration
  	public void reloadConfiguration(){
  		
  		reloadConfig();
  		
  		debug							= getConfig().getBoolean("debug");
  		
  		mysql_host						= getConfig().getString("database.sourcebans.host");
  		mysql_port						= getConfig().getString("database.sourcebans.port");
  		mysql_username					= getConfig().getString("database.sourcebans.username");
  		mysql_password					= getConfig().getString("database.sourcebans.password");
  		mysql_database					= getConfig().getString("database.sourcebans.database");
  		mysql_prefix					= getConfig().getString("database.sourcebans.prefix");
  		
  		mysql_host_mc					= getConfig().getString("database.minecraft.host");
  		mysql_port_mc					= getConfig().getString("database.minecraft.port");
  		mysql_username_mc				= getConfig().getString("database.minecraft.username");
  		mysql_password_mc				= getConfig().getString("database.minecraft.password");
  		mysql_database_mc				= getConfig().getString("database.minecraft.database");
  		
  		messageBan						= getConfig().getString("message.ban");
  		messageUnban					= getConfig().getString("message.unban");
  		
  		setupMySQL();
  	}

  	public String formatBanMessage(String message, String adminName, String playerName, String reason)
  	{
  		message = message.replace("{admin}", adminName);
  		message = message.replace("{player}", playerName);
  		message = message.replace("{reson}", reason);
  		
  		return message;
  	}
  	
  	
}