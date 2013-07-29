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

package net.cazzar.bukkit.sourcebansmc.commands;

import net.cazzar.bukkit.sourcebansmc.Bans;
import net.cazzar.bukkit.sourcebansmc.util.Util;
import org.bukkit.command.CommandSender;

import static net.cazzar.bukkit.sourcebansmc.Bans.instance;

/**
 * @Author: Cayde
 */
public class CommandSourceBans extends PluginCommand {
    public CommandSourceBans() {
        super("sourcebans");
    }

    /**
     * Executes the command, returning its success
     *
     * @param sender Source object which is executing this command
     * @param args   All arguments passed to the command, split via ' '
     *
     * @return true if the command was successful, otherwise false
     */
    @Override
    public boolean execute(CommandSender sender, String... args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) {
            Util.sendMessage(sender, "");
            Util.sendMessage(sender, Bans.pluginPrefix + "Command Help &8-----------------");
            Util.sendMessage(sender, "");

            if (Util.has(sender, "ban")) {
                Util.sendMessage(sender, "/ban [player] [time] [reason]  - &7Ban player");
                Util.sendMessage(sender, "Time: 1 = 1 second, 1m = 1 minute, 1h = 1 hour, 1d = 1 day, 1w = 1 week, 1mo = 1 month");
            }
            if (Util.has(sender, "unban"))
                Util.sendMessage(sender, "/unban [player] [reason]  - &7Unban player");
            if (Util.has(sender, "kick"))
                Util.sendMessage(sender, "/kick [player] <reason>  - &7Kick player");
            if (Util.has(sender, "fban"))
                Util.sendMessage(sender, "/fban <reaon>  - &7Fake banning player [TEST]");
            if (Util.has(sender, "reload"))
                Util.sendMessage(sender, "/sourcebans reload  - &7Reload the config file.");
            if (Util.has(sender, name))
                Util.sendMessage(sender, "/sourcebans version - &7Get the version.");

            // Comando SourceBans
        } else if (args[0].equalsIgnoreCase("sb")) {

            if (!Util.has(sender, "admin", false)) {
                return true;
            }

            if (args[1].equalsIgnoreCase("info")) {
                String srvIP = instance().getServer().getIp();
                int srvPort = instance().getServer().getPort();

                Util.sendMessage(sender, "");
                Util.sendMessage(sender, true, "SourceBans Info &8-----------------");
                Util.sendMessage(sender, "");
                Util.sendMessage(sender, "&cIP:    &e%s", srvIP);
                Util.sendMessage(sender, "&cPorta: &e%s", srvPort);
                Util.sendMessage(sender, "");
            }


            // Comando Mostrar Versao
        } else if (args[0].equalsIgnoreCase("reload")) {

            if (!Util.has(sender, "admin", false)) {
                return true;
            }

            instance().loadConfiguration();
            Util.sendMessage(sender, Bans.pluginPrefix + "&4Reloaded!");
        } else if (args[0].equalsIgnoreCase("version")) {
            Util.sendMessage(sender, "");
            Util.sendMessage(sender, Bans.pluginPrefix + " Informacoes  &8-----------------");
            Util.sendMessage(sender, "");
            Util.sendMessage(sender, "Version: " + instance().pluginVersion);
            Util.sendMessage(sender, "");
        }
        return true;
    }
}

