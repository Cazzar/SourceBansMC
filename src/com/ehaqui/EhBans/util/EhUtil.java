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


    /**
     * Tranforma o ID de Cores para Cores
     * 
     * @param line - String nao tratada
     * @return line
     */
    public static String colorize(String line){
        String[] Colours = {    "[color=black]", "[color=darkblue]", "[color=darkgreen]", "[color=darkaqua]", "[color=darkred]", "[color=darkpurple]", "[color=gold]", "[color=gray]",
                                "[color=darkgray]", "[color=blue]", "[color=green]", "[color=aqua]", "[color=red]", "[color=lightpurple]", "[color=yellow]", "[color=white]", "[/color]",
                                "[b]", "[s]", "[u]", "[i]", "[k]", "[r]",
                                "[/b]", "[/s]", "[/u]", "[/i]", "[/k]"
                              };
        ChatColor[] cCode = {   ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN, ChatColor.DARK_AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY,
                                ChatColor.DARK_GRAY, ChatColor.BLUE, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW, ChatColor.WHITE, ChatColor.WHITE,
                                ChatColor.BOLD, ChatColor.STRIKETHROUGH, ChatColor.UNDERLINE, ChatColor.ITALIC, ChatColor.MAGIC, ChatColor.RESET,
                                ChatColor.RESET, ChatColor.RESET, ChatColor.RESET, ChatColor.RESET, ChatColor.RESET
                              };
        
        for (int x = 0; x < Colours.length; x++) {
            CharSequence cChkU = null;
            CharSequence cChkL = null;

            cChkU = Colours[x].toUpperCase();
            cChkL = Colours[x].toLowerCase();
            if (line.contains(cChkU) || line.contains(cChkL)) {
                line = line.replace(cChkU, cCode[x].toString());
                line = line.replace(cChkL, cCode[x].toString());
            }
        }
        
        for (ChatColor color : ChatColor.values()) 
        {
            line = line.replaceAll(String.format("&%c", color.getChar()), color.toString());
        }
        
        return line;
    }
    
    
    /**
     * 
     * 
     * @param sender
     * @param message
     */
    public static void sendMessage(Player sender, String message)
    {
        String[] messages = message.split("\n");
                 
        for(int x = 0; x < messages.length; x++) 
        {
            sender.sendMessage(colorize(messages[x]));
        }
    }
    public static void sendMessage(Player sender, String message, Object... txt)
    {
        String[] messages = message.split("\n");
                 
        for(int x = 0; x < messages.length; x++) 
        {
            sender.sendMessage(colorize(String.format(messages[x], txt)));
        }
    }

    
    /**
     * 
     * @param sender
     * @param message
     */
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
                messages[x] = colorize(messages[x]);
                
                String colorsStr = "([§|&][0-9|a-r])";
                messages[x] = messages[x].replaceAll(colorsStr, "");
                
                log.info(messages[x]); 
            }
        }
    }
    public static void sendMessage(CommandSender sender, String message, Object... txt) 
    {
        String[] messages = message.split("\n");
         
        for(int x = 0; x < messages.length; x++) 
        {
            if(sender instanceof Player)
            {
                sender.sendMessage(colorize(String.format(messages[x], txt)));
            }
            else
            {
                messages[x] = colorize(messages[x]);
                
                String colorsStr = "([§|&][0-9|a-r])";
                messages[x] = messages[x].replaceAll(colorsStr, "");
                
                
                log.info(String.format(messages[x], txt)); 
            }
        }
    }
    
    
    /**
     * 
     * @param sender
     * @param message
     * @param prefix
     */
    public static void sendMessage(Player player, boolean prefix, String message) 
    {
        if(prefix)
        {
            String[] messages = message.split("\n");
             
            for(int x = 0; x < messages.length; x++) 
            {
                player.sendMessage(colorize(plugin.pluginPrefix + " " + messages[x]));
            }
        }
        else
        {
            String[] messages = message.split("\n");
             
            for(int x = 0; x < messages.length; x++) 
            {
                player.sendMessage(colorize(messages[x]));
            }
        }
    }
    public static void sendMessage(Player player, boolean prefix, String message, Object... txt) 
    {
        if(prefix)
        {
            String[] messages = message.split("\n");
             
            for(int x = 0; x < messages.length; x++) 
            {
                player.sendMessage(colorize(plugin.pluginPrefix + " " + String.format(messages[x], txt)));
            }
        }
        else
        {
            String[] messages = message.split("\n");
             
            for(int x = 0; x < messages.length; x++) 
            {
                player.sendMessage(colorize(String.format(messages[x], txt)));
            }
        }
    }
    
    
    public static void sendMessage(CommandSender sender, boolean prefix, String message) 
    {
        if(prefix)
        {
            String[] messages = message.split("\n");
             
            for(int x = 0; x < messages.length; x++) 
            {
                if(sender instanceof Player)
                {
                    sender.sendMessage(colorize(plugin.pluginPrefix + " " + messages[x]));
                }
                else
                {
                    messages[x] = colorize(messages[x]);
                    messages[x] = messages[x].replaceAll("([§|&][0-9|a-r])", "");
                    
                    log.info(messages[x]); 
                }
                    
            }
        }
        else
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
                    messages[x] = colorize(messages[x]);
                    messages[x] = messages[x].replaceAll("([§|&][0-9|a-r])", "");
                    
                    log.info(messages[x]); 
                }
            }
        }
    }
    
    
    /**
     * 
     * @param message
     */
    public static void broadcastMessage(String message)
    {
        String[] messages = message.split("\n");
         
        for(int x = 0; x < messages.length; x++) 
        {
            plugin.getServer().broadcastMessage(colorize(messages[x])); 
        }
    }
    public static void broadcastMessage(String message, Object... txt)
    {
        String[] messages = message.split("\n");
         
        for(int x = 0; x < messages.length; x++) 
        {
            plugin.getServer().broadcastMessage(colorize(String.format(messages[x], txt))); 
        }
    }
    
    
    public static boolean has(Player player, String permission)
    {
        String prefixPermission = plugin.prefixPermission;
        permission = prefixPermission + "." + permission;
        
        if(player.hasPermission(permission))
            return true;
        else
            return false;
    }


    public static boolean has(CommandSender sender, String permission) 
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
            
            if(player.hasPermission(permission))
                return true;
            else
                return false;
        }
    }
    
    public static boolean has(CommandSender sender, String permission, boolean vip) 
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
            
            
            if(player.hasPermission(permission))
                result = true;
            else
                result = false;
        }
        
        if(!result)
        {   
            if(!vip)
                sendMessage(sender, "&cVoce nao tem Permissao\n* Node: " + permission);
            else
                sendMessage(sender, "&6&lEh &a&lAqui &r- Desculpe, Somente &bVIP&r tem permissao para usar esse comando!");
        }
        
        return result;
    }


    
}
