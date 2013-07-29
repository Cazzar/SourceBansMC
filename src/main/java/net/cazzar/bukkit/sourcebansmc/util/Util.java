/**************************************************************************************************
 * Copyright (C) 2013 cazzar                                                                      *
 *                                                                                                *
 * This program is free software: you can redistribute it and/or modify                           *
 * it under the terms of the GNU General Public License as published by                           *
 * the Free Software Foundation, either version 3 of the License, or                              *
 * (at your option) any later version.                                                            *
 *                                                                                                *
 * This program is distributed in the hope that it will be useful,                                *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                                 *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                                  *
 * GNU General Public License for more details.                                                   *
 *                                                                                                *
 * You should have received a copy of the GNU General Public License                              *
 * along with this program.  If not, see [http://www.gnu.org/licenses/].                          *
 **************************************************************************************************/

package net.cazzar.bukkit.sourcebansmc.util;

import net.cazzar.bukkit.sourcebansmc.Bans;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("SameParameterValue")
public class Util {

    private static Bans plugin;

    public Util(Bans instance) {
        plugin = instance;
    }


    /**
     * Tranforma o ID de Cores para Cores
     *
     * @param line - String nao tratada
     *
     * @return line
     */
    public static String colorize(String line) {
        String[] Colours = {"[color=black]", "[color=darkblue]", "[color=darkgreen]", "[color=darkaqua]", "[color=darkred]", "[color=darkpurple]", "[color=gold]", "[color=gray]",
                "[color=darkgray]", "[color=blue]", "[color=green]", "[color=aqua]", "[color=red]", "[color=lightpurple]", "[color=yellow]", "[color=white]", "[/color]",
                "[b]", "[s]", "[u]", "[i]", "[k]", "[r]",
                "[/b]", "[/s]", "[/u]", "[/i]", "[/k]"
        };
        ChatColor[] cCode = {ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN, ChatColor.DARK_AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY,
                ChatColor.DARK_GRAY, ChatColor.BLUE, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW, ChatColor.WHITE, ChatColor.WHITE,
                ChatColor.BOLD, ChatColor.STRIKETHROUGH, ChatColor.UNDERLINE, ChatColor.ITALIC, ChatColor.MAGIC, ChatColor.RESET,
                ChatColor.RESET, ChatColor.RESET, ChatColor.RESET, ChatColor.RESET, ChatColor.RESET
        };

        for (int x = 0; x < Colours.length; x++) {
            CharSequence cChkU;
            CharSequence cChkL;

            cChkU = Colours[x].toUpperCase();
            cChkL = Colours[x].toLowerCase();
            if (line.contains(cChkU) || line.contains(cChkL)) {
                line = line.replace(cChkU, cCode[x].toString());
                line = line.replace(cChkL, cCode[x].toString());
            }
        }

        for (ChatColor color : ChatColor.values()) {
            line = line.replaceAll(String.format("&%c", color.getChar()), color.toString());
        }

        return line;
    }


    /**
     * @param sender  Who to send it to
     * @param message the message to send
     */
    public static void sendMessage(CommandSender sender, String message) {
        String[] messages = message.split("\n");

        for (int x = 0; x < messages.length; x++) {
            if (sender instanceof Player) {
                sender.sendMessage(colorize(messages[x]));
            } else {
                messages[x] = colorize(messages[x]);

                String colorsStr = "([§|&][0-9|a-r])";
                messages[x] = messages[x].replaceAll(colorsStr, "");

                LogHelper.info(messages[x]);
            }
        }
    }

    public static void sendMessage(CommandSender sender, String message, Object... txt) {
        String[] messages = message.split("\n");

        for (int x = 0; x < messages.length; x++) {
            if (sender instanceof Player) {
                sender.sendMessage(colorize(String.format(messages[x], txt)));
            } else {
                messages[x] = colorize(messages[x]);

                String colorsStr = "([§|&][0-9|a-r])";
                messages[x] = messages[x].replaceAll(colorsStr, "");


                LogHelper.info(String.format(messages[x], txt));
            }
        }
    }


    public static void sendMessage(CommandSender sender, boolean prefix, String message) {
        String[] messages = message.split("\n");

        for (int x = 0; x < messages.length; x++) {
            if (sender instanceof Player) {
                sender.sendMessage(colorize(((prefix) ? Bans.pluginPrefix + " " : "") + messages[x]));
            } else {
                messages[x] = colorize(messages[x]);
                messages[x] = messages[x].replaceAll("([\u00a7|&][0-9|a-r])", "");

                LogHelper.info(messages[x]);
            }

        }

    }


    /**
     * @param message the message to broadcast
     */
    public static void broadcastMessage(String message) {
        String[] messages = message.split("\n");

        for (String message1 : messages) {
            plugin.getServer().broadcastMessage(colorize(message1));
        }
    }

    public static void broadcastMessage(String message, Object... txt) {
        String[] messages = message.split("\n");

        for (String message1 : messages) {
            plugin.getServer().broadcastMessage(colorize(String.format(message1, txt)));
        }
    }


    public static boolean has(Player player, String permission) {
        String prefixPermission = plugin.prefixPermission;
        permission = prefixPermission + "." + permission;

        return player.hasPermission(permission);
    }


    public static boolean has(CommandSender sender, String permission) {
        if (!(sender instanceof Player)) {
            return true;
        } else {
            Player player = (Player) sender;

            String prefixPermission = plugin.prefixPermission;
            permission = prefixPermission + "." + permission;

            return player.hasPermission(permission);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean has(CommandSender sender, String permission, boolean vip) {
        boolean result;

        if (!(sender instanceof Player)) {
            result = true;
        } else {
            Player player = (Player) sender;

            String prefixPermission = plugin.prefixPermission;
            permission = prefixPermission + "." + permission;


            result = player.hasPermission(permission);
        }

        if (!result) {
            if (!vip)
                sendMessage(sender, "&cVoce nao tem Permissao\n* Node: " + permission);
            else
                sendMessage(sender, "&6&lEh &a&lAqui &r- Desculpe, Somente &bVIP&r tem permissao para usar esse comando!");
        }

        return result;
    }


}
