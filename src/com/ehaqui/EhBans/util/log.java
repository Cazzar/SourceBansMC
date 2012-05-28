package com.ehaqui.EhBans.util;

import java.util.logging.Logger;

import com.ehaqui.EhBans.EhBans;

public class log {
    
    public static EhBans plugin;
    public static Logger log = Logger.getLogger("Minecraft");
    
    public log(EhBans instance) 
    {
        plugin = instance;
    }
    
    /**
     *  Log Info
     *  
     * @param text
     */
    public static void info(String text)
    {
        log.info(plugin.pluginName + text);
    }
    
    public static void info(String text, Object... txt)
    {
        log.info(plugin.pluginName + String.format(text, txt));
    }
    
    
    /**
     * Log Aviso
     * 
     * @param text
     */
    public static void aviso(String text)
    {
        log.warning(plugin.pluginName + text);
    }
    
    public static void aviso(String text, Object... txt)
    {
        log.warning(plugin.pluginName + String.format(text, txt));
    }
    
    
    /**
     * Log Debug
     * 
     * @param text
     */
    public static void debug(String text)
    {
        if(plugin.debug)
        {
            log.info("DEBUG " + plugin.pluginName + text);
        }
    }
}
