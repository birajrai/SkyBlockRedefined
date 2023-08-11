package me.birajrai.skyblockredefined.events;

import me.birajrai.skyblockredefined.Island;
import org.bukkit.Location;

import java.util.UUID;


/**
 * Fired when a player enters an island's area
 * @author tastybento
 *
 */
public class IslandEnterEvent extends ASkyBlockEvent {
    private final Location location;

    /**
     * Called to create the event
     * @param player
     * @param island - island the player is entering
     * @param location - Location of where the player entered the island or tried to enter
     */
    public IslandEnterEvent(UUID player, Island island, Location location) {
        super(player,island);
        this.location = location;
        //Bukkit.getLogger().info("DEBUG: IslandEnterEvent called");
    }

    /**
     * Location of where the player entered the island or tried to enter
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

}
