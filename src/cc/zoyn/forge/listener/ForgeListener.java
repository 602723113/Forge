package cc.zoyn.forge.listener;

import cc.zoyn.forge.Forge;
import cc.zoyn.forge.dao.SkillExpDAO;
import cc.zoyn.forge.dao.SkillLevelDAO;
import cc.zoyn.forge.gui.BluePrintGUI;
import cc.zoyn.forge.gui.MakeStuffGUI;
import cc.zoyn.forge.manager.BluePrintManager;
import cc.zoyn.forge.manager.ItemManager;
import cc.zoyn.forge.manager.PlayerSkillLevelManager;
import cc.zoyn.forge.model.BluePrint;
import cc.zoyn.forge.model.Item;
import cc.zoyn.forge.model.MakingStuff;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ForgeListener implements Listener {

    private static Map<String, String> selectBluePrints = Maps.newHashMap();

    public static Map<String, String> getSelectBluePrint() {
        return selectBluePrints;
    }

    private static boolean isMakeStuff(ItemStack itemStack) {
        return itemStack != null && !itemStack.getType().equals(Material.AIR) && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName();
    }

    private static boolean isEmpty(Inventory inventory) {
        List<ItemStack> itemStacks = Lists.newArrayList(inventory.getContents());
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                return false;
            }
        }
        return true;
    }

    private static List<ItemStack> getInventoryNotNullItems(Inventory inventory) {
        List<ItemStack> itemStacks = Lists.newArrayList();
        if (!isEmpty(inventory)) {
            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                    itemStacks.add(itemStack);
                }
            }
        }
        return itemStacks;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getRawSlot() < 0) {
            return;
        }
        Inventory inv = event.getInventory();
        String title = inv.getTitle();
        Player player = (Player) event.getWhoClicked();
        if (title.equals("§c§l选择你想要锻造的图纸!")) {
            event.setCancelled(true);
            if (event.getRawSlot() > 54) {
                event.setCancelled(true);
                return;
            }
            if (inv.getItem(event.getRawSlot()) == null || inv.getItem(event.getRawSlot()).getType().equals(Material.AIR)) {
                return;
            }
            selectBluePrints.put(player.getName(), inv.getItem(event.getRawSlot()).getItemMeta().getDisplayName());
            MakeStuffGUI.openGUI(player);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inv = event.getInventory();
        if (inv.getTitle().equals("§c请放入锻造材料后关闭背包开始锻造!")) {
            List<ItemStack> items = getInventoryNotNullItems(inv);
            if (isEmpty(inv)) {
                player.sendMessage("§c请放入锻造材料!");
                return;
            }
            if (selectBluePrints.containsKey(player.getName())) {
                BluePrint selectBluePrint = BluePrintManager.getBluePrintByDisplayName("" + selectBluePrints.get(player.getName())).get();
                int level = SkillLevelDAO.getSkillLevel(player.getName());
                if (selectBluePrint.getNeedSkillLevel() > level) {
                    player.sendMessage("§c你的锻造等级还不能锻造该物品哦!");
                    return;
                }


                /* 材料名检查开始 */
                if (items.size() < selectBluePrint.getMakeStuff().size()) {
                    // 返还物品
                    items.forEach(itemStack -> player.getInventory().addItem(itemStack));
                    player.sendMessage("§c请放入该图纸所有所需的锻造材料!");
                    return;
                }

                for (ItemStack item : items) {
                    if (item == null) {
                        continue;
                    }
                    if (!isMakeStuff(item)) {
                        // 返还物品
                        items.forEach(itemStack -> player.getInventory().addItem(itemStack));
                        player.sendMessage("§c请不要放入跟锻造材料无关的物品, 以避免带来不必要的损失!");
                        return;
                    }

                    List<String> makeStuffNames = selectBluePrint.getMakeStuff().stream().map(MakingStuff::getName).collect(Collectors.toList());
                    if (!makeStuffNames.contains(item.getItemMeta().getDisplayName())) {
                        // 返还物品
                        items.forEach(itemStack -> player.getInventory().addItem(itemStack));
                        player.sendMessage("§c请不要放入跟锻造材料无关的物品, 以避免带来不必要的损失!");
                        return;
                    }
                }
                /* 材料名检查结束 */


                // 检查所有锻造材料
                for (int i = 0; i < items.size(); i++) {
                    ItemStack item = items.get(i);
                    MakingStuff stuff = selectBluePrint.getMakingStuffByName(item.getItemMeta().getDisplayName());
                    if (item.getAmount() < stuff.getAmount()) {
                        // 返还物品
                        items.forEach(itemStack -> player.getInventory().addItem(itemStack));
                        player.sendMessage("§a锻造材料: " + stuff.getName() + " 的数量不够!");
                        break;
                    }
                    if (i == items.size() - 1) {
                        // 多余物品的返还
                        for (ItemStack itemStack : items) {
                            MakingStuff makingStuff = selectBluePrint.getMakingStuffByName(itemStack.getItemMeta().getDisplayName());
                            if (itemStack.getAmount() > makingStuff.getAmount()) {
                                itemStack.setAmount(itemStack.getAmount() - makingStuff.getAmount());
                                player.getInventory().addItem(itemStack);
                            }
                        }

                        // 现在所有的材料都是ok的
                        // 可以进行锻造操作
                        player.sendMessage("§a正在锻造中...");

                        int itemId = selectBluePrint.getOutputItem();
                        double exp = selectBluePrint.getOutputExp();
                        Item itemObj = ItemManager.getItems().get("" + itemId);
                        ItemStack itemStack = itemObj.toItemStack(player.getName());
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setLore(itemMeta.getLore()
                                .stream()
                                .map(s -> s.replace("%player%", player.getName()))
                                .collect(Collectors.toList()));
                        itemStack.setItemMeta(itemMeta);

                        player.getInventory().addItem(itemStack);
                        SkillExpDAO.addSkillExp(player.getName(), exp);
                        player.sendMessage("§a锻造成功! 除此之外你还获得了 " + exp + " 点锻造经验!");
                        return;
                    }
                }
            } else {
                player.sendMessage("§c发生了一个内部错误! 请重新选择你需要锻造的图纸!");
                player.closeInventory();
                Bukkit.getScheduler().runTaskLater(Forge.getInstance(), () -> BluePrintGUI.openGUI(player), 40L);
            }
        }
    }

}
