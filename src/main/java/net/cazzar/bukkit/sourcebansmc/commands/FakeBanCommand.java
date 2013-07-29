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

import net.cazzar.bukkit.sourcebansmc.BanManager;
import net.cazzar.bukkit.sourcebansmc.Bans;
import net.cazzar.bukkit.sourcebansmc.util.Util;
import org.bukkit.command.CommandSender;

/**
 * @Author: Cayde
 */
public class FakeBanCommand extends PluginCommand {
    public FakeBanCommand() {
        super("fban");
    }

    @Override
    public String getNoPermissionMessage() {
        return "";
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
        if (args.length == 0) {
            Util.broadcastMessage("");
            Util.broadcastMessage("%s by %s", Bans.pluginPrefix, sender.getName());
            Util.broadcastMessage("&8-------------------------------");
            Util.broadcastMessage("&cName: &eN/A");
            Util.broadcastMessage("&cReason: &e%s", Bans.instance().messageBan);
            Util.broadcastMessage("");
        } else {
            Util.broadcastMessage("");
            Util.broadcastMessage("%s by %s", Bans.pluginPrefix, sender.getName());
            Util.broadcastMessage("&8-------------------------------");
            Util.broadcastMessage("&cName: &eN/A");
            Util.broadcastMessage("&cReaon: &e%s", BanManager.parseReason(args));
            Util.broadcastMessage("");
        }
        return true;
    }
}
