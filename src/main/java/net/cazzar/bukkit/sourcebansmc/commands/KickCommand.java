package net.cazzar.bukkit.sourcebansmc.commands;

import net.cazzar.bukkit.sourcebansmc.BanManager;
import net.cazzar.bukkit.sourcebansmc.Bans;
import net.cazzar.bukkit.sourcebansmc.util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @Author: Cayde
 */
public class KickCommand extends PluginCommand {
    public KickCommand() {
        super("kick");
    }

    @Override
    public String getNoPermissionMessage() {
        return "&cYou do not have the permission to kick";
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
            Util.sendMessage(sender, Bans.pluginPrefix + " Kick  &8-----------------");
            Util.sendMessage(sender, "&c/kick [player]");
            Util.sendMessage(sender, "");
        }

        Player player = Bans.instance().getServer().getPlayer(args[0]);

        if (player != null) {
            args[0] = "";

            String playerName = player.getName();
            String reason = Util.colorize("&cReason: &e" + BanManager.parseReason(args));

            if (args.length == 1)
                reason = "You have been kicked from the server!";

            player.kickPlayer(reason);

            Util.broadcastMessage("");
            Util.broadcastMessage("%s by %s", Bans.pluginPrefix, sender.getName());
            Util.broadcastMessage("&8-------------------------------");
            Util.broadcastMessage("&c%s was kicked from the server", playerName);

            if (args.length > 2)
                Util.broadcastMessage(reason);

            Util.broadcastMessage("");
        } else {
            Util.sendMessage(sender, "&cThe player is offline!");
        }

        return true;
    }
}
