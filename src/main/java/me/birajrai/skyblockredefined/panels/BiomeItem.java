package me.birajrai.skyblockredefined.panels;

import me.birajrai.skyblockredefined.Settings;
import me.birajrai.skyblockredefined.util.Util;
import me.birajrai.skyblockredefined.util.VaultHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BiomeItem {
    private ItemStack item;
    private int slot;
    private double price;
    // private String description;
    private String name;
    private boolean confirm;
    private Biome biome;

    /**
     * @param slot
     * @param cost
     * @param description
     * @param name
     */
    public BiomeItem(Material material, int slot, double cost, String description, String name, boolean confirm, Biome biome) {
        this.slot = slot;
        this.price = cost;
        // this.description = description;
        this.name = name;
        this.confirm = confirm;
        this.biome = biome;
        // Make the item(s)
        item = new ItemStack(material);
        // Set the description and price
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + name);
        List<String> lore = new ArrayList<String>();
        if (description.contains("|") || description.length() <= 20) {
            // Split pip character requires escaping it
            String[] split = description.split("\\|");
            lore = new ArrayList<String>(Arrays.asList(split));
        } else {
            lore = Util.chop(ChatColor.YELLOW, description, 20);
        }
        // Create price
        if (Settings.useEconomy && cost > 0D) {
            lore.add(VaultHelper.econ.format(cost));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    /**
     * @return the item
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * @return the slot
     */
    public int getSlot() {
        return slot;
    }

    /**
     * @return the confirm
     */
    public boolean isConfirm() {
        return confirm;
    }

    /**
     * @return the biome
     */
    public Biome getBiome() {
        return biome;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param biome
     *            the biome to set
     */
    public void setBiome(Biome biome) {
        this.biome = biome;
    }

}
