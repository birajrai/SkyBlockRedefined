package me.birajrai.skyblockredefined.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is fired when ASkyBlock is ready to play and all files are loaded
 * 
 * @author tastybento
 * 
 */
public class ReadyEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
