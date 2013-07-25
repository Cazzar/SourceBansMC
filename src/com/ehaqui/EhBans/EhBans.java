package com.ehaqui.EhBans;

import com.ehaqui.EhBans.commands.EhCommands;
import com.ehaqui.EhBans.listener.EhPlayerListener;
import com.ehaqui.EhBans.util.EhDatabase;
import com.ehaqui.EhBans.util.EhUpdater;
import com.ehaqui.EhBans.util.EhUtil;
import com.ehaqui.EhBans.util.LogHelper;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class EhBans extends JavaPlugin {

    public static String pluginName = "";
    public static String pluginVersion = "";
    public static final String pluginPrefix = "&7[&6SourceBansMC&7]&f";
    public static final String pluginCommand = "mcsbans";
    public static final String prefixPermission = "mcsbans";

    public EhDatabase db = null;
    ;

    public EhBansManager EhBansManager = new EhBansManager(this);
    public EhUtil EhUtil = new EhUtil(this);
    public LogHelper logManager = new LogHelper(this);
    public EhUpdater updater;
    //public PluginHook    t;

    // Config
    public boolean debug = false;
    public boolean updateCheck = true;

    public boolean logBlock = true;

    // Config SourceBans
    public String mysql_host = "localhost";
    public String mysql_port = "3306";
    public String mysql_username = "root";
    public String mysql_password = "";
    public String mysql_database = "ehaqui_sourcebans";
    public String mysql_prefix = "sb_";

    public String messageBan = "&cYou have been banned";
    public String messageUnban = "Player &c&l{player} &rfoi by Admin &c&l{admin}&r. Reason: {reason}";

    public String lastBanReason = "";
    public boolean banIP = true;


    public void onEnable() {
        //t = Translate.getPluginHook(this);

        pluginName = "[" + getDescription().getName() + "] ";
        pluginVersion = getDescription().getVersion();

        loadConfiguration();

        updater = new EhUpdater(this, getDescription().getName(), getDescription().getVersion());

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EhPlayerListener(this), this);


        // Register command executor.
        getCommand("ban").setExecutor(new EhCommands(this));
        getCommand("unban").setExecutor(new EhCommands(this));
        getCommand("kick").setExecutor(new EhCommands(this));

        getCommand("fban").setExecutor(new EhCommands(this));
        getCommand(pluginCommand).setExecutor(new EhCommands(this));

        setupCheckUpdate();
    }


    public void onDisable() {
        db.close();
    }

    public void setupMySQL() {
        LogHelper.info("[MYSQL] Connecting...");

        db = new EhDatabase(mysql_host, mysql_port, mysql_database, mysql_username, mysql_password);

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
        getConfig().addDefault("message.unban", messageUnban);

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
        messageUnban = getConfig().getString("message.unban");

        banIP = getConfig().getBoolean("ban.ip");

        setupMySQL();
    }

    public String formatBanMessage(String message, String adminName, String playerName, String reason) {
        message = message.replace("{admin}", adminName);
        message = message.replace("{player}", playerName);
        message = message.replace("{reson}", reason);

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