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

package net.cazzar.bukkit.sourcebansmc;

import net.cazzar.bukkit.sourcebansmc.util.Util;
import net.cazzar.bukkit.sourcebansmc.util.logging.LogHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BanManager {
    public BanManager() {
    }

    /**
     * Checa se o Player esta Banido
     *
     * @param playerName - Nome do Player
     * @param playerIP   - IP do Player
     * @return true/false
     */
    public static boolean checkBan(String playerName, String playerIP) {

        if (isIpBanned(playerIP)) {
            LogHelper.info("%s has been IP banned", playerName);
            return true;
        }

        if (isNickBanned(playerName)) {
            LogHelper.info("%s has been name banned", playerName);
            return true;
        }

        return false;
    }


    /**
     * Check an IP ban
     *
     * @param ip the IP to check
     */
    private static boolean isIpBanned(String ip) {
        try {
            String query = String.format("SELECT * FROM `%sbans` WHERE type = 1 AND ip = '%s' AND (length = '0' OR ends > UNIX_TIMESTAMP()) AND RemoveType IS NULL",
                    Bans.instance().mysql_prefix,
                    ip);

            LogHelper.debug(query);

            ResultSet res = Bans.instance().db.query(query);

            return res.next();

        } catch (SQLException ex) {
            LogHelper.warning(ex.getMessage());
            return false;
        }
    }


    /**
     * Checa Nick banido
     */
    private static boolean isNickBanned(String nick) {
        try {
            String query = String.format("SELECT * FROM `%sbans` WHERE type = 1 AND name = '%s' AND (length = '0' OR ends > UNIX_TIMESTAMP()) AND RemoveType IS NULL",
                    Bans.instance().mysql_prefix,
                    nick);

            LogHelper.debug(query);

            ResultSet res = Bans.instance().db.query(query);

            return res.next();

        } catch (SQLException ex) {
            LogHelper.warning(ex.getMessage());
            return false;
        }
    }


    /**
     * Adiciona Barramento ao SourceBans
     *
     * @param nick - Nick do Player
     * @param ip   - IP do Player
     */
    public static void addBlockBan(String nick, String ip) {
        String srvIP = Bans.instance().getServer().getIp();
        int srvPort = Bans.instance().getServer().getPort();

        String query = String.format("INSERT INTO %sbanlog (sid ,time ,name ,bid) VALUES ((SELECT sid FROM %sservers WHERE ip = '%s' AND port = '%s' LIMIT 0,1), UNIX_TIMESTAMP(), '%s', (SELECT bid FROM %sbans WHERE (name = '%s' OR ip = '%s') AND RemoveType IS NULL LIMIT 0,1))",
                Bans.instance().mysql_prefix,
                Bans.instance().mysql_prefix,
                srvIP,
                srvPort,
                nick,
                Bans.instance().mysql_prefix,
                nick,
                ip
        );

        LogHelper.debug(query);

        Bans.instance().db.update(query);
    }


    /**
     * Add a ban
     *
     * @param admin the admin who banned
     * @param args  the name, time and reason to ban
     * @return true if successful, otherwise false
     */
    @SuppressWarnings("ConstantConditions")
    public static boolean addBan(CommandSender admin, String[] args) {
        Player banned;
        String bannedIP;
        int time;
        String bannedName;
        String reason;
        String adminName;
        String adminIP;
        String serverIP;
        int serverPort;

        LogHelper.info("ban args length is %s", args.length);

        if (args.length == 0) {
            return false;
        }

        LogHelper.log.info("Variables init");
        String unmatchedName = args[0];
        banned = Bans.instance().getServer().getPlayer(unmatchedName);
        if (banned == null)
            bannedName = unmatchedName;
        else
            bannedName = banned.getName();
        bannedIP = Bans.instance().banIP ? banned != null ? banned.getAddress().toString().substring(1).split(":")[0] : "" : "";


        LogHelper.info("Parsing ban");
        time = (args.length > 1) ? getTime(args[1]) : 3600;
        args[0] = "";
        if (time != 3600)
            args[1] = "";

        reason = parseReason(args);

        adminName = admin.getName();

        if (admin instanceof Player) {
            Player adm = (Player) admin;
            adminIP = adm.getAddress().toString().substring(1).split(":")[0];
        } else
            adminIP = "";

        serverIP = Bans.instance().getServer().getIp();
        serverPort = Bans.instance().getServer().getPort();

        PreparedStatement pst = null;

        try {
            String query = String.format("INSERT INTO %sbans (ip, authid, name, created, ends, length, reason, aid, adminIp, sid, country , type) VALUES ('%s', '%s', '%s', UNIX_TIMESTAMP(), UNIX_TIMESTAMP() + %d, %d, '%s', IFNULL((SELECT aid FROM %sadmins WHERE user = '%s'),'0'), '%s', (SELECT sid FROM %sservers WHERE ip = '%s' AND port = '%s' LIMIT 0,1), ' ', '1')",
                    Bans.instance().mysql_prefix, bannedIP, bannedName, bannedName, time, time, reason, Bans.instance().mysql_prefix, adminName, adminIP, Bans.instance().mysql_prefix, serverIP, serverPort);

            LogHelper.warning(query);

            Statement st = Bans.instance().db.getConnection().createStatement();
            st.executeUpdate(query);

            if (banned != null) {
                Bans.instance().lastBanReason = "You have been banned reason:" + reason;
                banned.kickPlayer(Bans.instance().lastBanReason);
            }
            Util.broadcastMessage("");
            Util.broadcastMessage("%s by %s", Bans.pluginPrefix, "&c" + adminName);
            Util.broadcastMessage("&8-------------------------------");
            Util.broadcastMessage("&cPlayer: &e" + bannedName);
            Util.broadcastMessage("&cReason: &e" + reason);
            Util.broadcastMessage("&cTime: &e" + getDate(time));
            Util.broadcastMessage("");


        } catch (SQLException e) {
            if (e.getErrorCode() == 1048) {
                LogHelper.warning("ERROR: Server not registered in SourceBans");
                LogHelper.warning("IP: %s, port: %d ", serverIP, serverPort);
                LogHelper.warning("Is this information correct?");

                Util.sendMessage(admin, "&cERROR: Server not registered in SourceBans", true);
            } else {
                LogHelper.warning("Error: Unexpected SQL error: code %s", e.getErrorCode());
                e.printStackTrace();
            }
            //return false;

        }// finally {
        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                LogHelper.warning("Error: Unexpected SQL error: code %s", e.getErrorCode());
            }
        }
        return true;
        //}
    }

    private static int getTime(String duration) {
        // Ban 0
        int time = 0;
        // Minutes
        if (duration.contains("m")) {
            time = Integer.parseInt(duration.replaceAll("m", "")) * 60;
        }

        // Hours
        else if (duration.contains("h")) {
            time = Integer.parseInt(duration.replaceAll("h", "")) * 60 * 60;
        }

        // Days
        else if (duration.contains("d")) {
            time = Integer.parseInt(duration.replaceAll("d", "")) * 60 * 60 * 24;
        }

        // Weeks
        else if (duration.contains("w")) {
            time = Integer.parseInt(duration.replaceAll("w", "")) * 60 * 60 * 24 * 7;
        }

        // Months
        else if (duration.contains("mo")) {
            time = Integer.parseInt(duration.replaceAll("mo", "")) * 60 * 60 * 24 * 30;
        }

        // seconds
        else if (isInteger(duration)) {
            time = Integer.valueOf(duration);
        }

        return time;
    }


    /*
     *  Adiciona o ban
	 */
    public static void removeBan(CommandSender admin, String[] args) {
        String playerName = args[0];
        args[0] = "";

        ResultSet rs;
        String bid = null;

        String query;

        String unbanReason = parseReason(args);

        try {
            query = String.format("SELECT `bid` FROM %sbans WHERE (`type` = 1 AND `name` = '%s') AND (`length` = '0' OR `ends` > UNIX_TIMESTAMP()) AND `RemoveType` IS NULL;",
                    Bans.instance().mysql_prefix,
                    playerName);

            LogHelper.debug(query);

            rs = Bans.instance().db.query(query);

            if (!rs.next()) {
                Util.sendMessage(admin, "&cPlayer is not banned.");
                return;
            }

            bid = rs.getString("bid");

        } catch (SQLException ex) {
            LogHelper.warning(ex.getMessage());
        }

        query = String.format("UPDATE %sbans SET `RemovedBy` = (SELECT `aid` FROM %sadmins WHERE `user` = '%s'), `RemoveType` = 'U', `RemovedOn` = UNIX_TIMESTAMP(), `ureason` = '%s' WHERE `bid` = %s;",
                Bans.instance().mysql_prefix,
                Bans.instance().mysql_prefix,
                admin.getName(),
                unbanReason,
                bid);

        LogHelper.debug(query);

        Bans.instance().db.update(query);

        LogHelper.warning(playerName + " unbanned by " + admin.getName());
        Util.broadcastMessage(Bans.formatBanMessage(Bans.instance().messageUnBan, admin.getName(), playerName, unbanReason));
    }


    /**
     * @param arr the array
     * @return the parsed reason
     */
    public static String parseReason(String... arr) {
        String str = "";

        for (String anArr : arr) {
            str = str + " " + anArr;
        }

        str = str.trim();

        if (str.isEmpty())
            str = "No reason";

        return str;
    }


    private static String getDate(int seconds) {
        String date;

        if (seconds > 0) {
            long milliSeconds = seconds * 1000;
            long now = System.currentTimeMillis();

            long time = now + milliSeconds;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date resultDate = new Date(time);

            date = "at: " + sdf.format(resultDate);
        } else {
            date = "Permanent";
        }

        return date;

    }


    /**
     * Verify int input as string.
     *
     * @param input a string to try and parse as an int
     * @return if it is an int
     */
    private static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
