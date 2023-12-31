package me.birajrai.skyblockredefined.panels;

import me.birajrai.skyblockredefined.schematics.Schematic;
import me.birajrai.skyblockredefined.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Schematics Panel Item
 * @author tastybento
 */
public class SPItem {
    private ItemStack item;
    private List<String> description = new ArrayList<String>();
    private String heading;
    private String name;
    private String perm;
    private int slot;
    private double cost;

    /**
     * This constructor is for the default schematic/island
     * @param material
     * @param name
     * @param description
     * @param slot
     */
    public SPItem(Material material, String name, String description, int slot) {
        this.slot = slot;
        this.name = name;
        this.perm = "";
        this.heading = "";
        this.description.clear();
        item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + name);
        this.description.addAll(Util.chop(ChatColor.AQUA, description, 25));
        meta.setLore(this.description);
        item.setItemMeta(meta);
    }

    /**
     * This constructor is for schematics that will do something if chosen
     * @param schematic
     * @param slot
     */
    public SPItem(Schematic schematic, int slot) {
        this.slot = slot;
        this.name = schematic.getName();
        this.perm = schematic.getPerm();
        this.heading = schematic.getHeading();
        this.description.clear();
        this.item = new ItemStack(schematic.getIcon());
        this.item.setDurability((short)schematic.getDurability());
        this.cost = schematic.getCost();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        // This neat bit of code makes a list out of the description split by new line character
        List<String> desc = new ArrayList<String>(Arrays.asList(schematic.getDescription().split("\\|")));
        this.description.addAll(desc);
        meta.setLore(this.description);
        item.setItemMeta(meta);
    }

    public ItemStack getItem() {
        return item;
    }

    /**
     * @return the slot
     */
    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getName() {
        return name;
    }

    /**
     * @return the perm
     */
    public String getPerm() {
        return perm;
    }

    /**
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(double cost) {
        this.cost = cost;
    }


}