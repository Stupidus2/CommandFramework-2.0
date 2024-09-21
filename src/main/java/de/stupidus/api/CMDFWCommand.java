package de.stupidus.api;

import de.stupidus.framework.CommandFramework;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface CMDFWCommand {
    boolean execute(CommandSender commandSender, Command command, String cmd, String[] args);

    void subCommandInitialize();

    void subCommandCode(CommandSender sender,Player player, org.bukkit.command.Command command, String cmd, String[] args);

    CommandFramework getCommandFramework();
}
