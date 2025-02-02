package me.MrRafter.framedupe;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandCompleter implements TabCompleter {

  private final List<String> arguments = new ArrayList<>();

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    List<String> results = new ArrayList<>();

    // Ensure the command is "framedupe"
    if (!cmd.getName().equalsIgnoreCase("framedupe")) {
      return results;
    }

    // Add "reload" only if the sender has permission
    if (arguments.isEmpty() && sender.hasPermission("framedupe.reload")) {
      arguments.add("reload");
    }

    // Only provide tab completion for the first argument
    if (args.length == 1) {
      String input = args[0].toLowerCase();
      for (String argument : arguments) {
        if (argument.startsWith(input)) {
          results.add(argument);
        }
      }
    }

    return results;
  }
}
