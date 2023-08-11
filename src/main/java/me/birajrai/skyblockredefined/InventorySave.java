package me.birajrai.skyblockredefined;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Stashes inventories when required for a player
 * 
 * @author tastybento
 * 
 */
public class InventorySave {
    private static InventorySave instance = new InventorySave(SkyBlockRedefined.getPlugin());
    private final Map<UUID, InventoryStore> inventories;

    /**
     * Saves the inventory of a player
     * @param plugin - ASkyBlock plugin object - ASkyBlock plugin
     */
    public InventorySave(SkyBlockRedefined plugin) {
        inventories = new HashMap<>();
    }

    /** Save player's inventory
     * @param player player object
     */
    public void savePlayerInventory(Player player) {
        //plugin.getLogger().info("DEBUG: Saving inventory");
        // Save the player's armor and things
        inventories.put(player.getUniqueId(),new InventoryStore(player.getInventory().getContents(), player.getInventory().getArmorContents()));
    }

    /**
     * Clears any saved inventory
     * @param player player object
     */
    public void clearSavedInventory(Player player) {
        //plugin.getLogger().info("DEBUG: Clearing inventory");
        inventories.remove(player.getUniqueId());
    }
    /**
     * Load the player's inventory
     * 
     * @param player playe object
     */
    public void loadPlayerInventory(Player player) {
        //plugin.getLogger().info("DEBUG: Loading inventory");
        // Get the info for this player
        if (inventories.containsKey(player.getUniqueId())) {
            InventoryStore inv = inventories.get(player.getUniqueId());
            //plugin.getLogger().info("DEBUG: player is known");
            player.getInventory().setContents(inv.getInventory());
            player.getInventory().setArmorContents(inv.getArmor());
            inventories.remove(player.getUniqueId());
        }
    }

    public static InventorySave getInstance() {
        return instance;
    }

}
