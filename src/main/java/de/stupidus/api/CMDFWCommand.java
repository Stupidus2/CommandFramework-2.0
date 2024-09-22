package de.stupidus.api;

import de.stupidus.framework.CommandFramework;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface CMDFWCommand {

    /**
     * If implemented, works like onCommand method
     * @param commandSender Sender of command
     * @param command Sender of command
     * @param cmd Get the command string
     * @param args Arguments from the player
     * @return
     */
    boolean execute(CommandSender commandSender, Command command, String cmd, String[] args);

    /**
     * Put down everything that needs to be initialized to the beginn
     * <p>
     * For example:
     * subCommand.addChoose("name")
     */
    void initialize();

    /**
     * Set the code of a subCommand in this function
     * @param sender Sender of command
     * @param player If command executed by player, return player
     * @param command Sender of command
     * @param cmd Get the command string
     * @param args Arguments from the player
     */
    void subCommandCode(CommandSender sender,Player player, org.bukkit.command.Command command, String cmd, String[] args);

    /**
     * Get the instance of the current command
     * @return
     */
    CommandFramework getCommandFramework();

    /**
     * Set the permissions for default command method "execute"
     * @param permission Permission string
     */
    void setPermission(String permission);

    /**
     * Get the permission from default command
     * @return Permission for default command
     */
    String getPermission();
}
