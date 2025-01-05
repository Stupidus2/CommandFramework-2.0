package de.stupidus.api;

import de.stupidus.command.others.Code;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface CMDFWSubCommand {

    /**
     * Adds an alternative way to execute a subcommand, providing a different string to trigger the subcommand.
     *
     * @param name The alternative string (or phrase) that can be used to execute the subcommand.
     *             Example: "spawn panda" or "panda".
     */
    void addChoose(String name);

    /**
     * Adds an alternative way to execute a subcommand, providing a different string to trigger the subcommand.
     *
     * @param name The alternative string (or phrase) that can be used to execute the subcommand.
     *             Example: "spawn panda" or "panda".
     *
     * @param uuid Player who can execute the command
     */
    void addChoose(String name, UUID uuid);

    /**
     * Adds specific settings or parameters to the subcommand.
     * Settings may define additional command behaviors or constraints.
     *
     * @param settings The settings to be applied to the subcommand.
     *                 Example: {@link Settings#PLAYER}, {@link Settings#COMMAND_SYNTAX}.
     */
    void addSetting(Settings settings);

    /**
     * Sets the required permission for executing the subcommand.
     *
     * @param permission The permission node required to run the subcommand.
     *                   Example: "myplugin.use.panda".
     */
    void setPermission(String permission);

    /**
     * FOR REMOVE
     */
    @Deprecated
    void filterChoose();

    /**
     * Checks whether the subcommand contains a variable argument (e.g., placeholders like <[duration]>).
     * This is useful when commands expect dynamic input such as a number or player name.
     *
     * @return true if the subcommand contains a variable argument, false otherwise.
     */
    boolean containsVarArg();

    /**
     * Sets the code that should be executed when the subcommand is called.
     * This method is deprecated in favor of {@link #setCode(Runnable)}.
     *
     * @param codeToExecute The {@link Code} object representing the actions to be executed.
     * @deprecated Use {@link #setCode(Runnable)} instead for better compatibility with future versions.
     */
    @Deprecated
    void setCode(Code codeToExecute);

    /**
     * Sets the code that will be executed when the subcommand is invoked.
     *
     * @param code A {@link Runnable} containing the logic to be executed.
     *             Example:
     *             <pre>
     *             subCommand.setCode(() -> {
     *                 yourFunction(player, args);
     *             });
     *             </pre>
     */
    void setCode(Runnable code);

    /**
     * Retrieves the code object associated with the subcommand.
     *
     * @return The {@link Code} object that represents the executable logic of the subcommand.
     */
    Code getCode();

    /**
     * Retrieves the {@link Runnable} code that will be executed when the subcommand is invoked.
     *
     * @return The runnable code that will execute when the subcommand is triggered.
     */
    Runnable getRunnableCode();

    /**
     * Retrieves a list of all alternative names or command strings for the subcommand.
     * These names can be used to execute the subcommand.
     *
     * @return A list of strings representing all possible ways to trigger the subcommand.
     *         Example: ["spawn panda", "panda"].
     */
    HashMap<String, List<UUID>> getNameList();

    /**
     * Retrieves the permission required to execute the subcommand.
     *
     * @return A string representing the permission node for the subcommand.
     */
    String getPermission();

    /**
     * Retrieves the list of settings associated with the subcommand.
     *
     * @return A list of {@link Settings} objects that define various behaviors for the subcommand.
     */
    List<Settings> getSettings();

    /**
     * Checks whether the subcommand is tab-completable, meaning it will show suggestions during tab completion.
     *
     * @return true if the subcommand supports tab completion, false otherwise.
     */
    boolean getTabCompletable();
}
