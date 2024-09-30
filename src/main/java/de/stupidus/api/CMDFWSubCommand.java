package de.stupidus.api;

import de.stupidus.command.Code;

import java.util.List;

public interface CMDFWSubCommand {

    /**
     * Add another way to execute a subcommand
     * @param name Name for the new way
     */
    void addChoose(String name);

    /**
     * Add settings to your subCommand - Currently not implemented
     * @param settings The setting you would like to add
     */
    void addSetting(Settings settings);

    /**
     * Set a permission for the subCommand
     * @param permission Permission witch should be added to the subCommand
     */

    void setPermission(String permission);

    /**
     * Clean up all added chooses of one command (Remove chooses witch are twice)
     */
    void filterChoose();

    /**
     * Check if the subCommands contains a paramater e.g. <[duration]>
     * @return true if it contains, false if it not contains
     */
    boolean containsVarArg();

    /**
     * Set code for the subcommand
     * @param codeToExecute Create new code and then basically add the code you want to execute
     */
    void setCode(Code codeToExecute);

    /**
     * Get code of the subCommand
     * @return Code of the subCommand
     */
    Code getCode();

    /**
     * All different names for the subCommand
     * <p>
     * For example:
     * "spawn panda"
     * "panda"
     * Both expressions are spawning a panda in this example
     * <p>
     * @return List with all names
     */
    List<String> getNameList();

    /**
     * Get permission for the subCommand to execute
     * @return String with permission
     */
    String getPermission();

    /**
     * Get the list with all settings for a subCommand
     * @return Settings list
     */
    List<Settings> getSettingsList();

    /**
     * Check if the subCommand is tabCompletable
     * @return Boolean if its tabCompletable
     */
    boolean getTabCompletable();
}
