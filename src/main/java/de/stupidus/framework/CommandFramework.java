package de.stupidus.framework;

import de.stupidus.command.syntax.Syntax;
import de.stupidus.framework.initializer.Initializer;
import de.stupidus.framework.initializer.MessageInitializer;
import de.stupidus.messages.Messages;
import de.stupidus.command.command.Command;
import de.stupidus.command.command.CommandManager;
import de.stupidus.messages.Message;
import de.stupidus.messages.Translator;
import de.stupidus.sound.CommandSound;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class CommandFramework {

    private static Message message;
    private static Initializer initializer;
    private static Syntax syntax;
    private static HashMap<Messages, Translator> messages = new HashMap<>();
    private static CommandSound commandSound;
    private CommandManager commandManager;
    private static final List<Command> commandName = new ArrayList<>();
    public CommandFramework() {}

    //Static methods

    public static Initializer getInitializer() {
        if (initializer == null) {
            return initializer = new Initializer();
        }
        return initializer;
    }

    public static Message getMessages() {
        if (message == null) {
            message = new Message(messages);
            MessageInitializer.messageInit();
        }
        return message;
    }
    public static Syntax getSyntax() {
        if (syntax == null) {
            return syntax = new Syntax();
        }
        return syntax;
    }
    public static CommandSound getCommandSound() {
        if (commandSound == null) {
            return commandSound = new CommandSound();
        }
        return commandSound;
    }
    public static List<Command> getCommands() {
        return commandName;
    }
    public static Command getCommand(String nameCommand) {
        for (Command command : commandName) {
            if (command.getName().equalsIgnoreCase(nameCommand)) {
                return command;
            }
        }
        return null;
    }

    public void removeCommandChoose(String commandName, String chooseToDelete) {
        for (Command command : CommandFramework.getCommands()) {
            if (!command.getName().equalsIgnoreCase(commandName)) continue;

            for (SubCommand subCommand : command.getSubCommands()) {
                for (String subCommandName : subCommand.getNameList().keySet()) {
                    if (!subCommandName.equalsIgnoreCase(chooseToDelete)) {
                        subCommand.removeChoose(chooseToDelete);
                        break;
                    }
                }
            }
        }
    }


    //Non-static methods

    public CommandManager getCommandManager() {
        if (commandManager == null) {
            commandManager = new CommandManager();
        }
        return commandManager;
    }
    public void addCommand(Command command) {
        commandName.add(command);
    }
}
