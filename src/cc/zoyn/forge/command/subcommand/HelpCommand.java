package cc.zoyn.forge.command.subcommand;

import cc.zoyn.forge.command.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * @author Zoyn
 */
public class HelpCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§7====== §e[§6Forge§e] §7======");
        sender.sendMessage("§b/dz §7打开图纸界面");
        sender.sendMessage("§b/dz help §7查询帮助");
        sender.sendMessage("§b/dz reload §7重载配置文件");
    }
}