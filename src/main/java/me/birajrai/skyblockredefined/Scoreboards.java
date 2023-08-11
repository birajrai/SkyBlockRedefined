package me.birajrai.skyblockredefined;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

/**
 * This class puts a player into a "team" and sets the island level as the suffix.
 * The team suffix variable can then be used by other plugins, such as Essentials Chat
 * {TEAMSUFFIX}
 * @author tastybento
 *
 */
public class Scoreboards {
    private static final SkyBlockRedefined plugin = SkyBlockRedefined.getPlugin();
    private static final Scoreboards instance = new Scoreboards();
    private static Scoreboard board;

    /**
     * 
     */
    private Scoreboards() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
    }

    /**
     * @return the instance
     */
    public static Scoreboards getInstance() {
        return instance;
    }

    /**
     * Puts a player into a team of their own and sets the team suffix to be the level
     * @param playerUUID - the player's UUID
     */
    public void setLevel(UUID playerUUID) {
        Player player = plugin.getServer().getPlayer(playerUUID);
        if (player == null) {
            // Player is offline...
            return;
        }
        // The default team name is their own name
        String teamName = player.getName();
        String level = String.valueOf(plugin.getPlayers().getIslandLevel(playerUUID));
        Team team = board.getTeam(teamName);
        if (team == null) {
            //Team does not exist
            team = board.registerNewTeam(teamName);
        }
        // Add the suffix
        team.setSuffix(Settings.teamSuffix.replace("[level]",String.valueOf(level)));
        //Adding player to team
        team.addPlayer(player);
        // Assign scoreboard to player
        player.setScoreboard(board);
    } 

    /**
     * Sets the player's level explicitly
     * @param playerUUID - the player's UUID
     * @param l
     */
    public void setLevel(UUID playerUUID, long l) {
        Player player = plugin.getServer().getPlayer(playerUUID);
        if (player == null) {
            // Player is offline...
            return;
        }
        // The default team name is their own name - must be 16 chars or less
        String teamName = player.getName();
        Team team = board.getTeam(teamName);
        if (team == null) {
            //Team does not exist
            team = board.registerNewTeam(teamName);
        }
        // Add the suffix
        team.setSuffix(Settings.teamSuffix.replace("[level]",String.valueOf(l)));
        //Adding player to team
        team.addPlayer(player);
        // Assign scoreboard to player
        player.setScoreboard(board);
    }
}
