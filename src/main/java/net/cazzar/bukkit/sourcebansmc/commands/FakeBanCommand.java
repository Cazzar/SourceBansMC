package net.cazzar.bukkit.sourcebansmc.commands;

import net.cazzar.bukkit.sourcebansmc.BanManager;
import net.cazzar.bukkit.sourcebansmc.Bans;
import net.cazzar.bukkit.sourcebansmc.util.Util;
import org.bukkit.command.CommandSender;

/**
 * @Author: Cayde
 */
public class FakeBanCommand extends PluginCommand {
    public FakeBanCommand() {
        super("fban");
    }

    @Override
    public String getNoPermissionMessage() {
        return "";
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
        if (args.length == 0) {
            Util.broadcastMessage("");
            Util.broadcastMessage("%s by %s", Bans.pluginPrefix, sender.getName());
            Util.broadcastMessage("&8-------------------------------");
            Util.broadcastMessage("&cName: &eN/A");
            Util.broadcastMessage("&cReason: &e%s", Bans.instance().messageBan);
            Util.broadcastMessage("");
        } else {
            Util.broadcastMessage("");
            Util.broadcastMessage("%s by %s", Bans.pluginPrefix, sender.getName());
            Util.broadcastMessage("&8-------------------------------");
            Util.broadcastMessage("&cName: &eN/A");
            Util.broadcastMessage("&cReaon: &e%s", BanManager.parseReason(args));
            Util.broadcastMessage("");
        }
        return true;
    }
}
