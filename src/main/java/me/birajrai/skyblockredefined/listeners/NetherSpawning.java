package me.birajrai.skyblockredefined.listeners;

import me.birajrai.skyblockredefined.SkyBlockRedefined;
import me.birajrai.skyblockredefined.Settings;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.Random;

/**
 * This makes wither skeletons spawn from spawners in the nether again in 1.11
 * @author tastybento
 *
 */
public class NetherSpawning implements Listener {
    private final SkyBlockRedefined plugin;
    private final static boolean DEBUG = false;
    private final static double WITHER_SKELETON_SPAWN_CHANCE = 0.1;
    private final Random random = new Random();
    private boolean hasWitherSkeleton = false;

    public NetherSpawning(SkyBlockRedefined plugin) {
        this.plugin = plugin;
        for (EntityType type: EntityType.values()) {
            if (type.toString().equals("WITHER_SKELETON")) {
                this.hasWitherSkeleton = true;
                if (DEBUG)
                    plugin.getLogger().info("DEBUG: Wither Skeleton exists");
                break;
            }    
        }
    }


    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSkeletonSpawn(final CreatureSpawnEvent e) {
        if (DEBUG)
            plugin.getLogger().info("DEBUG: " + e.getEventName());
        if (!Settings.hackSkeletonSpawners) {
            return;
        }
        if (!hasWitherSkeleton) {
            // Only if this type of Entity exists
            return;
        }
        // Check for spawn reason
        if (e.getSpawnReason().equals(SpawnReason.SPAWNER) && e.getEntityType().equals(EntityType.SKELETON)) {
            if (!Settings.createNether || !Settings.newNether || SkyBlockRedefined.getNetherWorld() == null) {
                return;
            }
            // Check world
            if (!e.getLocation().getWorld().equals(SkyBlockRedefined.getNetherWorld())) {
                return;
            }
            if (random.nextDouble() < WITHER_SKELETON_SPAWN_CHANCE) {
                if (DEBUG)
                    plugin.getLogger().info("DEBUG: Wither Skelly spawned");
                e.setCancelled(true);
                e.getLocation().getWorld().spawnEntity(e.getLocation(), EntityType.WITHER_SKELETON);
            } else {
                if (DEBUG)
                    plugin.getLogger().info("DEBUG: Standard Skelly spawned");
            }
        }
    }
}