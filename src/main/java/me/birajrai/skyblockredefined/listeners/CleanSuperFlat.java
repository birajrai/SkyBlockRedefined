package me.birajrai.skyblockredefined.listeners;

import me.birajrai.skyblockredefined.SkyBlockRedefined;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

/**
 * Cleans superflat chunks if they exist
 * Used to recover from a failed generator
 * 
 * @author tastybento
 */
public class CleanSuperFlat implements Listener {
    private static final boolean DEBUG = false;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChunkLoad(ChunkLoadEvent e) {
        if (SkyBlockRedefined.getIslandWorld() == null || e.getWorld() != SkyBlockRedefined.getIslandWorld()) {
            if (DEBUG)
                Bukkit.getLogger().info("DEBUG: not right world");
            return;
        }
        if (e.getChunk().getBlock(0, 0, 0).getType().equals(Material.BEDROCK)) {
            e.getWorld().regenerateChunk(e.getChunk().getX(), e.getChunk().getZ());
            Bukkit.getLogger().warning("Regenerating superflat chunk at " + (e.getChunk().getX() * 16) + "," + (e.getChunk().getZ() * 16));
        }
    }


}