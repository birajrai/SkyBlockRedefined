package me.birajrai.skyblockredefined.events;

import me.birajrai.skyblockredefined.panels.MiniShopItem;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;


/**
 * Fired when a player buys or sells in the mini shop
 * @author tastybento
 *
 */
public class MiniShopEvent extends ASkyBlockEvent {
    public enum TransactionType {BUY, SELL}
    private final MiniShopItem item;
    private final TransactionType type;

    /**
     * Called to create the event
     * @param player
     * @param item
     * @param type
     */
    public MiniShopEvent(final UUID player, final MiniShopItem item, final TransactionType type) {
        super(player);
        this.item = item;
        this.type = type;
    }

    /**
     * @return Description of the item
     */
    public String getDescription() {
        return item.getDescription();
    }

    /**
     * @return The item in itemstack form
     */
    public ItemStack getItem() {
        return item.getItemClean();
    }

    /**
     * @return The sell price or buy price
     */
    public double getPrice() {
        if (type == TransactionType.BUY) {
            return item.getPrice();
        } else {
            return item.getSellPrice();
        }
    }

    /**
     * @return The number of the item
     */
    public int getQuantity() {
        return item.getQuantity();
    }

    /**
     * @return The transaction type BUY or SELL
     */
    public TransactionType getTransactionType() {
        return type;
    }


}
