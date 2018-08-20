package cc.zoyn.forge.model;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class Item {

    private String id;
    private Material material;
    private int data;
    private String displayName;
    private List<String> lore;

    public Item(String id, Material material, int data, String displayName, List<String> lore) {
        this.id = id;
        this.material = material;
        this.data = data;
        this.displayName = displayName;
        this.lore = lore;
    }

    public String getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public int getData() {
        return data;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemStack toItemStack(String playerName) {
        ItemStack itemStack = new ItemStack(material, 1, (short) data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore.stream()
                .map(s -> s.replace("%player%", playerName))
                .collect(Collectors.toList()));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
