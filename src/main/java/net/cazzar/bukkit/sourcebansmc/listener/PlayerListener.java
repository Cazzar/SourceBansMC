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

package net.cazzar.bukkit.sourcebansmc.listener;

import net.cazzar.bukkit.sourcebansmc.BanManager;
import net.cazzar.bukkit.sourcebansmc.Bans;
import net.cazzar.bukkit.sourcebansmc.util.LogHelper;
import net.cazzar.bukkit.sourcebansmc.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import java.net.InetAddress;
import java.net.UnknownHostException;


@SuppressWarnings({"UnusedDeclaration", "UnusedParameters"})
public class PlayerListener implements Listener {

    private static Bans plugin;

    public PlayerListener(Bans instance) {
        plugin = instance;
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        // Nao kicar se o Player ja estiver Online
        if (player.isOnline()) {
            player.kickPlayer("Voce ja esta Online");
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Voce ja esta Online");
            return;
        }


        String playerName = event.getPlayer().getName();
        String playerIP = event.getAddress().getHostAddress();
        String playerHostname = "";

        if (playerIP.equals("")) {
            LogHelper.severe("Warning! Couldn't load " + playerName + "'s IP. Possibly plugins conflict");
            return;
        }

        if (plugin.debug) {
            try {
                playerHostname = InetAddress.getByName(playerIP).getHostName();
            } catch (UnknownHostException ignored) {
            }

            LogHelper.info(playerName + " connected. Detected ip: " + playerIP + " Detected hostname: " + playerHostname);
        }

        if (BanManager.checkBan(playerName, playerIP)) {
            BanManager.addBlockBan(playerName, playerIP);

            event.disallow(Result.KICK_OTHER, Util.colorize(plugin.messageBan));
        }
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
    }


    // Remove "x left the game" 
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if ((event.getReason()).equals(plugin.lastBanReason)) {
            event.setLeaveMessage(null);
        }
    }


}
