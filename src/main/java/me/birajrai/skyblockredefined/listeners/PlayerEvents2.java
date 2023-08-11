package me.birajrai.skyblockredefined.listeners;

import me.birajrai.skyblockredefined.SkyBlockRedefined;
import me.birajrai.skyblockredefined.Island;
import me.birajrai.skyblockredefined.Island.SettingsFlag;
import me.birajrai.skyblockredefined.Settings;
import me.birajrai.skyblockredefined.util.VaultHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * @author tastybento
 *         Provides protection to islands
 */
public class PlayerEvents2 implements Listener {
    private final SkyBlockRedefined plugin;
    private static final boolean DEBUG = false;

    public PlayerEvents2(final SkyBlockRedefined plugin) {
        this.plugin = plugin;
    }

    /*
     * Prevent item pickup by visitors for servers before 1.12.
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onVisitorPickup(final PlayerPickupItemEvent e) {
        if (DEBUG) {
            plugin.getLogger().info(e.getEventName());
        }
        if (!IslandGuard.inWorld(e.getPlayer())) {
            return;
        }
        Island island = plugin.getGrid().getIslandAt(e.getItem().getLocation());
        if ((island != null && island.getIgsFlag(SettingsFlag.VISITOR_ITEM_PICKUP)) 
                || e.getPlayer().isOp() || VaultHelper.checkPerm(e.getPlayer(), Settings.PERMPREFIX + "mod.bypassprotect")
                || plugin.getGrid().locationIsOnIsland(e.getPlayer(), e.getItem().getLocation())) {
            return;
        }
        e.setCancelled(true);
    }
}
