package me.MrRafter.framedupe;

import me.MrRafter.FrameDupe;
import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.GlowItemFrame;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.List;

public class Events {

    private final FrameDupe plugin;

    public Events(FrameDupe plugin) {
        this.plugin = plugin;
    }

    public class FrameAll implements Listener {

        @EventHandler
        private void onFrameBreak(EntityDamageByEntityEvent event) {
            if (!plugin.getConfig().getBoolean("FrameDupe.Enabled")) return;
            if (!(event.getEntity() instanceof ItemFrame)) return;

            ItemFrame itemFrame = (ItemFrame) event.getEntity();
            ItemStack item = itemFrame.getItem();

            if (!isValidItem(item, "FrameDupe")) return;

            int rng = (int) (Math.random() * 100);
            if (rng < plugin.getConfig().getInt("FrameDupe.Probability-percentage")) {
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), item);
            }
        }
    }

    public class FrameSpecific implements Listener {

        @EventHandler
        private void onFrameBreak(EntityDamageByEntityEvent event) {
            if (!plugin.getConfig().getBoolean("GLOW_FrameDupe.Enabled")) return;
            if (!(event.getEntity() instanceof GlowItemFrame)) return;

            GlowItemFrame itemFrame = (GlowItemFrame) event.getEntity();
            ItemStack item = itemFrame.getItem();

            if (!isValidItem(item, "GLOW_FrameDupe")) return;

            int rng = (int) (Math.random() * 100);
            if (rng < plugin.getConfig().getInt("GLOW_FrameDupe.Probability-percentage")) {
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), item);
            }
        }
    }

    /**
     * Validates whether the given item should be duplicated based on the plugin's whitelist and blacklist.
     * Also prevents blacklisted items inside shulker boxes.
     */
    private boolean isValidItem(ItemStack item, String configPath) {
        if (item == null) return false;

        // Whitelist check
        if (plugin.getConfig().getBoolean(configPath + ".Whitelist.Enabled")) {
            List<String> whitelist = plugin.getConfig().getStringList(configPath + ".Whitelist.Items");
            if (!whitelist.contains(item.getType().toString())) {
                return false;
            }
        }

        // Blacklist check
        if (plugin.getConfig().getBoolean(configPath + ".Blacklist.Enabled")) {
            List<String> blacklist = plugin.getConfig().getStringList(configPath + ".Blacklist.Items");
            if (blacklist.contains(item.getType().toString())) {
                return false;
            }
        }

        // Handle shulker boxes
        if (plugin.getConfig().getBoolean(configPath + ".Fix-Shulkers")) {
            if (item.getItemMeta() instanceof BlockStateMeta) {
                BlockStateMeta blockStateMeta = (BlockStateMeta) item.getItemMeta();
                if (blockStateMeta.getBlockState() instanceof ShulkerBox) {
                    Inventory shulkerInventory = ((ShulkerBox) blockStateMeta.getBlockState()).getInventory();

                    for (ItemStack shulkerItem : shulkerInventory.getContents()) {
                        if (shulkerItem != null) {
                            String itemTypeName = shulkerItem.getType().toString();

                            if (plugin.getConfig().getBoolean(configPath + ".Blacklist.Enabled")) {
                                List<String> blacklist = plugin.getConfig().getStringList(configPath + ".Blacklist.Items");
                                if (blacklist.contains(itemTypeName)) {
                                    return false;
                                }
                            }

                            if (plugin.getConfig().getBoolean(configPath + ".Whitelist.Enabled")) {
                                List<String> whitelist = plugin.getConfig().getStringList(configPath + ".Whitelist.Items");
                                if (!whitelist.contains(itemTypeName)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
