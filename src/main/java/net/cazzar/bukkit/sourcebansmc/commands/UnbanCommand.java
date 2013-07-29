package net.cazzar.bukkit.sourcebansmc.commands;

import net.cazzar.bukkit.sourcebansmc.BanManager;
import net.cazzar.bukkit.sourcebansmc.Bans;
import net.cazzar.bukkit.sourcebansmc.util.Util;
import org.bukkit.command.CommandSender;

/**
 * @Author: Cayde
 */
public class UnbanCommand extends PluginCommand {
    public UnbanCommand() {
        super("unban");
    }

    @Override
    public String getNoPermissionMessage() {
        return "&cYou do not have permission to unban";
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
        if (args.length <= 0) {
            Util.sendMessage(sender, Bans.pluginPrefix + " Unban  &8-----------------");
            Util.sendMessage(sender, "&c/unban [player] [reason]");
            Util.sendMessage(sender, "");
            return false;
        }

        BanManager.removeBan(sender, args);

        return true;
    }
}
