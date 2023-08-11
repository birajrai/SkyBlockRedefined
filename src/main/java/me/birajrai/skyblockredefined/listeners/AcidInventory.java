package me.birajrai.skyblockredefined.listeners;

import me.birajrai.skyblockredefined.SkyBlockRedefined;
import me.birajrai.skyblockredefined.Settings;
import me.birajrai.skyblockredefined.nms.NMSAbstraction;
import me.birajrai.skyblockredefined.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tastybento
 *         This listener will check to see if a player has a water bucket and if
 *         so change it to acid bucket
 *         It also checks for interactions with water bottles
 */
public class AcidInventory implements Listener {
    private final SkyBlockRedefined plugin;
    private List<String> lore = new ArrayList<>();
    private final static boolean DEBUG = false;

    public AcidInventory(SkyBlockRedefined skyBlockRedefined) {
        plugin = skyBlockRedefined;
    }

    /**
     * This covers items in a chest, etc. inventory, then change the name then
     * 
     * @param e - event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (DEBUG)
            plugin.getLogger().info("DEBUG: " + e.getEventName());

        // plugin.getLogger().info("Inventory open event called");
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.worldName)) {
            Inventory inventory = e.getInventory();
            if (Settings.acidDamage == 0D || !Settings.acidBottle) {
                return;
            }
            // If this is the minishop - forget it
            if (inventory.getName() != null && inventory.getName().equalsIgnoreCase(plugin.myLocale(e.getPlayer().getUniqueId()).islandMiniShopTitle)) {
                return;
            }
            if (inventory.contains(Material.WATER_BUCKET)) {
                if (DEBUG)
                    plugin.getLogger().info("Inventory contains water bucket");
                ItemStack[] inv = inventory.getContents();
                for (ItemStack item : inv) {
                    if (item != null) {
                        // plugin.getLogger().info(item.toString());
                        if (item.getType() == Material.WATER_BUCKET) {
                            if (DEBUG)
                                plugin.getLogger().info("Found it!");
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(plugin.myLocale(e.getPlayer().getUniqueId()).acidBucket);
                            lore = Arrays.asList(plugin.myLocale(e.getPlayer().getUniqueId()).acidLore.split("\n"));
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                        }
                    }
                }
            } else if (inventory.contains(Material.POTION)) {
                if (DEBUG)
                    plugin.getLogger().info("Inventory contains water bottle");
                ItemStack[] inv = inventory.getContents();
                for (ItemStack item : inv) {
                    if (item != null) {
                        //plugin.getLogger().info(item.toString());
                        if (item.getType() == Material.POTION) {
                            NMSAbstraction nms = null;
                            try {
                                nms = Util.checkVersion();
                            } catch (Exception ex) {
                                return;
                            }
                            if (!nms.isPotion(item)) {
                                if (DEBUG)
                                    plugin.getLogger().info("Found it!");
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName(plugin.myLocale(e.getPlayer().getUniqueId()).acidBottle);
                                lore = Arrays.asList(plugin.myLocale(e.getPlayer().getUniqueId()).acidLore.split("\n"));
                                meta.setLore(lore);
                                item.setItemMeta(meta);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * If the player filled up the bucket themselves
     * 
     * @param e - event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBucketFill(PlayerBucketFillEvent e) {
        if (DEBUG)
            plugin.getLogger().info("Player filled the bucket");
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.worldName)) {
            if (DEBUG)
                plugin.getLogger().info("Correct world");
            if (Settings.acidDamage > 0D && Settings.acidBottle) {
                ItemStack item = e.getItemStack();
                if (item.getType().equals(Material.WATER_BUCKET) || item.getType().equals(Material.POTION)) {
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(plugin.myLocale(e.getPlayer().getUniqueId()).acidBucket);
                    lore = Arrays.asList(plugin.myLocale(e.getPlayer().getUniqueId()).acidLore.split("\n"));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
            }
        }
    }

    /**
     * Checks to see if a player is drinking acid
     * 
     * @param e - event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onWaterBottleDrink(final PlayerItemConsumeEvent e) {
        if (Settings.acidDamage == 0D || !Settings.acidBottle)
            return;
        if (DEBUG)
            plugin.getLogger().info("DEBUG: " + e.getEventName());
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.worldName) && e.getItem().getType().equals(Material.POTION)) {
            if (DEBUG)
                plugin.getLogger().info(e.getEventName() + " called for " + e.getItem().getType());
            NMSAbstraction nms = null;
            try {
                nms = Util.checkVersion();
            } catch (Exception ex) {
                return;
            }
            if (!nms.isPotion(e.getItem())) {
                plugin.getLogger().info(e.getPlayer().getName() + " " + plugin.myLocale().drankAcidAndDied);
                if (!Settings.muteDeathMessages) {
                    for (Player p : plugin.getServer().getOnlinePlayers()) {
                        Util.sendMessage(p, e.getPlayer().getName() + " " + plugin.myLocale(p.getUniqueId()).drankAcid);
                    }
                }
                final ItemStack item = new ItemStack(Material.GLASS_BOTTLE);
                e.getPlayer().setItemInHand(item);
                e.getPlayer().setHealth(0D);
                e.setCancelled(true);
            }
        }
    }

    /**
     * This event makes sure that any acid bottles become potions without the
     * warning
     * 
     * @param e - event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBrewComplete(final BrewEvent e) {
        if (DEBUG)
            plugin.getLogger().info("DEBUG: " + e.getEventName());

        if (e.getBlock().getWorld().getName().equalsIgnoreCase(Settings.worldName)) {
            //if (Settings.acidBottle && Settings.acidDamage>0 && e.getBlock().getWorld().getName().equalsIgnoreCase(Settings.worldName)) {

            plugin.getLogger().info("DEBUG: Brew Event called");
            BrewerInventory inv = e.getContents();
            int i = 0;
            for (ItemStack item : inv.getContents()) {
                if (item != null) {
                    // Remove lore
                    ItemMeta meta = item.getItemMeta();
                    plugin.getLogger().info("DEBUG: " + meta.getDisplayName());
                    meta.setDisplayName(null);
                    inv.setItem(i, item);
                }
                i++;
            }
        }
    }

    /**
     * Event that covers filling a bottle
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onWaterBottleFill(final PlayerInteractEvent e) {
        if (DEBUG)
            plugin.getLogger().info("DEBUG: " + e.getEventName());

        Player player = e.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase(Settings.worldName))
            return;
        if (Settings.acidDamage == 0D || !Settings.acidBottle)
            return;
        if (!player.getItemInHand().getType().equals(Material.GLASS_BOTTLE)) {
            return;
        }
        // plugin.getLogger().info(e.getEventName() + " called");
        // Look at what the player was looking at
        BlockIterator iter = new BlockIterator(player, 10);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR)
                continue;
            break;
        }
        // plugin.getLogger().info(lastBlock.getType().toString());
        if (lastBlock.getType().equals(Material.WATER) || lastBlock.getType().equals(Material.STATIONARY_WATER)
                || lastBlock.getType().equals(Material.CAULDRON)) {
            // They *may* have filled a bottle with water
            // Check inventory for POTIONS in a tick
            plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
                @Override
                public void run() {
                    // plugin.getLogger().info("Checking inventory");
                    PlayerInventory inv = e.getPlayer().getInventory();
                    if (inv.contains(Material.POTION)) {
                        // plugin.getLogger().info("POTION in inventory");
                        // They have a POTION of some kind in inventory
                        int i = 0;
                        for (ItemStack item : inv.getContents()) {
                            if (item != null) {
                                // plugin.getLogger().info(i + ":" +
                                // item.getType().toString());
                                if (item.getType().equals(Material.POTION)) {
                                    NMSAbstraction nms = null;
                                    try {
                                        nms = Util.checkVersion();
                                    } catch (Exception ex) {
                                        return;
                                    }
                                    if (!nms.isPotion(item)) {
                                        // plugin.getLogger().info("Water bottle found!");
                                        ItemMeta meta = item.getItemMeta();
                                        meta.setDisplayName(plugin.myLocale(e.getPlayer().getUniqueId()).acidBottle);
                                        // ArrayList<String> lore = new
                                        // ArrayList<String>(Arrays.asList("Poison",
                                        // "Beware!", "Do not drink!"));
                                        meta.setLore(lore);
                                        item.setItemMeta(meta);
                                        inv.setItem(i, item);
                                    }
                                }
                            }
                            i++;
                        }
                    }
                }
            });
        }

    }
}