package cc.zoyn.forge.gui;

import cc.zoyn.forge.listener.ForgeListener;
import cc.zoyn.forge.manager.BluePrintManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BluePrintGUI {

    public static void openGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§c§l选择你想要锻造的图纸!");

        // 增加玩家已习得的所有图纸
        BluePrintManager.getBluePrints().forEach(bluePrint -> {
            if (player.hasPermission(bluePrint.getPermission())) {
                inventory.addItem(bluePrint.toItemStack());
            }
        });

        ForgeListener.getSelectBluePrint().remove(player.getName());
        player.closeInventory();
        player.openInventory(inventory);
    }

}
