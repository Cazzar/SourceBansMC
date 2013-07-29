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
