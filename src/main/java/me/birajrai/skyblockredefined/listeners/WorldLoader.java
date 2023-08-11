package me.birajrai.skyblockredefined.listeners;

import me.birajrai.skyblockredefined.SkyBlockRedefined;
import me.birajrai.skyblockredefined.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class WorldLoader implements Listener {
    private final SkyBlockRedefined plugin;
    private boolean worldLoaded = false;
    private static final boolean DEBUG = false;

    /**
     * Class to force world loading before plugins.
     * @param plugin - ASkyBlock plugin object
     */
    public WorldLoader(SkyBlockRedefined plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChunkLoad(final ChunkLoadEvent event) {
        if (worldLoaded) {
            return;
        }
        if (DEBUG)
            plugin.getLogger().info("DEBUG: " + event.getEventName() + " : " + event.getWorld().getName());
        if (event.getWorld().getName().equals(Settings.worldName) || event.getWorld().getName().equals(Settings.worldName + "_nether")) {
            return;
        }
        // Load the world
        worldLoaded = true;
        SkyBlockRedefined.getIslandWorld();
    }
}
