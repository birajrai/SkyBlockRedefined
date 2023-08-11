package me.birajrai.skyblockredefined.listeners;

import me.birajrai.skyblockredefined.SkyBlockRedefined;
import me.birajrai.skyblockredefined.Island;
import me.birajrai.skyblockredefined.Island.SettingsFlag;
import me.birajrai.skyblockredefined.Settings;
import me.birajrai.skyblockredefined.util.VaultHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

/**
 * @author tastybento
 *         Provides protection to islands
 */
public class PlayerEvents3 implements Listener {
    private final SkyBlockRedefined plugin;
    private static final boolean DEBUG = false;

    public PlayerEvents3(final SkyBlockRedefined plugin) {
        this.plugin = plugin;
    }

    /*
     * Prevent item pickup by visitors for 1.12+.
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onVisitorPickup(final EntityPickupItemEvent e) {
        if (DEBUG) {
            plugin.getLogger().info(e.getEventName());
        }
        if (e.getEntity() instanceof Player) {
            Player player = (Player)e.getEntity();
            if (!IslandGuard.inWorld(player)) {
                return;
            }
            Island island = plugin.getGrid().getIslandAt(e.getItem().getLocation());
            if ((island != null && island.getIgsFlag(SettingsFlag.VISITOR_ITEM_PICKUP)) 
                    || player.isOp() || VaultHelper.checkPerm(player, Settings.PERMPREFIX + "mod.bypassprotect")
                    || plugin.getGrid().locationIsOnIsland(player, e.getItem().getLocation())) {
                return;
            }
            e.setCancelled(true);
        }
    }
}
