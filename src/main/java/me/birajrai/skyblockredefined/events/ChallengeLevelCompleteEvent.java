package me.birajrai.skyblockredefined.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * This event is fired when a player completes a challenge level
 * 
 * @author tastybento
 * 
 */
public class ChallengeLevelCompleteEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final int oldLevel;
    private final int newLevel;
    private final List<ItemStack> rewardedItems;

    /**
     * @param player
     * @param oldLevel
     * @param newLevel
     * @param rewardedItems 
     */
    public ChallengeLevelCompleteEvent(Player player, int oldLevel, int newLevel, List<ItemStack> rewardedItems) {
        this.player = player;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
        this.rewardedItems = rewardedItems;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the oldLevel
     */
    public int getOldLevel() {
        return oldLevel;
    }

    /**
     * @return the newLevel
     */
    public int getNewLevel() {
        return newLevel;
    }

    /**
     * @return the rewardedItems
     */
    public List<ItemStack> getRewardedItems() {
        return rewardedItems;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
