package me.birajrai.skyblockredefined.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * This event is fired when a player joins a new Team
 * 
 * @author Exloki
 * 
 */
public class TeamJoinEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final UUID player;
    private final UUID newTeamLeader;
    private boolean cancelled;

    public TeamJoinEvent(UUID player, UUID newTeamLeader) {
        super();
        this.player = player;
        this.newTeamLeader = newTeamLeader;
    }

    /**
     * The UUID of the player changing Team
     * @return the player UUID
     */
    public UUID getPlayer() {
        return player;
    }

    /**
     * The UUID of the new Team's Leader
     * @return the team leader
     */
    public UUID getNewTeamLeader() {
        return newTeamLeader;
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
