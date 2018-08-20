package cc.zoyn.forge.command;

import cc.zoyn.forge.command.subcommand.HelpCommand;
import cc.zoyn.forge.command.subcommand.ReloadCommand;
import cc.zoyn.forge.gui.BluePrintGUI;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Zoyn
 */
public class CommandHandler implements CommandExecutor {

    public static Map<String, SubCommand> commandMap = Maps.newHashMap();

    /**
     * Initialize all sub commands
     */
    public CommandHandler() {
        registerCommand("help", new HelpCommand());
        registerCommand("reload", new ReloadCommand());
    }

    private void registerCommand(String commandName, SubCommand subCommand) {
        if (commandMap.containsKey(commandName)) {
            Bukkit.getLogger().warning("duplicate add command!");
        }
        commandMap.put(commandName, subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§f控制台禁止输入此命令!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            BluePrintGUI.openGUI(player);
            return true;
        }
        if (!commandMap.containsKey(args[0].toLowerCase())) {
            sender.sendMessage("§c未知指令!");
            return true;
        }
        // args[0] ---> SubCommand name
        SubCommand subCommand = commandMap.get(args[0].toLowerCase());
        subCommand.execute(sender, args);
        return true;
    }
}
