package cc.zoyn.forge;

import cc.zoyn.forge.command.CommandHandler;
import cc.zoyn.forge.hook.PlaceholderAPIHook;
import cc.zoyn.forge.listener.ForgeListener;
import cc.zoyn.forge.listener.PlayerListener;
import cc.zoyn.forge.manager.BluePrintManager;
import cc.zoyn.forge.manager.ItemManager;
import cc.zoyn.forge.manager.PlayerSkillLevelManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Forge extends JavaPlugin {

    private static Forge instance;
    private static final Random RANDOM = new Random();
    private FileConfiguration blueprintConfig;
    private FileConfiguration itemConfig;
    private File playerSkillLevelDataFolder = new File(getDataFolder() + "\\PlayerData\\");

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveResource("blueprint.yml", false);
        saveResource("item.yml", false);
        File blueprintFile = new File(getDataFolder(), "blueprint.yml");
        File itemFile = new File(getDataFolder(), "item.yml");
        blueprintConfig = YamlConfiguration.loadConfiguration(blueprintFile);
        itemConfig = YamlConfiguration.loadConfiguration(itemFile);
        // 玩家锻造等级经验文件夹
        if (!playerSkillLevelDataFolder.exists()) {
            playerSkillLevelDataFolder.mkdirs();
        }

        Bukkit.getConsoleSender().sendMessage("§8[§6Forge§8]§f开始加载所有图纸...");
        BluePrintManager.reloadBluePrints();
        Bukkit.getConsoleSender().sendMessage("§8[§6Forge§8]§f开始加载所有物品...");
        ItemManager.reloadItems();
        Bukkit.getConsoleSender().sendMessage("§8[§6Forge§8]§f开始加载玩家数据...");
        PlayerSkillLevelManager.reloadPlayerSkillLevel();

        CommandHandler handler = new CommandHandler();
        Bukkit.getPluginCommand("dz").setExecutor(handler);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new ForgeListener(), this);

        // 挂钩PlaceholderAPI
        boolean isEnable = getServer().getPluginManager().getPlugin("PlaceholderAPI").isEnabled();
        if (isEnable) {
            Bukkit.getConsoleSender().sendMessage("§8[§6Forge§8]§f正在检查PlaceholderAPI加载情况: §a加载成功!");
            Bukkit.getConsoleSender().sendMessage("§8[§6Forge§8]§f正在挂钩至PlaceholderAPI...");
            boolean hookSuccess = new PlaceholderAPIHook(instance).hook();
            if (hookSuccess) {
                Bukkit.getConsoleSender().sendMessage("§8[§6Forge§8]§a挂钩至PlaceholderAPI成功!");
            } else {
                Bukkit.getConsoleSender().sendMessage("§8[§6Forge§8]§c无法挂钩至PlaceholderAPI!");
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("§8[§6Forge§8]§f正在检查PlaceholderAPI加载情况: §c加载失败!");
        }
    }

    @Override
    public void onDisable() {
        PlayerSkillLevelManager.savePlayerSkillLevel();
    }

    public static Forge getInstance() {
        return instance;
    }

    public Random getRANDOM() {
        return RANDOM;
    }

    public FileConfiguration getBlueprintConfig() {
        return blueprintConfig;
    }

    public void reloadBlueprintConfig() {
        File blueprintFile = new File(getDataFolder(), "blueprint.yml");
        try {
            blueprintConfig.load(blueprintFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void reloadItemConfig() {
        File itemFile = new File(getDataFolder(), "item.yml");
        try {
            itemConfig.load(itemFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getItemConfig() {
        return itemConfig;
    }

    public File getPlayerSkillLevelDataFolder() {
        return playerSkillLevelDataFolder;
    }

}
