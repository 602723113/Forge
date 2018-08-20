package cc.zoyn.forge.hook;

import cc.zoyn.forge.dao.SkillExpDAO;
import cc.zoyn.forge.dao.SkillLevelDAO;
import cc.zoyn.forge.manager.PlayerSkillLevelManager;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderAPIHook extends EZPlaceholderHook {

    public PlaceholderAPIHook(Plugin plugin) {
        super(plugin, "forge");
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {
        switch (s) {
            case "level": {
                int level = SkillLevelDAO.getSkillLevel(player.getName());
                return "" + level;
            }
            case "currentExp":
                double exp = SkillExpDAO.getSkillExp(player.getName());
                return "" + exp;
            case "maxExp": {
                int level = SkillLevelDAO.getSkillLevel(player.getName());
                double nextLevelCostExp = PlayerSkillLevelManager.getALevelToNextLevelCostExp(level);
                return "" + nextLevelCostExp;
            }
        }
        return "在获取数据时出现了错误!";
    }
}
