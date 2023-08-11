package me.birajrai.skyblockredefined.events;

import me.birajrai.skyblockredefined.Island;

import java.util.UUID;

/**
 * This event is fired when a player joins an island team
 * 
 * @author tastybento
 * 
 */
public class IslandJoinEvent extends ASkyBlockEvent {


    /**
     * @param player
     * @param island
     */
    public IslandJoinEvent(UUID player, Island island) {
        super(player,island);
    }

}
