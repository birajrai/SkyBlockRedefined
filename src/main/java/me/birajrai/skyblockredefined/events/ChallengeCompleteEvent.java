package me.birajrai.skyblockredefined.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * This event is fired when a player completes a challenge
 * 
 * @author tastybento
 * 
 */
public class ChallengeCompleteEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String challengeName;
    private String[] permList;
    private String[] itemRewards;
    private final double moneyReward;
    private final int expReward;
    private final String rewardText;
    private final List<ItemStack> rewardedItems;

    /**
     * @param player
     * @param challengeName
     * @param permList
     * @param itemRewards
     * @param moneyReward
     * @param expReward
     * @param rewardText
     */
    public ChallengeCompleteEvent(Player player, String challengeName, String[] permList, String[] itemRewards, double moneyReward, int expReward,
            String rewardText, List<ItemStack> rewardedItems) {
        this.player = player;
        this.challengeName = challengeName;
        this.permList = permList;
        this.itemRewards = itemRewards;
        this.moneyReward = moneyReward;
        this.expReward = expReward;
        this.rewardText = rewardText;
        this.rewardedItems = rewardedItems;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the challengeName
     */
    public String getChallengeName() {
        return challengeName;
    }

    /**
     * @return the permList
     */
    public String[] getPermList() {
        return permList;
    }

    /**
     * @return the itemRewards
     */
    public String[] getItemRewards() {
        return itemRewards;
    }

    /**
     * @return the moneyReward
     */
    public double getMoneyReward() {
        return moneyReward;
    }

    /**
     * @return the expReward
     */
    public int getExpReward() {
        return expReward;
    }

    /**
     * @return the rewardText
     */
    public String getRewardText() {
        return rewardText;
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
