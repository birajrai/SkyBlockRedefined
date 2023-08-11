package me.birajrai.skyblockredefined.util;

import me.birajrai.skyblockredefined.nms.NMSAbstraction;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a spawn egg that can be used to spawn mobs. Only for V1.9 servers
 * 
 * @author tastybento
 */
public class SpawnEgg1_9 {
    private EntityType type;
    private NMSAbstraction nms;

    public SpawnEgg1_9(EntityType type) {
        this.type = type;
        nms = null;
        try {
            nms = Util.checkVersion();
        } catch (Exception ex) {
            Bukkit.getLogger().severe("Could not find NMS code to support");
        }
    }

    /**
     * Get an ItemStack of one spawn egg 
     * @return ItemStack
     */
    public ItemStack toItemStack() {
        return toItemStack(1);
    }

    
    /**
     * Get an itemstack of spawn eggs
     * @param amount
     * @return ItemStack of spawn eggs
     */
    public ItemStack toItemStack(int amount) {        
        return nms.getSpawnEgg(type, amount);
    }

}
