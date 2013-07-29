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
import net.cazzar.bukkit.sourcebansmc.util.LogHelper;
import net.cazzar.bukkit.sourcebansmc.util.Util;
import org.bukkit.command.CommandSender;

/**
 * @Author: Cayde
 */
public class BanCommand extends PluginCommand {

    public BanCommand() {
        super("ban");
    }

    @Override
    public String getNoPermissionMessage() {
        return "&cYou do not have permission to Ban";
    }

    @Override
    public boolean execute(CommandSender sender, String... args) {
        if (args.length == 0) {
            Util.sendMessage(sender, "/ban [player] [time] [reason]  - &7Ban player");
        }
        LogHelper.info("Ban Command used!");
        return BanManager.addBan(sender, args);
    }
}
