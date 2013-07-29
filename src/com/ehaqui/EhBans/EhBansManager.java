package com.ehaqui.EhBans;

import com.ehaqui.EhBans.util.EhUtil;
import com.ehaqui.EhBans.util.LogHelper;
import com.sun.istack.internal.NotNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EhBansManager {

    public static EhBans plugin;

    static String banReson = "";

    public EhBansManager(EhBans instance) {
        plugin = instance;
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
    public static boolean isIpBanned(String ip) {
        try {
            String query = String.format("SELECT * FROM `%sbans` WHERE type = 1 AND ip = '%s' AND (length = '0' OR ends > UNIX_TIMESTAMP()) AND RemoveType IS NULL",
                    plugin.mysql_prefix,
                    ip);

            LogHelper.debug(query);

            ResultSet res = plugin.db.query(query);

            if (res.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException ex) {
            LogHelper.warning(ex.getMessage());
            return false;
        }
    }


    /**
     * Checa Nick banido
     */
    public static boolean isNickBanned(String nick) {
        try {
            String query = String.format("SELECT * FROM `%sbans` WHERE type = 1 AND name = '%s' AND (length = '0' OR ends > UNIX_TIMESTAMP()) AND RemoveType IS NULL",
                    plugin.mysql_prefix,
                    nick);

            LogHelper.debug(query);

            ResultSet res = plugin.db.query(query);

            if (res.next()) {
                return true;
            } else {
                return false;
            }

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
        String srvIP = plugin.getServer().getIp();
        int srvPort = plugin.getServer().getPort();

        String query = String.format("INSERT INTO %sbanlog (sid ,time ,name ,bid) VALUES ((SELECT sid FROM %sservers WHERE ip = '%s' AND port = '%s' LIMIT 0,1), UNIX_TIMESTAMP(), '%s', (SELECT bid FROM %sbans WHERE (name = '%s' OR ip = '%s') AND RemoveType IS NULL LIMIT 0,1))",
                plugin.mysql_prefix,
                plugin.mysql_prefix,
                srvIP,
                srvPort,
                nick,
                plugin.mysql_prefix,
                nick,
                ip
        );

        LogHelper.debug(query);

        plugin.db.update(query);
    }


    /**
     * Adiciona o ban
     * <p/>
     * /ban Player Tempo Razao
     *
     * @param admin
     * @param args
     * @return
     */
    public static boolean addBan(@NotNull CommandSender admin, @NotNull String[] args) {
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
        banned = plugin.getServer().getPlayer(unmatchedName);
        if (banned == null)
            bannedName = unmatchedName;
        else
            bannedName = banned.getName();
        bannedIP = plugin.banIP ? banned != null ? banned.getAddress().toString().substring(1).split(":")[0] : "" : "";


        LogHelper.info("Parsing ban");
        time = (args.length > 1) ? getTime(args[1]) : 3600;
        args[0] = "";
        if (time != 3600)
            args[1] = "";

        reason = banReson = parseReason(args);

        adminName = admin.getName();

        if (admin instanceof Player) {
            Player adm = (Player) admin;
            adminIP = adm.getAddress().toString().substring(1).split(":")[0];
        } else
            adminIP = "";

        serverIP = plugin.getServer().getIp();
        serverPort = plugin.getServer().getPort();

        PreparedStatement pst = null;

        try {
            String query = String.format("INSERT INTO %sbans (ip, authid, name, created, ends, length, reason, aid, adminIp, sid, country , type) VALUES ('%s', '%s', '%s', UNIX_TIMESTAMP(), UNIX_TIMESTAMP() + %d, %d, '%s', IFNULL((SELECT aid FROM %sadmins WHERE user = '%s'),'0'), '%s', (SELECT sid FROM %sservers WHERE ip = '%s' AND port = '%s' LIMIT 0,1), ' ', '1')",
                    plugin.mysql_prefix, bannedIP, bannedName, bannedName, time, time, reason, plugin.mysql_prefix, adminName, adminIP, plugin.mysql_prefix, serverIP, serverPort);

            LogHelper.warning(query);

            Statement st = plugin.db.getConnection().createStatement();
            st.executeUpdate(query);

            if (banned != null) {
                plugin.lastBanReason = "You have been banned reason:" + banReson;
                banned.kickPlayer(plugin.lastBanReason);
            }
            EhUtil.broadcastMessage("");
            EhUtil.broadcastMessage("%s by %s", plugin.pluginPrefix, "&c" + adminName);
            EhUtil.broadcastMessage("&8-------------------------------");
            EhUtil.broadcastMessage("&cPlayer: &e" + bannedName);
            EhUtil.broadcastMessage("&cReason: &e" + reason);
            EhUtil.broadcastMessage("&cTime: &e" + getDate(time));
            EhUtil.broadcastMessage("");


        } catch (SQLException e) {
            if (e.getErrorCode() == 1048) {
                LogHelper.warning("ERROR: Server not registered in SourceBans");
                LogHelper.warning("IP: %s, port: %d ", serverIP, serverPort);
                LogHelper.warning("Is this information correct?");

                EhUtil.sendMessage(admin, "&cERROR: Server not registered in SourceBans", true);
            } else {
                LogHelper.warning("Error: Unexpected SQL error: code %s", e.getErrorCode());
                e.printStackTrace();
            }
        }
        finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    LogHelper.warning("Error: Unexpected SQL error: code %s", e.getErrorCode());
                }
            }
        }

        return false;

    }

    public static int getTime(String duration) {
        // Ban 0
        int time = 0;
        // Minutes
        if (duration.contains("m")) {
            time = Integer.parseInt(duration.replaceAll("mi", "")) * 60;
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
            time = Integer.parseInt(duration.replaceAll("s", "")) * 60 * 60 * 24 * 7;
        }

        // Months
        else if (duration.contains("mo")) {
            time = Integer.parseInt(duration.replaceAll("m", "")) * 60 * 60 * 24 * 30;
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

        ResultSet rs = null;
        String bid = null;

        String query = "";

        String unbanReason = parseReason(args);

        try {
            query = String.format("SELECT `bid` FROM %sbans WHERE (`type` = 1 AND `name` = '%s') AND (`length` = '0' OR `ends` > UNIX_TIMESTAMP()) AND `RemoveType` IS NULL;",
                    plugin.mysql_prefix,
                    playerName);

            LogHelper.debug(query);

            rs = plugin.db.query(query);

            if (!rs.next()) {
                EhUtil.sendMessage(admin, "&cPlayer is not banned.");
                return;
            }

            bid = rs.getString("bid");

        } catch (SQLException ex) {
            LogHelper.warning(ex.getMessage());
        }

        query = String.format("UPDATE %sbans SET `RemovedBy` = (SELECT `aid` FROM %sadmins WHERE `user_mc` = '%s'), `RemoveType` = 'U', `RemovedOn` = UNIX_TIMESTAMP(), `ureason` = '%s' WHERE `bid` = %s;",
                plugin.mysql_prefix,
                plugin.mysql_prefix,
                admin.getName(),
                unbanReason,
                bid);

        LogHelper.debug(query);

        plugin.db.update(query);

        LogHelper.warning(playerName + " unbanned by " + admin.getName());
        EhUtil.broadcastMessage(plugin.formatBanMessage(plugin.messageUnban, admin.getName(), playerName, unbanReason));
    }


    /**
     * @param arr
     * @return
     */
    public static String parseReason(String[] arr) {
        String str = "";

        for (int i = 0; i < arr.length; i++) {
            str = str + " " + arr[i];
        }

        str = str.trim();

        if (str.isEmpty())
            str = "No reason";

        return str;
    }


    static String getDate(int seconds) {
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
     * @param input
     * @return
     */
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
