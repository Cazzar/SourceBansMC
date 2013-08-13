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

import net.cazzar.bukkit.sourcebansmc.commands.*;
import net.cazzar.bukkit.sourcebansmc.listener.PlayerListener;
import net.cazzar.bukkit.sourcebansmc.util.Database;
import net.cazzar.bukkit.sourcebansmc.util.Updater;
import net.cazzar.bukkit.sourcebansmc.util.Util;
import net.cazzar.bukkit.sourcebansmc.util.logging.LogHelper;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


@SuppressWarnings("UnusedDeclaration")
public class Bans extends JavaPlugin {

    public String pluginName = "";
    public String pluginVersion = "";
    public static final String pluginPrefix = ChatColor.WHITE + "[" + ChatColor.GREEN + "SourceBansMC" + ChatColor.RESET + "]";
    public final String pluginCommand = "sourcebans";
    public final String prefixPermission = "sourcebans";

    public Database db = null;

    public BanManager EhBansManager = new BanManager();
    public Util util = new Util(this);
    public LogHelper logManager = new LogHelper(this);
    public Updater updater;
    //public PluginHook    t;

    // Config
    public boolean debug = false;
    public boolean updateCheck = false;

    public boolean logBlock = true;

    // Config SourceBans
    public String mysql_host = "localhost";
    public String mysql_port = "3306";
    public String mysql_username = "root";
    public String mysql_password = "";
    public String mysql_database = "sourcebans";
    public String mysql_prefix = "sb_";

    public String messageBan = "&cYou have been banned";
    public String messageUnBan = "Player &c&l{player} &rfoi by Admin &c&l{admin}&r. Reason: {reason}";

    public String lastBanReason = "";
    public boolean banIP = true;

    public static Bans instance() {
        return instance;
    }

    private static Bans instance;


    public void onEnable() {
        //t = Translate.getPluginHook(this);

        instance = this;
        pluginName = "[" + getDescription().getName() + "] ";
        pluginVersion = getDescription().getVersion();

        loadConfiguration();

        updater = new Updater(getDescription().getName(), getDescription().getVersion());

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);


        // Register command executor.
        //getCommand("ban").setExecutor(new CommandExec(this));
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("unban").setExecutor(new UnbanCommand());
        getCommand("kick").setExecutor(new KickCommand());

        getCommand("fban").setExecutor(new FakeBanCommand());
        getCommand(pluginCommand).setExecutor(new CommandSourceBans());

        setupCheckUpdate();
    }


    public void onDisable() {
        db.close();
    }

    public void setupMySQL() {
        LogHelper.info("[MYSQL] Connecting...");

        db = new Database(mysql_host, mysql_port, mysql_database, mysql_username, mysql_password);

        db.open();

        if (!db.isClosed()) {
            LogHelper.info("[MySQL] Connected to the SourceBans database!");
        } else {
            LogHelper.warning("[MySQL] Error connecting to the SourceBans database");
            getServer().getPluginManager().disablePlugin(this);
        }

    }


    public void loadConfiguration() {
        getConfig().addDefault("debug", debug);
        getConfig().addDefault("update.check", updateCheck);

        getConfig().addDefault("log.block", logBlock);

        getConfig().addDefault("database.sourcebans.host", mysql_host);
        getConfig().addDefault("database.sourcebans.port", mysql_port);
        getConfig().addDefault("database.sourcebans.username", mysql_username);
        getConfig().addDefault("database.sourcebans.password", mysql_password);
        getConfig().addDefault("database.sourcebans.database", mysql_database);
        getConfig().addDefault("database.sourcebans.prefix", mysql_prefix);

        getConfig().addDefault("message.ban", messageBan);
        getConfig().addDefault("message.unban", messageUnBan);

        getConfig().addDefault("ban.ip", banIP);


        getConfig().options().copyDefaults(true);
        saveConfig();


        debug = getConfig().getBoolean("debug");
        updateCheck = getConfig().getBoolean("update.check");

        logBlock = getConfig().getBoolean("log.block");

        mysql_host = getConfig().getString("database.sourcebans.host");
        mysql_port = getConfig().getString("database.sourcebans.port");
        mysql_username = getConfig().getString("database.sourcebans.username");
        mysql_password = getConfig().getString("database.sourcebans.password");
        mysql_database = getConfig().getString("database.sourcebans.database");
        mysql_prefix = getConfig().getString("database.sourcebans.prefix");

        messageBan = getConfig().getString("message.ban");
        messageUnBan = getConfig().getString("message.unban");

        banIP = getConfig().getBoolean("ban.ip");

        setupMySQL();
    }

    public static String formatBanMessage(String message, String adminName, String playerName, String reason) {
        message = message.replace("{admin}", adminName);
        message = message.replace("{player}", playerName);
        message = message.replace("{reason}", reason);

        return message;
    }


    public void setupCheckUpdate() {
        if (!updateCheck) {
            return;
        }

        LogHelper.info("[Updates] Initializing updater.");

        new BukkitRunnable() {
            @Override
            public void run() {
                updater.loadLatestVersion();

                if (updater.isUpdateAvailable()) {
                    updater.printMessage();
                }
            }
        }.runTaskTimerAsynchronously(this, 20 * 3, 20 * 60 * 60 * 1);
    }
}