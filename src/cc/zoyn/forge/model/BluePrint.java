package cc.zoyn.forge.model;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 表示一张图纸
 */
public class BluePrint {

    private String id;
    private String name;
    private int needSkillLevel;
    /**
     * 制作材料
     */
    private List<MakingStuff> makeStuff;
    private int outputItem;
    private double outputExp;
    private String permission;

    // 以下为图纸外观设置
    private Material material;
    private int data;
    private String displayName;
    private List<String> lore;

    public BluePrint(String id, String name, int needSkillLevel, List<MakingStuff> makeStuff, int outputItem, double outputExp, String permission, Material material, int data, String displayName, List<String> lore) {
        this.id = id;
        this.name = name;
        this.needSkillLevel = needSkillLevel;
        this.makeStuff = makeStuff;
        this.outputItem = outputItem;
        this.outputExp = outputExp;
        this.permission = permission;
        this.material = material;
        this.data = data;
        this.displayName = displayName;
        this.lore = lore;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPermission() {
        return permission;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNeedSkillLevel() {
        return needSkillLevel;
    }

    public void setNeedSkillLevel(int needSkillLevel) {
        this.needSkillLevel = needSkillLevel;
    }

    public List<MakingStuff> getMakeStuff() {
        return makeStuff;
    }

    public MakingStuff getMakingStuffByName(String name) {
        List<String> makeStuffNames = getMakeStuff().stream().map(MakingStuff::getName).collect(Collectors.toList());
        if (makeStuffNames.contains(name)) {
            return getMakeStuff().stream().filter(stuff -> stuff.getName().equals(name)).findAny().get();
        }
        return null;
    }

    public void setMakeStuff(List<MakingStuff> makeStuff) {
        this.makeStuff = makeStuff;
    }

    public int getOutputItem() {
        return outputItem;
    }

    public void setOutputItem(int outputItem) {
        this.outputItem = outputItem;
    }

    public double getOutputExp() {
        return outputExp;
    }

    public void setOutputExp(double outputExp) {
        this.outputExp = outputExp;
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(material, 1, (short) data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
