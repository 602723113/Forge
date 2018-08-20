package cc.zoyn.forge.dao;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 此类用于保存用户的锻造等级数据
 */
public class SkillLevelDAO {

    private static Map<String, Integer> skillLevelData = Maps.newHashMap();

    public static Map<String, Integer> getSkillLevelData() {
        return skillLevelData;
    }

    public static void setSkillLevel(String playerName, int level) {
        skillLevelData.put(playerName, level);
    }

    public static int getSkillLevel(String playerName) {
        if (skillLevelData.containsKey(playerName)) {
            return skillLevelData.get(playerName);
        } else {
            setSkillLevel(playerName, 1);
            return 1;
        }
    }

    public static void addSkillLevel(String playerName, int level) {
        if (skillLevelData.containsKey(playerName)) {
            setSkillLevel(playerName, getSkillLevel(playerName) + level);
        } else {
            setSkillLevel(playerName, level);
        }
    }
}
