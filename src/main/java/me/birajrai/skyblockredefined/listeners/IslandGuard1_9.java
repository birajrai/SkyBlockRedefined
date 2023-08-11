package me.birajrai.skyblockredefined.listeners;

import me.birajrai.skyblockredefined.SkyBlockRedefined;
import me.birajrai.skyblockredefined.Island;
import me.birajrai.skyblockredefined.Island.SettingsFlag;
import me.birajrai.skyblockredefined.Settings;
import me.birajrai.skyblockredefined.util.Util;
import me.birajrai.skyblockredefined.util.VaultHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;

/**
 * @author tastybento
 *         Provides protection to islands - handles newer events that may not
 *         exist in older servers
 */
public class IslandGuard1_9 implements Listener {
    private final SkyBlockRedefined plugin;
    private static final String NO_PUSH_TEAM_NAME = "ASkyBlockNP";
    private Scoreboard scoreboard;
    private final Map<Integer, UUID> thrownPotions;

    public IslandGuard1_9(final SkyBlockRedefined plugin) {
        this.plugin = plugin;
        this.thrownPotions = new HashMap<>();
        if (!Settings.allowPushing) {
            // try to remove the team from the scoreboard
            try {
                ScoreboardManager manager = plugin.getServer().getScoreboardManager();
                if (manager != null) {
                    Scoreboard scoreboard = manager.getMainScoreboard();
                    if (scoreboard != null) {
                        Team pTeam = scoreboard.getTeam(NO_PUSH_TEAM_NAME);
                        if (pTeam != null) {
                            pTeam.unregister();
                        }
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Problem removing no push from scoreboard.");
            }
        }
    }

    /**
     * Handles Frost Walking on visitor's islands
     * @param e - event
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onBlockForm(final EntityBlockFormEvent e) {
        if (e.getEntity() instanceof Player && e.getNewState().getType().equals(Material.FROSTED_ICE)) {
            Player player= (Player) e.getEntity();
            if (!IslandGuard.inWorld(player)) {
                return;
            }
            if (player.isOp()) {
                return;
            }
            // This permission bypasses protection
            if (VaultHelper.checkPerm(player, Settings.PERMPREFIX + "mod.bypassprotect")) {
                return;
            }
            // Check island
            Island island = plugin.getGrid().getIslandAt(player.getLocation());
            if (island == null && Settings.defaultWorldSettings.get(SettingsFlag.PLACE_BLOCKS)) {
                return;
            }
            if (island !=null) {
                if (island.getMembers().contains(player.getUniqueId()) || island.getIgsFlag(SettingsFlag.PLACE_BLOCKS)) {
                    return;
                }
            }
            // Silently cancel the event
            e.setCancelled(true);
        }
    }


    /**
     * Handle interaction with end crystals 1.9
     * 
     * @param e - event
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onHitEndCrystal(final PlayerInteractAtEntityEvent e) {
        if (!IslandGuard.inWorld(e.getPlayer())) {
            return;
        }
        if (e.getPlayer().isOp()) {
            return;
        }
        // This permission bypasses protection
        if (VaultHelper.checkPerm(e.getPlayer(), Settings.PERMPREFIX + "mod.bypassprotect")) {
            return;
        }
        if (e.getRightClicked() != null && e.getRightClicked().getType().equals(EntityType.ENDER_CRYSTAL)) {
            // Check island
            Island island = plugin.getGrid().getIslandAt(e.getRightClicked().getLocation());
            if (island == null && Settings.defaultWorldSettings.get(SettingsFlag.BREAK_BLOCKS)) {
                return;
            }
            if (island !=null) {
                if (island.getMembers().contains(e.getPlayer().getUniqueId()) || island.getIgsFlag(SettingsFlag.BREAK_BLOCKS)) {
                    return;
                }
            }
            e.setCancelled(true);
            Util.sendMessage(e.getPlayer(), ChatColor.RED + plugin.myLocale(e.getPlayer().getUniqueId()).islandProtected);
        }
    }

    // End crystal
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
    void placeEndCrystalEvent(final PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!IslandGuard.inWorld(p)) {
            return;
        }
        if (p.isOp() || VaultHelper.checkPerm(p, Settings.PERMPREFIX + "mod.bypassprotect")) {
            // You can do anything if you are Op
            return;
        }

        // Check if they are holding armor stand
        for (ItemStack inHand : Util.getPlayerInHandItems(e.getPlayer())) {
            if (inHand.getType().equals(Material.END_CRYSTAL)) {
                // Check island
                Island island = plugin.getGrid().getIslandAt(e.getPlayer().getLocation());
                if (island == null && Settings.defaultWorldSettings.get(SettingsFlag.PLACE_BLOCKS)) {
                    return;
                }
                if (island !=null && (island.getMembers().contains(p.getUniqueId()) || island.getIgsFlag(SettingsFlag.PLACE_BLOCKS))) {
                    //plugin.getLogger().info("1.9 " +"DEBUG: armor stand place check");
                    if (Settings.limitedBlocks.containsKey("END_CRYSTAL") && Settings.limitedBlocks.get("END_CRYSTAL") > -1) {
                        //plugin.getLogger().info("1.9 " +"DEBUG: count armor stands");
                        int count = island.getTileEntityCount(Material.END_CRYSTAL,e.getPlayer().getWorld());
                        //plugin.getLogger().info("1.9 " +"DEBUG: count is " + count + " limit is " + Settings.limitedBlocks.get("ARMOR_STAND"));
                        if (Settings.limitedBlocks.get("END_CRYSTAL") <= count) {
                            Util.sendMessage(e.getPlayer(), ChatColor.RED + (plugin.myLocale(e.getPlayer().getUniqueId()).entityLimitReached.replace("[entity]",
                                    Util.prettifyText(Material.END_CRYSTAL.toString()))).replace("[number]", String.valueOf(Settings.limitedBlocks.get("END_CRYSTAL"))));
                            e.setCancelled(true);
                            return;
                        }
                    }
                    return;
                }
                // plugin.getLogger().info("1.9 " +"DEBUG: stand place cancelled");
                e.setCancelled(true);
                Util.sendMessage(e.getPlayer(), ChatColor.RED + plugin.myLocale(e.getPlayer().getUniqueId()).islandProtected);
                e.getPlayer().updateInventory();
            }
        }

    }

    /**
     * Handle end crystal damage by visitors
     * @param e - event
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void EndCrystalDamage(final EntityDamageByEntityEvent e) {
        if (e.getEntity() == null || !IslandGuard.inWorld(e.getEntity())) {
            return;
        }
        if (!(e.getEntity() instanceof EnderCrystal)) {
            return;
        }
        Player p = null;
        if (e.getDamager() instanceof Player) {
            p = (Player) e.getDamager();
        } else if (e.getDamager() instanceof Projectile) {
            // Get the shooter
            Projectile projectile = (Projectile)e.getDamager();
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player) {
                p = (Player)shooter;
            }
        }
        if (p != null) {  
            if (p.isOp() || VaultHelper.checkPerm(p, Settings.PERMPREFIX + "mod.bypassprotect")) {
                return;
            }
            // Check if on island
            if (plugin.getGrid().playerIsOnIsland(p)) {
                return;
            }
            // Check island
            Island island = plugin.getGrid().getIslandAt(e.getEntity().getLocation());
            if (island == null && Settings.defaultWorldSettings.get(SettingsFlag.BREAK_BLOCKS)) {
                return;
            }
            if (island != null && island.getIgsFlag(SettingsFlag.BREAK_BLOCKS)) {
                return;
            }
            Util.sendMessage(p, ChatColor.RED + plugin.myLocale(p.getUniqueId()).islandProtected);
            e.setCancelled(true);
        }

    }

    /**
     * Handles end crystal explosions
     * @param e - event
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onExplosion(final EntityExplodeEvent e) {
        if (e.getEntity() == null || !e.getEntityType().equals(EntityType.ENDER_CRYSTAL)) {
            return;
        }

        if (!IslandGuard.inWorld(e.getLocation())) {
            return;
        }
        // General settings irrespective of whether this is allowed or not
        if (!Settings.allowTNTDamage) {
            plugin.getLogger().info("1.9 " +"TNT block damage prevented");
            e.blockList().clear();
        } else {
            if (!Settings.allowChestDamage) {
                List<Block> toberemoved = new ArrayList<>();
                // Save the chest blocks in a list
                for (Block b : e.blockList()) {
                    switch (b.getType()) {
                    case CHEST:
                    case ENDER_CHEST:
                    case STORAGE_MINECART:
                    case TRAPPED_CHEST:
                        toberemoved.add(b);
                        break;
                    default:
                        break;
                    }
                }
                // Now delete them
                for (Block b : toberemoved) {
                    e.blockList().remove(b);
                }
            }
        }
        // prevent at spawn
        if (plugin.getGrid().isAtSpawn(e.getLocation())) {
            e.blockList().clear();
            e.setCancelled(true);
        }

    }

    /**
     * Triggers a push protection change or not
     * @param e - event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        if (Settings.allowPushing) {           
            Team t = e.getPlayer().getScoreboard().getTeam(NO_PUSH_TEAM_NAME);
            if (t != null) {
                t.unregister();
            }
            return;
        }
        setPush(e.getPlayer());
    }

    /**
     * Triggers scoreboard cleanup on Quit
     * @param e - event
     */
    @EventHandler(priority = EventPriority.NORMAL,ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent e)
    {
        if(Settings.allowPushing)
        {
            return;
        }
        removePush(e.getPlayer());
    }

    /**
     * Handles push protection
     * @param player
     */
    public void setPush(Player player) {
        scoreboard = player.getScoreboard();
        if (scoreboard == null) {
            //plugin.getLogger().info("1.9 " +"DEBUG: initializing scoreboard");
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }
        if (Settings.allowPushing) {
            if (scoreboard.getTeam(NO_PUSH_TEAM_NAME) != null) {
                //plugin.getLogger().info("1.9 " +"DEBUG: unregistering the team");
                scoreboard.getTeam(NO_PUSH_TEAM_NAME).unregister();
            }
            return;
        }
        // Try and get what team the player is on right now
        Team pushTeam = scoreboard.getEntryTeam(player.getName());
        if (pushTeam == null) {
            // It doesn't exist yet, so make it
            pushTeam = scoreboard.getTeam(NO_PUSH_TEAM_NAME);
            if (pushTeam == null) {
                pushTeam = scoreboard.registerNewTeam(NO_PUSH_TEAM_NAME);
            }
            // Add the player to the team
            pushTeam.addEntry(player.getName()); 
        }
        if (pushTeam.getName().equals(NO_PUSH_TEAM_NAME)) {
            //plugin.getLogger().info("1.9 " +"DEBUG: pushing not allowed");
            pushTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);               
        } else {
            //plugin.getLogger().info("1.9 " +"DEBUG: player is already in another team");
        }
    }

    /**
     * Handles cleaning push protection on player quit
     * @param player
     */
    private void removePush(Player player)
    {
        try {
            scoreboard = player.getScoreboard();
            if(scoreboard !=null)
            {
                //Player Remove
                Team pTeam = scoreboard.getTeam(NO_PUSH_TEAM_NAME);
                if (pTeam != null) {
                    pTeam.removeEntry(player.getName());
                }
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Error trying to remove player from push scoreboard");
            plugin.getLogger().severe(player.getName() + " : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle blocks that need special treatment
     * Tilling of coarse dirt into dirt using off-hand (regular hand is in 1.8)
     * Usually prevented because it could lead to an endless supply of dirt with gravel
     * 
     * @param e - event
     */
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onPlayerInteract(final PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!IslandGuard.inWorld(e.getPlayer())) {
            return;
        }
        if (e.getPlayer().isOp()) {
            return;
        }
        // This permission bypasses protection
        if (VaultHelper.checkPerm(e.getPlayer(), Settings.PERMPREFIX + "mod.bypassprotect")
                || VaultHelper.checkPerm(e.getPlayer(), Settings.PERMPREFIX + "craft.dirt")) {
            return;
        }
        // Prevents tilling of coarse dirt into dirt
        ItemStack inHand = e.getPlayer().getInventory().getItemInOffHand();
        if (inHand.getType() == Material.WOOD_HOE || inHand.getType() == Material.IRON_HOE || inHand.getType() == Material.GOLD_HOE
                || inHand.getType() == Material.DIAMOND_HOE || inHand.getType() == Material.STONE_HOE) {
            // plugin.getLogger().info("1.8 " + "DEBUG: hoe in hand");
            Block block = e.getClickedBlock();
            // plugin.getLogger().info("1.8 " + "DEBUG: block is " + block.getType() +
            // ":" + block.getData());
            // Check if coarse dirt
            if (block.getType() == Material.DIRT && block.getData() == (byte) 1) {
                // plugin.getLogger().info("1.8 " + "DEBUG: hitting coarse dirt!");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onLingeringPotionSplash(final LingeringPotionSplashEvent e) {
        if (!IslandGuard.inWorld(e.getEntity().getLocation())) {
            return;
        }
        // Try to get the shooter
        Projectile projectile = (Projectile) e.getEntity();
        if (projectile.getShooter() != null && projectile.getShooter() instanceof Player) {
            UUID uuid = ((Player)projectile.getShooter()).getUniqueId();
            // Store it and remove it when the effect is gone
            thrownPotions.put(e.getAreaEffectCloud().getEntityId(), uuid);
            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

                @Override
                public void run() {
                    thrownPotions.remove(e.getAreaEffectCloud().getEntityId());

                }}, e.getAreaEffectCloud().getDuration());
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onLingeringPotionDamage(final EntityDamageByEntityEvent e) {
        if (!IslandGuard.inWorld(e.getEntity().getLocation())) {
            return;
        }
        if (e.getEntity() == null || e.getEntity().getUniqueId() == null) {
            return;
        }
        if (e.getCause().equals(DamageCause.ENTITY_ATTACK) && thrownPotions.containsKey(e.getDamager().getEntityId())) {
            UUID attacker = thrownPotions.get(e.getDamager().getEntityId());
            // Self damage
            if (attacker.equals(e.getEntity().getUniqueId())) {
                return;
            }
            Island island = plugin.getGrid().getIslandAt(e.getEntity().getLocation());
            boolean inNether = false;
            if (e.getEntity().getWorld().equals(SkyBlockRedefined.getNetherWorld())) {
                inNether = true;
            }
            // Monsters being hurt
            if (e.getEntity() instanceof Monster || e.getEntity() instanceof Slime || e.getEntity() instanceof Squid) {
                // Normal island check
                if (island != null && island.getMembers().contains(attacker)) {
                    // Members always allowed
                    return;
                }
                if (actionAllowed(attacker, e.getEntity().getLocation(), SettingsFlag.HURT_MONSTERS)) {
                    return;
                }
                // Not allowed
                e.setCancelled(true);
                return;
            }

            // Mobs being hurt
            if (e.getEntity() instanceof Animals || e.getEntity() instanceof IronGolem || e.getEntity() instanceof Snowman
                    || e.getEntity() instanceof Villager) {
                if (island != null && (island.getIgsFlag(SettingsFlag.HURT_MOBS) || island.getMembers().contains(attacker))) {
                    return;
                }
                e.setCancelled(true);
                return;
            }

            // Establish whether PVP is allowed or not.
            boolean pvp = false;
            if ((inNether && island != null && island.getIgsFlag(SettingsFlag.NETHER_PVP) || (!inNether && island != null && island.getIgsFlag(SettingsFlag.PVP)))) {
                pvp = true;
            }

            // Players being hurt PvP
            if (e.getEntity() instanceof Player) {
                if (pvp) {
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onRodDamage(final PlayerFishEvent e) {
        if (e.getPlayer().isOp() || VaultHelper.checkPerm(e.getPlayer(), Settings.PERMPREFIX + "mod.bypassprotect")) {
            return;
        }
        if (!IslandGuard.inWorld(e.getPlayer().getLocation())) {
            return;
        }
        Player p = e.getPlayer();
        if (e.getCaught() != null && (e.getCaught().getType().equals(EntityType.ARMOR_STAND) || e.getCaught().getType().equals(EntityType.ENDER_CRYSTAL))) {
            if (p.isOp() || VaultHelper.checkPerm(p, Settings.PERMPREFIX + "mod.bypassprotect")) {
                return;
            }
            // Check if on island
            if (plugin.getGrid().playerIsOnIsland(p)) {
                return;
            }
            // Check island
            Island island = plugin.getGrid().getIslandAt(e.getCaught().getLocation());
            if (island == null && Settings.defaultWorldSettings.get(SettingsFlag.BREAK_BLOCKS)) {
                return;
            }
            if (island != null && island.getIgsFlag(SettingsFlag.BREAK_BLOCKS)) {
                return;
            }
            Util.sendMessage(p, ChatColor.RED + plugin.myLocale(p.getUniqueId()).islandProtected);
            e.getHook().remove();
            e.setCancelled(true);
        }
    }
    
    /**
     * Checks if action is allowed for player in location for flag
     * @param uuid
     * @param location
     * @param flag
     * @return true if allowed
     */
    private boolean actionAllowed(UUID uuid, Location location, SettingsFlag flag) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) {
            return actionAllowed(location, flag);
        }
        // This permission bypasses protection
        if (player.isOp() || VaultHelper.checkPerm(player, Settings.PERMPREFIX + "mod.bypassprotect")) {
            return true;
        }
        Island island = plugin.getGrid().getProtectedIslandAt(location);
        if (island != null && (island.getIgsFlag(flag) || island.getMembers().contains(player.getUniqueId()))){
            return true;
        }
        if (island == null && Settings.defaultWorldSettings.get(flag)) {
            return true;
        }
        return false;
    }

    /**
     * Action allowed in this location
     * @param location
     * @param flag
     * @return true if allowed
     */
    private boolean actionAllowed(Location location, SettingsFlag flag) {
        Island island = plugin.getGrid().getProtectedIslandAt(location);
        if (island != null && island.getIgsFlag(flag)){
            return true;
        }
        return island == null && Settings.defaultWorldSettings.get(flag);
    }
}