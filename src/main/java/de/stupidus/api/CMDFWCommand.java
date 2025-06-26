package de.stupidus.api;

import de.stupidus.framework.CommandFramework;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public interface CMDFWCommand {

    /**
     * Executes the main logic for the command.
     *
     * @param commandSender The sender of the command (could be console or player).
     * @param player The player executing the command (null if sent from console).
     * @param command The actual command being executed.
     * @param cmd The command string (e.g. "panda").
     * @param args The arguments provided by the player.
     * @return true if the command was successfully executed, false otherwise.
     */
    boolean execute(CommandSender commandSender, Player player, Command command, String cmd, String[] args);

    /**
     * Initializes the command's necessary configurations.
     * <p>
     * This method is used to set up subcommands, choose options, and other
     * initial settings that are required at the beginning of the command's lifecycle.
     * <p>
     * Example usage:
     * <pre>
     * subCommand.addChoose("optionName");
     * </pre>
     */
    void initialize();

    /**
     * Defines the logic for subcommands.
     *
     * @param sender The sender of the command.
     * @param player The player executing the subcommand (null if not applicable).
     * @param command The command being executed.
     * @param cmd The string representation of the command.
     * @param args The arguments provided by the player.
     */
    void subCommandCode(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args);

    /**
     * Gets the instance of the current command framework.
     *
     * @return The {@link CommandFramework} instance managing this command.
     */
    CommandFramework getCommandFramework();

    /**
     * Sets the permission required to use the command.
     *
     * @param permission The permission string (e.g. "plugin.command.use").
     */
    void setCommandPermission(String permission);

    /**
     * Adds additional settings to the command.
     *
     * @param settings The {@link Settings} object defining the configuration to add.
     */
    void addSetting(Settings settings);

    /**
     * Retrieves all settings applied to this command.
     *
     * @return A list of {@link Settings} applied to this command.
     */
    List<Settings> getSettings();

    /**
     * Gets the permission string for this command.
     *
     * @return The permission required to execute the command.
     */
    String getPermission();

    /**
     * Gets the name of the command.
     *
     * @return The name of the command.
     */
    String getName();
}
