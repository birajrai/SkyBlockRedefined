package me.birajrai.skyblockredefined.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * This event is fired when a player leaves an existing Team
 * 
 * @author Exloki
 * 
 */
public class TeamLeaveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final UUID player;
    private final UUID oldTeamLeader;
    private boolean cancelled;

    public TeamLeaveEvent(UUID player, UUID oldTeamLeader) {
        super();
        this.player = player;
        this.oldTeamLeader = oldTeamLeader;
    }

    /**
     * The UUID of the player changing Team
     * @return the player UUID
     */
    public UUID getPlayer() {
        return player;
    }

    /**
     * The UUID of the player's previous Team's Leader
     * @return the team leader
     */
    public UUID getOldTeamLeader() {
        return oldTeamLeader;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
        
    }
}
