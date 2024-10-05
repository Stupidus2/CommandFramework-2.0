package de.stupidus.api;

import org.bukkit.command.CommandSender;


public interface CMDFWSyntax {

    /**
     * Sets the syntax pattern to be displayed for a command.
     *
     * @param syntax The string pattern representing the command's syntax.
     *               This pattern will define how the command should be structured.
     *               <command> will be replaced to your command.
     *               <arg> will be replaced to your arguments.
     *               Example: "<command> <arg>"
     */
    void setSyntaxPattern(String syntax);

    /**
     * Sets a detailed syntax pattern with a headline, a bottom line, and the command syntax.
     *
     * @param headline   The headline or title to be displayed above the syntax.
     * @param bottomLine The text to be displayed below the syntax, often for notes or explanations.
     * @param syntax     The actual command syntax pattern that explains how to use the command.
     *                   Example: "<command> <arg>"
     */
    void setSyntaxPattern(String headline, String bottomLine, String syntax);

    /**
     * Adds a full command string to a list or structure for tracking or validation purposes.
     *
     * @param fullCommandString The full command string.
     */
    void addCommandString(String fullCommandString);

    /**
     * Checks if the specified full command string exists within the tracked command list.
     *
     * @param fullCommandString The full command string.
     * @return true if the command string exists, false otherwise.
     */
    boolean contains(String fullCommandString);

    /**
     * Sends the command syntax pattern to the sender.
     *
     * @param sender           The {@link org.bukkit.command.CommandSender} who will receive the syntax message.
     *                         This could be a player, console.
     * @param fullCommandString The full command string.
     */
    void sendSyntax(CommandSender sender, String fullCommandString);

    /**
     * Replaces the argument at position <code>argsLength</code> in the
     * command string.
     * @param commandName the name of the command
     * @param argsLength the required number of arguments
     * @param replacement the new value for the argument
     */
    void replaceArg(String commandName, int argsLength, String replacement);
}
