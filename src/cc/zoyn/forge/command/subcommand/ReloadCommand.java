package cc.zoyn.forge.command.subcommand;

import cc.zoyn.forge.Forge;
import cc.zoyn.forge.command.SubCommand;
import cc.zoyn.forge.manager.BluePrintManager;
import cc.zoyn.forge.manager.ItemManager;
import cc.zoyn.forge.manager.PlayerSkillLevelManager;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§c权限不足!");
            return;
        }
        Forge.getInstance().reloadConfig();
        Forge.getInstance().reloadItemConfig();
        Forge.getInstance().reloadBlueprintConfig();

        BluePrintManager.reloadBluePrints();
        ItemManager.reloadItems();
        // 先保存数据, 然后再读取
        PlayerSkillLevelManager.savePlayerSkillLevel();
        PlayerSkillLevelManager.reloadPlayerSkillLevel();
        sender.sendMessage("§a重载成功!");
    }
}
