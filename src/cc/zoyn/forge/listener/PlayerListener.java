package cc.zoyn.forge.listener;

import cc.zoyn.forge.Forge;
import cc.zoyn.forge.dao.SkillExpDAO;
import cc.zoyn.forge.dao.SkillLevelDAO;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!SkillExpDAO.getPlayerSkillExpData().containsKey(player.getName())) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(Forge.getInstance(), () -> {
                SkillExpDAO.setSkillExp(player.getName(), 0);
                SkillLevelDAO.setSkillLevel(player.getName(), 1);

                File file = new File(Forge.getInstance().getPlayerSkillLevelDataFolder(), player.getName() + ".yml");
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
            }, 3 * 20L);
        }
    }

}
