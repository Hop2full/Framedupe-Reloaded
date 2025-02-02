package me.MrRafter;

import me.MrRafter.framedupe.Events;
import me.MrRafter.framedupe.Metrics;
import me.MrRafter.framedupe.CommandCompleter;
import me.MrRafter.framedupe.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class FrameDupe extends JavaPlugin {

    @Override
    public void onEnable() {
        // Get the Minecraft version safely
        String versionString = Bukkit.getBukkitVersion().split("-")[0]; // Extract "1.21.4"
        int versionNumber = Integer.parseInt(versionString.split("\\.")[1]); // Get the middle number (21)

        // Register events based on version
        Events events = new Events(this);
        if (versionNumber >= 17) {
            getServer().getPluginManager().registerEvents(events.new FrameSpecific(), this);
        }
        getServer().getPluginManager().registerEvents(events.new FrameAll(), this);

        // Register commands
        if (getCommand("framedupe") != null) {
            getCommand("framedupe").setExecutor((CommandExecutor) new Commands(this));
            getCommand("framedupe").setTabCompleter((TabCompleter) new CommandCompleter());
        } else {
            getLogger().severe("Command 'framedupe' is missing from plugin.yml!");
        }

        // Metrics (bStats)
        int pluginId = 17434;
        new Metrics(this, pluginId);

        // Load config
        saveDefaultConfig();

        // Log successful load
        getLogger().info("FrameDupe by MrRafter & Jaswin loaded successfully on Minecraft " + versionString);
    }
}
