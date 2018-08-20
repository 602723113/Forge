package cc.zoyn.forge.dao;

import cc.zoyn.forge.Forge;
import cc.zoyn.forge.manager.PlayerSkillLevelManager;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 该类用于存放用户的锻造经验.
 * 指的是用户当前等级, 到了升级的时候此map的数据会自动设置为0
 */
public class SkillExpDAO {

    private static Map<String, Double> playerSkillExpData = Maps.newHashMap();

    public static Map<String, Double> getPlayerSkillExpData() {
        return playerSkillExpData;
    }

    public static void setSkillExp(String playerName, double exp) {
        playerSkillExpData.put(playerName, exp);
    }

    public static double getSkillExp(String playerName) {
        if (playerSkillExpData.containsKey(playerName)) {
            return playerSkillExpData.get(playerName);
        } else {
            setSkillExp(playerName, 0D);
            return 0D;
        }
    }

    public static void addSkillExp(String playerName, double exp) {
        if (playerSkillExpData.containsKey(playerName)) {
            // 满级
            if (SkillLevelDAO.getSkillLevel(playerName) == Forge.getInstance().getConfig().getInt("maxSkillLevel")) {
                setSkillExp(playerName, PlayerSkillLevelManager.getALevelToNextLevelCostExp(Forge.getInstance().getConfig().getInt("maxSkillLevel")));
                return;
            }
            int level = SkillLevelDAO.getSkillLevel(playerName);
            double costExp = PlayerSkillLevelManager.getALevelToNextLevelCostExp(level);
            // 判断升级
            if (getSkillExp(playerName) + exp >= costExp) {
                SkillLevelDAO.addSkillLevel(playerName, 1);
                // 检查剩下的经验
                double remainExp = getSkillExp(playerName) + exp - costExp;
                if (remainExp == 0) {
                    setSkillExp(playerName, 0);
                } else {
                    // 还有剩余
                    addSkillExp(playerName, exp);
                }
            } else {
                setSkillExp(playerName, getSkillExp(playerName) + exp);
            }
        } else {
            setSkillExp(playerName, exp);
        }
    }
}
