/*
 * {one line to give the program's name and a brief idea of what it does
 * Copyright (C) 2013 cazzar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/].
 */

package net.cazzar.bukkit.EhBans.commands;

import net.cazzar.bukkit.EhBans.EhBans;
import net.cazzar.bukkit.EhBans.EhBansManager;
import net.cazzar.bukkit.EhBans.util.EhUtil;
import net.cazzar.bukkit.EhBans.util.LogHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EhCommands implements CommandExecutor {

    private static EhBans plugin;

    public EhCommands(EhBans instance) {
        plugin = instance;
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean success = false;

        if (command.getName().equalsIgnoreCase("ban")) {

            if (!EhUtil.has(sender, "ban")) {
                EhUtil.sendMessage(sender, "&cYou do not have permission to Ban");
                return true;
            }
            onBanCommand(sender, args);
            success = true;

        } else {
            if (command.getName().equalsIgnoreCase("unban")) {

                if (!EhUtil.has(sender, "unban")) {
                    EhUtil.sendMessage(sender, "&cYou do not have permission to Ban");
                    return true;
                }

                if (args.length > 0) {
                    onUnbanCommand(sender, args);
                } else {
                    EhUtil.sendMessage(sender, plugin.pluginPrefix + " Unban  &8-----------------");
                    EhUtil.sendMessage(sender, "&c/unban [player] [reason]");
                    EhUtil.sendMessage(sender, "");

                }

                success = true;

            } else if (command.getName().equalsIgnoreCase("kick")) {

                if (!EhUtil.has(sender, "kick")) {
                    EhUtil.sendMessage(sender, "&cYou do not have the permission to kick");
                    return true;
                }

                if (args.length > 0) {
                    onKickCommand(sender, args);
                } else {
                    EhUtil.sendMessage(sender, plugin.pluginPrefix + " Kick  &8-----------------");
                    EhUtil.sendMessage(sender, "&c/kick [player]");
                    EhUtil.sendMessage(sender, "");

                }

                success = true;

            } else if (command.getName().equalsIgnoreCase("fban")) {

                if (!EhUtil.has(sender, "fban", false)) {
                    return true;
                }

                if (args.length == 0) {
                    EhUtil.broadcastMessage("");
                    EhUtil.broadcastMessage("%s by %s", plugin.pluginPrefix, sender.getName());
                    EhUtil.broadcastMessage("&8-------------------------------");
                    EhUtil.broadcastMessage("&cName: &eNone");
                    EhUtil.broadcastMessage("&cReason: &eI dont know!");
                    EhUtil.broadcastMessage("");
                } else {
                    EhUtil.broadcastMessage("");
                    EhUtil.broadcastMessage("%s by %s", plugin.pluginPrefix, sender.getName());
                    EhUtil.broadcastMessage("&8-------------------------------");
                    EhUtil.broadcastMessage("&cName: &eN/A");
                    EhUtil.broadcastMessage("&cReaon: &e%s", EhBansManager.parseReason(args));
                    EhUtil.broadcastMessage("");
                }

                success = true;

            } else if (command.getName().equalsIgnoreCase("ehbans") || command.getName().equalsIgnoreCase("ebans")) {
                if (args.length == 0 || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) {
                    onBanHelpCommand(sender);

                    success = true;


                    // Comando SourceBans
                } else if (args[0].equalsIgnoreCase("sb")) {

                    if (!EhUtil.has(sender, "admin", false)) {
                        success = true;
                        return success;
                    }

                    if (args[1].equalsIgnoreCase("info")) {
                        getSBInfo(sender);
                        success = true;
                    } else
                        success = false;


                    // Comando Mostrar Versao
                } else if (args[0].equalsIgnoreCase("reload")) {

                    if (!EhUtil.has(sender, "admin", false)) {
                        success = true;
                        return success;
                    }

                    onReloadCommand(sender);

                    success = true;


                    // Comando Mostrar Versao
                } else if (args[0].equalsIgnoreCase("version")) {

                    onVersionCommand(sender);

                    success = true;
                }
            }
        }


        if (!success) {
            EhUtil.sendMessage(sender, "&cInvalid Arguments! Use '/%s help' for a list of valid commands", plugin.pluginCommand);
        }

        return true;
    }


    private void getSBInfo(CommandSender sender) {

        String srvIP = plugin.getServer().getIp();
        int srvPort = plugin.getServer().getPort();

        EhUtil.sendMessage(sender, "");
        EhUtil.sendMessage(sender, true, "SourceBans Info &8-----------------");
        EhUtil.sendMessage(sender, "");
        EhUtil.sendMessage(sender, "&cIP:    &e%s", srvIP);
        EhUtil.sendMessage(sender, "&cPorta: &e%s", srvPort);
        EhUtil.sendMessage(sender, "");
    }


    /*
      * Comando Mostrar Uso do Chat
	  */
    void onBanHelpCommand(CommandSender sender) {
        EhUtil.sendMessage(sender, "");
        EhUtil.sendMessage(sender, plugin.pluginPrefix + "Command Help &8-----------------");
        EhUtil.sendMessage(sender, "");

        if (EhUtil.has(sender, "ban")) {
            EhUtil.sendMessage(sender, "/ban [player] [time] [reason]  - &7Ban player");
            EhUtil.sendMessage(sender, "Time: 1 = 1 second, 1m = 1 minute, 1h = 1 hour, 1d = 1 day, 1w = 1 week, 1mo = 1 month");
        }

        if (EhUtil.has(sender, "unban"))
            EhUtil.sendMessage(sender, "/unban [player] [reason]  - &7Unban player");

        if (EhUtil.has(sender, "kick"))
            EhUtil.sendMessage(sender, "/kick [player] <reason>  - &7Kick player");

        if (EhUtil.has(sender, "fban"))
            EhUtil.sendMessage(sender, "/fban <reaon>  - &7Fake banning player [TEST]");

        if (EhUtil.has(sender, "reload"))
            EhUtil.sendMessage(sender, "/ehban reload  - &7Reload the config file.");

        EhUtil.sendMessage(sender, "/ehban version - &7Get the version.");
    }


    /*
     *  Reload config command
     */
    void onReloadCommand(CommandSender sender) {
        plugin.loadConfiguration();

        EhUtil.sendMessage(sender, plugin.pluginPrefix + "&4Reloaded!");
    }


    /**
     * Ban a player
     *
     * @param sender the sender
     * @param args   The name and other details of the ban
     */
    void onBanCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            EhUtil.sendMessage(sender, "/ban [player] [time] [reason]  - &7Ban player");
        }
        LogHelper.info("Ban Command used!");
        EhBansManager.addBan(sender, args);
    }


    /*
     *  Desbane o Player
     */
    void onUnbanCommand(CommandSender sender, String[] args) {
        EhBansManager.removeBan(sender, args);
    }


    private void onKickCommand(CommandSender sender, String[] args) {
        Player player = plugin.getServer().getPlayer(args[0]);

        if (player != null) {
            args[0] = "";

            String playerName = player.getName();
            String reason = EhUtil.colorize("&cReason: &e" + EhBansManager.parseReason(args));

            if (args.length == 1)
                reason = "You have been kicked from the server!";

            player.kickPlayer(reason);

            EhUtil.broadcastMessage("");
            EhUtil.broadcastMessage("%s by %s", plugin.pluginPrefix, sender.getName());
            EhUtil.broadcastMessage("&8-------------------------------");
            EhUtil.broadcastMessage("&c%s was kicked from the server", playerName);

            if (args.length > 2)
                EhUtil.broadcastMessage(reason);

            EhUtil.broadcastMessage("");
        } else {
            EhUtil.sendMessage(sender, "&cThe player is offline!");
        }
    }

    void onVersionCommand(CommandSender sender) {
        EhUtil.sendMessage(sender, "");
        EhUtil.sendMessage(sender, plugin.pluginPrefix + " Informacoes  &8-----------------");
        EhUtil.sendMessage(sender, "");
        EhUtil.sendMessage(sender, "Version: " + plugin.pluginVersion);
        //EhUtil.sendMessage(sender, "Desenvolvido por Lucas Didur");
        //EhUtil.sendMessage(sender, "Site: http://ehaqui.com");
        EhUtil.sendMessage(sender, "");
    }


}
