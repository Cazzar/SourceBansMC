package com.ehaqui.EhBans.util;

import java.util.logging.Logger;

import com.ehaqui.EhBans.EhBans;

public class log {
	
	public static EhBans plugin;

    public log(EhBans instance) 
    {
        plugin = instance;
    }
    
    public static Logger log = Logger.getLogger("Minecraft");

    	
   	public static void info(String text){
   		log.info(plugin.pluginName + text);
   	}
   	
	public static void aviso(String text){
    	log.warning(plugin.pluginName + text);
    }
	
	public static void debug(String text){
		
		if(plugin.debug)
		{
			log.info("DEBUG " + plugin.pluginName + text);
		}
    }
    
}
