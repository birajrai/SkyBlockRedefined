package me.birajrai.skyblockredefined.events;

import me.birajrai.skyblockredefined.Island;
import org.bukkit.Location;

import java.util.UUID;


/**
 * Fired when a player exits an island's protected area
 * @author tastybento
 *
 */
public class IslandExitEvent extends ASkyBlockEvent {
    private final Location location;

    /**
     * @param player
     * @param island that the player is leaving
     * @param location - Location of where the player exited the island's protected area
     */
    public IslandExitEvent(UUID player, Island island, Location location) {
        super(player,island);
        this.location = location;
        //Bukkit.getLogger().info("DEBUG: IslandExitEvent called");
    }

    /**
     * Location of where the player exited the island's protected area
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

}
