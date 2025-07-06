package de.stupidus.framework;

import de.stupidus.command.command.BaseCommand;
import de.stupidus.command.command.CommandManager;
import de.stupidus.command.syntax.Syntax;
import de.stupidus.framework.initializer.Initializer;
import de.stupidus.framework.initializer.MessageInitializer;
import de.stupidus.messages.Message;
import de.stupidus.messages.Messages;
import de.stupidus.messages.Translator;
import de.stupidus.sound.CommandSound;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandFramework {

    private static Message message;
    private static Initializer initializer;
    private static Syntax syntax;
    private static HashMap<Messages, Translator> messages = new HashMap<>();
    private static CommandSound commandSound;
    private CommandManager commandManager;
    private static final List<BaseCommand> commandName = new ArrayList<>();
    private static final Map<String, Object> commandsByClass = new HashMap<>();
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
    public static List<BaseCommand> getCommands() {
        return commandName;
    }
    public static BaseCommand getCommand(String nameCommand) {
        for (BaseCommand command : commandName) {
            if (command.getName().equalsIgnoreCase(nameCommand)) {
                return command;
            }
        }
        return null;
    }

    public void removeCommandChoose(String commandName, String chooseToDelete) {
        for (BaseCommand command : CommandFramework.getCommands()) {
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
    public void addCommand(BaseCommand command, Object instance) {
        commandName.add(command);
        commandsByClass.put(command.getCommandClassName(), instance);
    }



    // SubCommand Reflection


    public static Object getCommandByClassName(String className) {
        return commandsByClass.get(className);
    }

    public static SubCommand getSubCommand(Object instance, String fieldName) {
        try {
            Field field = findField(instance.getClass(), fieldName);

            if (field == null) {
                Bukkit.getConsoleSender().sendMessage("§c[CMDFW] Field '" + fieldName + "' not found in class " + instance.getClass().getName());
                return null;
            }
            field.setAccessible(true);
            Object value = field.get(instance);

            if (value instanceof SubCommand) {
                return (SubCommand) value;
            } else {
                Bukkit.getConsoleSender().sendMessage("§c[CMDFW] Field is not a SubCommand in " + instance.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void setSubCommand(Object instance, String fieldName, SubCommand subCommand) {
        try {
            Field field = findField(instance.getClass(), fieldName);
            if (field == null) {
                Bukkit.getConsoleSender().sendMessage("§c[CMDFW] Field '" + fieldName + "' not found in class " + instance.getClass().getName());
                return;
            }
            field.setAccessible(true);
            field.set(instance, subCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Field findField(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
}
