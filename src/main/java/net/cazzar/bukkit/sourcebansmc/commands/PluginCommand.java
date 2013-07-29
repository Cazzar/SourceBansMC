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

import net.cazzar.bukkit.sourcebansmc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @Author: Cayde
 */
public abstract class PluginCommand implements CommandExecutor {
    final String name;

    public PluginCommand(String name) {
        this.name = name;
    }

    /**
     * Executes the command, returning its success
     *
     * @param sender Source object which is executing this command
     * @param args   All arguments passed to the command, split via ' '
     *
     * @return true if the command was successful, otherwise false
     */
    public abstract boolean execute(CommandSender sender, String... args);

    public String getNoPermissionMessage() {
        return "&cYou have no permission for that!";
    }

    public boolean hasPermission(CommandSender sender) {
        return Util.has(sender, name);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!hasPermission(sender)) {
            sender.sendMessage(getNoPermissionMessage());
            return false;
        }

        return execute(sender, args);
    }
}
