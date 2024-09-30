package de.stupidus.framework;

import de.stupidus.api.Messages;
import de.stupidus.command.CommandManager;
import de.stupidus.msg.Message;
import de.stupidus.msg.Translation;
import de.stupidus.msg.Translator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;

import java.util.*;

public class CommandFramework {

    private static Message message;
    private CommandManager commandManager;
    private static Initializer initializer;
    private static HashMap<Messages, Translator> messages = new HashMap<>();
    private static HashMap<String, CommandExecutor> commandNamesCommandExecutor = new HashMap<>();
    private static HashMap<String, TabCompleter> commandNamesTabCompleter = new HashMap<>();
    private static HashMap<String, Listener> commandNamesListener = new HashMap<>();
    private static boolean spigotTabCompleter = false;

    public CommandFramework(String name, CommandExecutor commandExecutor, TabCompleter tabCompleter, Listener listener) {
        commandNamesCommandExecutor.putIfAbsent(name, commandExecutor);
        commandNamesTabCompleter.putIfAbsent(name, tabCompleter);
        commandNamesListener.putIfAbsent(name, listener);
    }

    public CommandFramework() {
    }

    //Static methods
    public static Initializer getInitializer() {
        if (initializer == null) {
            initializer = new Initializer();
        }
        return initializer;
    }

    public static Message getMessages() {
        if (message == null) {
            message = new Message(messages);
            messageInit();
        }
        return message;
    }

    public static HashMap<String, CommandExecutor> getAllCommandNamesCommandExecutor() {
        return commandNamesCommandExecutor;
    }

    public static HashMap<String, TabCompleter> getAllCommandNamesTabCompleter() {
        return commandNamesTabCompleter;
    }

    public static HashMap<String, Listener> getAllCommandNamesListener() {
        return commandNamesListener;
    }


    //Non-static methods

    public CommandManager getCommandManager() {
        if (commandManager == null) {
            commandManager = new CommandManager();
        }
        return commandManager;
    }
    public static void setSpigotTabCompleter(boolean bool) {
        spigotTabCompleter = bool;
    }
    private static void messageInit() {
        //Unknown Command message
        Translator unknownCommandTranslator = new Translator();
        unknownCommandTranslator.setTranslationMessage(Translation.English, ChatColor.YELLOW + "Could not find this command");
        unknownCommandTranslator.setTranslationMessage(Translation.German, ChatColor.YELLOW + "Dieser Command konnte nicht gefunden werden");
        message.newMessage(Messages.UNKNOWN_COMMAND_NAME, unknownCommandTranslator);

        //Not a player message
        Translator noPlayerTranslator = new Translator();
        noPlayerTranslator.setTranslationMessage(Translation.English, ChatColor.YELLOW + "You have to be a player to execute this command");
        noPlayerTranslator.setTranslationMessage(Translation.German, ChatColor.YELLOW + "Du musst ein Spieler sein um diesen Command auszuführen");
        message.newMessage(Messages.NOT_A_PLAYER, noPlayerTranslator);

        //Missing Permission message
        Translator missingPermissionTranslator = new Translator();
        missingPermissionTranslator.setTranslationMessage(Translation.English, ChatColor.YELLOW + "You need following permission: " + ChatColor.RED + "%permission_required%");
        missingPermissionTranslator.setTranslationMessage(Translation.German, ChatColor.YELLOW + "Du benötigst folgende Berechtigungen: " + ChatColor.RED + "%permission_required%");
        message.newMessage(Messages.MISSING_PERMISSION, missingPermissionTranslator);

    }
}
