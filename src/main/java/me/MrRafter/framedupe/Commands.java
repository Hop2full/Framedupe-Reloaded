package me.MrRafter.framedupe;

import me.MrRafter.FrameDupe;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

    private final FrameDupe plugin;

    public Commands(FrameDupe instance) {
        this.plugin = instance;
    }

    private static final String PREFIX = ChatColor.AQUA + "" + ChatColor.BOLD + "[FrameDupe] ";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("framedupe")) { // Use lowercase "framedupe" to match plugin.yml
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(PREFIX + ChatColor.RED + "Usage: " + ChatColor.BOLD + "/fd reload");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("framedupe.reload")) { // Match permission with plugin.yml
                sender.sendMessage(PREFIX + ChatColor.RED + "You don't have permission to run this command.");
                return true;
            }

            plugin.reloadConfig();
            sender.sendMessage(PREFIX + ChatColor.GREEN + "Config reloaded successfully!");
            return true;
        }

        // If the subcommand isn't recognized, send usage message
        sender.sendMessage(PREFIX + ChatColor.RED + "Unknown subcommand. Usage: " + ChatColor.BOLD + "/fd reload");
        return true;
    }
}
