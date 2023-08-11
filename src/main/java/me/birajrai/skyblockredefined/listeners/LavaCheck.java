package me.birajrai.skyblockredefined.listeners;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import me.birajrai.skyblockredefined.SkyBlockRedefined;
import me.birajrai.skyblockredefined.SkyBlockRedefinedAPI;
import me.birajrai.skyblockredefined.Island;
import me.birajrai.skyblockredefined.Settings;
import me.birajrai.skyblockredefined.util.Util;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author tastybento
 * 
 */
public class LavaCheck implements Listener {
    BukkitTask task;
    private final SkyBlockRedefined plugin;
    private final static boolean DEBUG = false;
    private final static List<BlockFace> FACES = Arrays.asList(BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
    private static Map<Long, Multiset<Material>> stats = new HashMap<Long, Multiset<Material>>();
    private static Map<Long, Map<Material, Double>> configChances = new HashMap<Long, Map<Material, Double>>();

    public LavaCheck(SkyBlockRedefined skyBlockRedefined) {
        plugin = skyBlockRedefined;
        stats.clear();
    }

    /**
     * Removes stone generated by lava pouring onto water
     * 
     * @param e - event
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCleanstoneGen(BlockFromToEvent e) {
        // Only do this in SkyBlock world
        if (!e.getBlock().getWorld().equals(SkyBlockRedefined.getIslandWorld())) {
            return;
        }
        // Do nothing if a new island is being created
        if (plugin.isNewIsland())
            return;
        final Block to = e.getToBlock();
        /*
		plugin.getLogger().info("From material is " + e.getBlock().toString());
		plugin.getLogger().info("To material is " + to.getType().toString());
		plugin.getLogger().info("---------------------------------");
         */
        if (Settings.acidDamage > 0) {
            if (DEBUG)
                plugin.getLogger().info("DEBUG: cleanstone gen " + e.getEventName());

            final Material prev = to.getType();
            // plugin.getLogger().info("To material was " +
            // to.getType().toString());
            plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
                @Override
                public void run() {
                    // plugin.getLogger().info("To material is after 1 tick " +
                    // to.getType().toString());
                    if ((prev.equals(Material.WATER) || prev.equals(Material.STATIONARY_WATER)) && to.getType().equals(Material.STONE)) {
                        to.setType(prev);
                        if (plugin.getServer().getVersion().contains("(MC: 1.8") || plugin.getServer().getVersion().contains("(MC: 1.7")) {
                            to.getWorld().playSound(to.getLocation(), Sound.valueOf("FIZZ"), 1F, 2F);
                        } else {
                            to.getWorld().playSound(to.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1F, 2F);
                        }
                    }
                }
            });
        }
    }

    /**
     * Magic Cobble Generator
     * @param e - event
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCobbleGen(BlockFromToEvent e){
        /*
        plugin.getLogger().info("DEBUG: " + e.getEventName());
        plugin.getLogger().info("From material is " + e.getBlock().toString());
        plugin.getLogger().info("To material is " + e.getToBlock().getType().toString());
        plugin.getLogger().info("---------------------------------");
         */
        // If magic cobble gen isnt used
        if(!Settings.useMagicCobbleGen) {
            //plugin.getLogger().info("DEBUG: no magic cobble gen");
            return;
        }

        // Only do this in ASkyBlock world
        if (!e.getBlock().getWorld().equals(SkyBlockRedefined.getIslandWorld())) {
            //plugin.getLogger().info("DEBUG: wrong world");
            return;
        }
        // Do nothing if a new island is being created
        if (plugin.isNewIsland()) {
            //plugin.getLogger().info("DEBUG: new island in creation");
            return;
        }

        // If only at spawn, do nothing if we're not at spawn
        if(Settings.magicCobbleGenOnlyAtSpawn && (!SkyBlockRedefinedAPI.getInstance().isAtSpawn(e.getBlock().getLocation()))){
            return;
        }

        final Block b = e.getBlock();
        if (b.getType().equals(Material.WATER) || b.getType().equals(Material.STATIONARY_WATER) 
                || b.getType().equals(Material.LAVA) || b.getType().equals(Material.STATIONARY_LAVA)) {
            //plugin.getLogger().info("DEBUG: From block is water or lava. To = " + e.getToBlock().getType());
            final Block toBlock = e.getToBlock();
            if (toBlock.getType().equals(Material.AIR) && generatesCobble(b, toBlock)){
                //plugin.getLogger().info("DEBUG: potential cobble gen");
                // Get island level or use default
                long l = Long.MIN_VALUE;
                Island island = plugin.getGrid().getIslandAt(b.getLocation());
                if (island != null) {
                    if (island.getOwner() != null) {	                    
                        l = plugin.getPlayers().getIslandLevel(island.getOwner());
                        //plugin.getLogger().info("DEBUG: level " + level);
                    }
                }
                final long level = l;
                // Check if cobble was generated next tick
                // Store surrounding blocks and their current material types
                final List<Block> prevBlock = new ArrayList<Block>();
                final List<Material> prevMat = new ArrayList<Material>();
                for (BlockFace face: FACES) {
                    Block r = toBlock.getRelative(face);
                    prevBlock.add(r);
                    prevMat.add(r.getType());
                    //r = toBlock.getRelative(face,2);
                    //prevBlock.add(r);
                    //prevMat.add(r.getType());
                }
                // Check if they became cobblestone next tick
                plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        Iterator<Block> blockIt = prevBlock.iterator();
                        Iterator<Material> matIt = prevMat.iterator();
                        while (blockIt.hasNext() && matIt.hasNext()) {
                            Block block = blockIt.next();
                            Material material = matIt.next();
                            if (block.getType().equals(Material.COBBLESTONE) && !block.getType().equals(material)) {
                                //plugin.getLogger().info("DEBUG: " + material + " => " + block.getType());
                                //plugin.getLogger().info("DEBUG: Cobble generated. Island level = " + level);
                                if(!Settings.magicCobbleGenChances.isEmpty()){
                                    Entry<Long,TreeMap<Double,Material>> entry = Settings.magicCobbleGenChances.floorEntry(level);
                                    double maxValue = entry.getValue().lastKey();                                    
                                    double rnd = Util.randomDouble() * maxValue;
                                    Entry<Double, Material> en = entry.getValue().ceilingEntry(rnd);
                                    //Bukkit.getLogger().info("DEBUG: " + entry.getValue().toString());
                                    //plugin.getLogger().info("DEBUG: Cobble generated. Island level = " + level);
                                    //plugin.getLogger().info("DEBUG: rnd = " + rnd + "/" + maxValue);
                                    //plugin.getLogger().info("DEBUG: material = " + en.getValue());
                                    if (en != null) {
                                        block.setType(en.getValue());
                                        // Record stats, per level
                                        if (stats.containsKey(entry.getKey())) {
                                            stats.get(entry.getKey()).add(en.getValue()); 
                                        } else {
                                            Multiset<Material> set = HashMultiset.create();
                                            set.add(en.getValue());
                                            stats.put(entry.getKey(), set);
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }


    public boolean generatesCobble(Block block, Block toBlock){
        Material mirrorID1 = (block.getType().equals(Material.WATER)) || (block.getType().equals(Material.STATIONARY_WATER)) ? Material.LAVA : Material.WATER;
        Material mirrorID2 = (block.getType().equals(Material.WATER)) || (block.getType().equals(Material.STATIONARY_WATER)) ? Material.STATIONARY_LAVA : Material.STATIONARY_WATER;
        for (BlockFace face: FACES) {
            Block r = toBlock.getRelative(face);
            if ((r.getType().equals(mirrorID1)) || (r.getType().equals(mirrorID2))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the magic cooble stone stats
     */
    public static Map<Long, Multiset<Material>> getStats() {
        return stats;
    }

    /**
     * Clears the magic cobble gen stats
     */
    public static void clearStats() {
        stats.clear();
    }

    /**
     * Store the configured chances in %
     * @param levelInt
     * @param chances
     */
    public static void storeChances(long levelInt, Map<Material, Double> chances) {
        configChances.put(levelInt, chances); 
    }
    
    /**
     * Clear the magic cobble gen chances
     */
    public static void clearChances() {
        configChances.clear();
    }

    /**
     * Return the chances for this level and material
     * @param level
     * @param material
     * @return chance, or 0 if the level or material don't exist
     */
    public static double getConfigChances(Long level, Material material) {
        double result = 0;
        //Bukkit.getLogger().info("DEBUG : requested " + level + " " + material);
        //Bukkit.getLogger().info("DEBUG : " + configChances.toString());
        if (configChances.containsKey(level) && configChances.get(level).containsKey(material)) {
            result = configChances.get(level).get(material);
        }
        //Bukkit.getLogger().info("DEBUG : result = " + result);
        return result;
    }
   
}