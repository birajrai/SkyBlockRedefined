package me.birajrai.skyblockredefined.events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * This event is fired when a player resets an island
 *
 * @author tastybento
 *
 */
public class IslandDeleteEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private final UUID playerUUID;
	private final Location location;

	/**
	 * @param playerUUID - the player's UUID
	 * @param oldLocation
	 */
	public IslandDeleteEvent(UUID playerUUID, Location oldLocation) {
		this.playerUUID = playerUUID;
		this.location = oldLocation;
	}

	/**
	 * @return the player's UUID
	 */
	public UUID getPlayerUUID() {
		return playerUUID;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
