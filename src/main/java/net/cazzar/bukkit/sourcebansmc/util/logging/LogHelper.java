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

package net.cazzar.bukkit.sourcebansmc.util.logging;

import net.cazzar.bukkit.sourcebansmc.Bans;

import java.util.logging.Logger;

public class LogHelper {

    private static Bans plugin;
    public static Logger log;

    static {
        log = Logger.getLogger(Bans.pluginPrefix);
        log.setParent(Logger.getLogger("Minecraft"));
    }

    public LogHelper(Bans instance) {
        plugin = instance;
    }

    /**
     * Log Info
     */
    public static void info(String text, Object... txt) {
        log.info(String.format(text, txt));
    }


    /**
     * Log Warning
     */
    public static void warning(String text, Object... txt) {
        log.warning(String.format(text, txt));
    }

    /**
     * Log Severe
     */
    public static void severe(String text, Object... txt) {
        log.severe(String.format(text, txt));
    }

    /**
     * Log Debug
     */
    public static void debug(String text) {
        if (plugin.debug) {
            log.info("DEBUG " + text);
        }
    }
}
