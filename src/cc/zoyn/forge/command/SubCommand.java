package cc.zoyn.forge.command;

import org.bukkit.command.CommandSender;

/**
 * @author Zoyn
 */
@FunctionalInterface
public interface SubCommand {

    void execute(CommandSender sender, String[] args);

}
