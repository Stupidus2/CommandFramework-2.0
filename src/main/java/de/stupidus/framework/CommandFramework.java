package de.stupidus.framework;

import de.stupidus.Messages.Message;
import de.stupidus.api.Messages;
import de.stupidus.command.Command;
import de.stupidus.command.CommandManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class CommandFramework {
    private static Message message;
    private CommandManager commandManager;
    private static Initializer initializer;

    private Command command;
    private static HashMap<Messages, String> messages = new HashMap<>();

    private static HashMap<String, CommandExecutor> commandNamesCommandExecutor = new HashMap<>();
    private static HashMap<String, TabCompleter> commandNamesTabCompleter = new HashMap<>();

    public CommandFramework(String name, CommandExecutor commandExecutor, TabCompleter tabCompleter){
        commandNamesCommandExecutor.putIfAbsent(name, commandExecutor);
        commandNamesTabCompleter.putIfAbsent(name, tabCompleter);
    }
    public CommandFramework(){}

    //Static methods
    public static Message getMessages() {
        if(message == null) {
            message = new Message(messages);
            messageInit(message);
        }
        return message;
    }
    public static Initializer getInitializer() {
        if(initializer == null) {
            initializer = new Initializer();
        }
        return initializer;
    }
    public static HashMap<String, CommandExecutor> getAllCommandNamesCommandExecutor() {
        return commandNamesCommandExecutor;
    }
    public static HashMap<String, TabCompleter> getAllCommandNamesTabCompleter() {
        return commandNamesTabCompleter;
    }



    //Non-static methods

    public CommandManager getCommandManager() {
        if(commandManager == null) {
            commandManager = new CommandManager();
        }
        return commandManager;
    }


    private static void messageInit(Message message) {
        message.newMessage(Messages.UNKNOWN_COMMAND_NAME, ChatColor.YELLOW + "Could not find this command");
        message.newMessage(Messages.NOT_A_PLAYER, ChatColor.YELLOW + "You have to be a player to execute this command!");
        message.newMessage(Messages.MISSING_PERMISSION, ChatColor.YELLOW + "You need following permission: "+ ChatColor.RED +"%permission_required%");

    }
}
