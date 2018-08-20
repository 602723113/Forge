package cc.zoyn.forge.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MakeStuffGUI {

    public static void openGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§c请放入锻造材料后关闭背包开始锻造!");
        player.closeInventory();
        player.openInventory(inventory);
    }

}
