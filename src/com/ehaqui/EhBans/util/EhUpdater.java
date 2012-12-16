package com.ehaqui.EhBans.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.entity.Player;

import com.ehaqui.EhBans.EhBans;

public class EhUpdater 
{
    private EhBans plugin;
    private String plName;
    private String currVer;
    private String latestVer;
    private String priority;

    private static final String VERSION_FILE = "http://plugins.ehaqui.com/bukkit/";
    
    public EhUpdater(EhBans instance, String plName, String currVer) 
    {
        this.plName     = plName;
        this.currVer    = currVer;
        this.plugin     = instance;
    }


    public void loadLatestVersion()
    {
        BufferedReader reader = null;

        try {
            String urlUpdate = VERSION_FILE + plName.toLowerCase() + ".txt";
                        
            URL url = new URL(urlUpdate);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String str = reader.readLine();
            String[] split = str.split("\\|");
            
            latestVer   = split[0];
            priority    = getPriority(Integer.parseInt(split[1]));
            
        } catch (IOException e) {
            log.aviso("Could not check for newer version!");
            latestVer = null;
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {}
        }
    }

    public static String getPriority(int level) 
    {
    	switch (level)
    	{
    		case 0:
    			return "BAIXO";
    		case 1:
    			return "NORMAL";
    		case 2:
    			return "MEDIO";
    		case 3:
    			return "ALTA";
    			
    	}
    	
    	return "NORMAL";
    }
    
    public boolean isUpdateAvailable()
    {
        if (latestVer == null)
            return false;

        return compareVer(latestVer, currVer) > 0;
    }

    private int compareVer(String str1, String str2) 
    {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;

        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i]))
            i++;

        if (i < vals1.length && i < vals2.length) 
        {
            int diff = new Integer(vals1[i]).compareTo(new Integer(vals2[i]));
            return diff < 0 ? -1 : diff == 0 ? 0 : 1;
        }

        return vals1.length < vals2.length ? -1 : vals1.length == vals2.length ? 0 : 1;
    }

    
    public void printMessage() 
    {
    	log.aviso("");
        log.aviso("-------- EhAqui Updater --------");
        log.aviso(plugin.t.s("UPDATE_1"), plName);
        log.aviso(plugin.t.s("UPDATE_2"), latestVer);
        log.aviso("");
        log.aviso(plugin.t.s("UPDATE_3"), priority);
        log.aviso(plugin.t.s("UPDATE_4") +" http://dev.bukkit.org/server-mods/" + plName.toLowerCase() + "/");
        log.aviso("-------------------------------");
        log.aviso("");
    }
    
    
    public void printMessageLitle(Player player) 
    {
        EhUtil.sendMessage(player, "");
        EhUtil.sendMessage(player, String.format("&c" + plugin.t.s("UPDATE_PLAYER"), plName, priority));
    }
}