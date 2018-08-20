package cc.zoyn.forge.manager;

import cc.zoyn.forge.Forge;
import cc.zoyn.forge.model.BluePrint;
import cc.zoyn.forge.model.MakingStuff;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 该类用于管理图纸
 */
public class BluePrintManager {

    private static List<BluePrint> bluePrints = Lists.newArrayList();
    private static FileConfiguration blueprintConfig = Forge.getInstance().getBlueprintConfig();

    public static List<BluePrint> getBluePrints() {
        return bluePrints;
    }

    public static Optional<BluePrint> getBluePrintById(String id) {
        return bluePrints.stream().filter(bluePrint -> bluePrint.getId().equals(id)).findAny();
    }

    public static Optional<BluePrint> getBluePrintByDisplayName(String displayName) {
        return bluePrints.stream().filter(bluePrint -> bluePrint.getDisplayName().equals(displayName)).findAny();
    }

    public static void reloadBluePrints() {
        bluePrints.clear();

        // 父键为 BluePrint
        ConfigurationSection bluePrint = blueprintConfig.getConfigurationSection("BluePrint");
        Set<String> bluePrintIds = bluePrint.getKeys(false); // 所有的图纸id
        bluePrintIds.forEach(id -> {
            String name = bluePrint.getString(id + ".name");
            int needSkillLevel = bluePrint.getInt(id + ".needSkillLevel");
            // 锻造材料读取
            List<MakingStuff> stuffs = Lists.newArrayList();
            Set<String> makingStuffIds = bluePrint.getConfigurationSection(id + ".makeStuff").getKeys(false);
            makingStuffIds.forEach(stuffId -> {
                String stuffName = bluePrint.getString(id + ".makeStuff." + stuffId + ".name").replace("&", "§");
                int amount = bluePrint.getInt(id + ".makeStuff." + stuffId + ".amount");
                MakingStuff stuff = new MakingStuff(stuffName, amount);
                stuffs.add(stuff);
            });
            int outputItem = bluePrint.getInt(id + ".forgingOutput");
            double outputExp = bluePrint.getDouble(id + ".outputExp");
            String permission = bluePrint.getString(id + ".permission");


            // 以下为图纸外观配置加载
            Material material = Material.getMaterial(bluePrint.getInt(id + ".material"));
            int data = bluePrint.getInt(id + ".data");
            String displayName = bluePrint.getString(id + ".displayName").replace("&", "§");
            List<String> lore = bluePrint.getStringList(id + ".lore")
                    .stream()
                    .map(s -> s.replace("&", "§"))
                    .collect(Collectors.toList());

            BluePrint bluePrintObj = new BluePrint(id, name, needSkillLevel, stuffs, outputItem, outputExp, permission, material, data, displayName, lore);
            bluePrints.add(bluePrintObj);
            Bukkit.getConsoleSender().sendMessage("§8[§6Forge§8]§f加载图纸: " + displayName + " §f成功!");
        });
    }
}
