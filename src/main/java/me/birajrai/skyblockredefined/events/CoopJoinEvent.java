package me.birajrai.skyblockredefined.events;

import me.birajrai.skyblockredefined.Island;
import org.bukkit.event.Cancellable;

import java.util.UUID;

/**
 * This event is fired when a player joins an island team as a coop member
 * 
 * @author birajrai
 * 
 */
public class CoopJoinEvent extends ASkyBlockEvent implements Cancellable {

    private final UUID inviter;
    private boolean cancelled;

    /**
     * @param player
     * @param island
     * @param inviter
     */
    public CoopJoinEvent(UUID player, Island island, UUID inviter) {
        super(player, island);
        this.inviter = inviter;
        //Bukkit.getLogger().info("DEBUG: Coop join event " + Bukkit.getServer().getOfflinePlayer(player).getName() + " joined " 
        //+ Bukkit.getServer().getOfflinePlayer(inviter).getName() + "'s island.");
    }

    /**
     * The UUID of the player who invited the player to join the island
     * @return the inviter
     */
    public UUID getInviter() {
        return inviter;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;   
    }

}
