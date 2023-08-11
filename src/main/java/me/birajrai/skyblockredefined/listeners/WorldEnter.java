package me.birajrai.skyblockredefined.listeners;

import me.birajrai.skyblockredefined.SkyBlockRedefined;
import me.birajrai.skyblockredefined.Settings;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldEnter implements Listener {
    private final SkyBlockRedefined plugin;
    private final static boolean DEBUG = false;

    public WorldEnter(SkyBlockRedefined skyblockRedefined) {
        this.plugin = skyblockRedefined;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
    public void onWorldEnter(final PlayerChangedWorldEvent event) {
        if (DEBUG)
            plugin.getLogger().info("DEBUG " + event.getEventName());
        if (!event.getPlayer().getWorld().equals(SkyBlockRedefined.getIslandWorld()) &&
                !event.getPlayer().getWorld().equals(SkyBlockRedefined.getNetherWorld())) {
            return;
        }
        if (DEBUG)
            plugin.getLogger().info("DEBUG correct world");
        Location islandLoc = plugin.getPlayers().getIslandLocation(event.getPlayer().getUniqueId());
        if (islandLoc == null) {
            if (DEBUG)
                plugin.getLogger().info("DEBUG  no island");
            // They have no island
            if (Settings.makeIslandIfNone || Settings.immediateTeleport) {
                event.getPlayer().performCommand(Settings.ISLANDCOMMAND);
            }
            if (DEBUG)
                plugin.getLogger().info("DEBUG Make island");
        } else {
            // They have an island and are going to their own world
            if (Settings.immediateTeleport && islandLoc.getWorld().equals(event.getPlayer().getWorld())) {
                if (DEBUG)
                    plugin.getLogger().info("DEBUG teleport");
                event.getPlayer().performCommand(Settings.ISLANDCOMMAND + " go");
            }
        }
    }
}
