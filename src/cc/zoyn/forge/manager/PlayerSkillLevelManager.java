package cc.zoyn.forge.manager;

import cc.zoyn.forge.Forge;
import cc.zoyn.forge.dao.SkillExpDAO;
import cc.zoyn.forge.dao.SkillLevelDAO;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class PlayerSkillLevelManager {

    // 对应关系: 等级 -> 所需的经验值才能到下一级
    private static Map<Integer, Double> levelData = Maps.newHashMap();

    static {
        FileConfiguration config = Forge.getInstance().getConfig();
        ConfigurationSection skillLevel = config.getConfigurationSection("skillLevel");
        Set<String> levels = skillLevel.getKeys(false);
        for (String level : levels) {
            double exp = skillLevel.getDouble(level);
            levelData.put(Integer.parseInt(level), exp);
        }
    }

    /**
     * 使用给定的等级来获取到下一级所需的经验
     * 当等级列表内无法搜到该所需经验则返回-1
     *
     * @param level 等级
     * @return 到下一级所需的经验
     */
    public static double getALevelToNextLevelCostExp(int level) {
        if (levelData.containsKey(level)) {
            return levelData.get(level);
        } else {
            return -1;
        }
    }

    public static void reloadPlayerSkillLevel() {
        SkillExpDAO.getPlayerSkillExpData().clear();
        SkillLevelDAO.getSkillLevelData().clear();

        File folder = Forge.getInstance().getPlayerSkillLevelDataFolder();
        File[] datas = folder.listFiles();
        if (datas == null || datas.length == 0) {
            return;
        }
        for (File playerData : datas) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(playerData);
            String playerName = playerData.getName().replace(".yml", "");
            int level = config.getInt("level");
            double exp = config.getDouble("exp");

            SkillExpDAO.setSkillExp(playerName, exp);
            SkillLevelDAO.setSkillLevel(playerName, level);
            Bukkit.getConsoleSender().sendMessage("§8[§6Forge§8]§f加载玩家锻造等级数据: " + playerName + " §f成功!");
        }
    }

    public static void savePlayerSkillLevel() {
        File folder = Forge.getInstance().getPlayerSkillLevelDataFolder();
        SkillExpDAO.getPlayerSkillExpData().forEach((key, value) -> {
            File file = new File(folder, key + ".yml");
            if (file.exists()) { // 非新进干员
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("level", SkillLevelDAO.getSkillLevel(key));
                config.set("exp", value);
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else { // 新进干员
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("level", 1);
                config.set("exp", 0);
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
