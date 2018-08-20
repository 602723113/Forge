package cc.zoyn.forge.manager;

import cc.zoyn.forge.Forge;
import cc.zoyn.forge.model.Item;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemManager {

    // 其对应关系为: 物品id -> 物品
    private static Map<String, Item> items = Maps.newHashMap();
    private static FileConfiguration itemConfig = Forge.getInstance().getItemConfig();

    public static Map<String, Item> getItems() {
        return items;
    }

    public static Item getItemById(String id) {
        return items.getOrDefault(id, null);
    }

    public static Optional<Item> getItemByDisplayName(String displayName) {
        return items.values().stream().filter(item -> item.getDisplayName().equals(displayName)).findAny();
    }

    public static void reloadItems() {
        items.clear();

        ConfigurationSection item = itemConfig.getConfigurationSection("Item");
        Set<String> itemIds = item.getKeys(false);
        itemIds.forEach(id -> {
            Material material = Material.getMaterial(item.getInt(id + ".material"));
            int data = item.getInt(id + ".data");
            String displayName = item.getString(id + ".displayName").replace("&", "§");
            List<String> lore = item.getStringList(id + ".lore")
                    .stream()
                    .map(s -> s.replace("&", "§"))
                    .collect(Collectors.toList());

            Item itemObj = new Item(id, material, data, displayName, lore);
            items.put(id, itemObj);
            Bukkit.getConsoleSender().sendMessage("§8[§6Forge§8]§f加载物品: " + displayName + " §f成功!");
        });

    }
}
