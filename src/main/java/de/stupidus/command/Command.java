package de.stupidus.command;

import de.stupidus.Messages.Message;
import de.stupidus.api.CMDFWCommand;
import de.stupidus.api.Messages;
import de.stupidus.framework.CommandFramework;
import de.stupidus.api.Settings;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public abstract class Command implements CMDFWCommand, CommandExecutor, TabCompleter, Listener {

    //Variablen
    private CommandFramework commandFramework;
    private ArrayList<SubCommand> subCommands;
    private Message message = CommandFramework.getMessages();
    private String name;
    private String permission = null;

    public Command(String name) {
        this.name = name;
        this.commandFramework = new CommandFramework(this.name, this, this);
        this.subCommands = commandFramework.getCommandManager().getSubCommands();
    }

    //Command System
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {

        String commandString = null;
        for (String arg : args) {
            if (commandString == null) {
                commandString = arg;
            } else {
                commandString = commandString + " " + arg;
            }
        }


        if (args.length > 0) {
            outerLoop:
            for (SubCommand subCommand : subCommands) {
                for (String name : subCommand.getNameList()) {
                    if (name != null) {
                        if (name.equalsIgnoreCase(commandString)) {
                            String[] subParts = name.split(" ");
                            String subCommandName = subParts[subParts.length - 1];
                            int commandLength = subParts.length;

                            if (commandLength > 1) {
                                for (int i = 0; i < commandLength; i++) {
                                    if (!args[i].equalsIgnoreCase(subParts[i])) {
                                        continue outerLoop;
                                    }
                                }
                                return checkSubCommandPermission(sender, subCommand, command, s, args);
                            } else {

                                if (args.length == commandLength) {
                                    if (args[commandLength - 1].equalsIgnoreCase(name)) {
                                        return checkSubCommandPermission(sender, subCommand, command, s, args);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            sender.sendMessage(message.getMessage(Messages.UNKNOWN_COMMAND_NAME));
        } else {
            if (permission == null || sender.hasPermission(getPermission())) {
                execute(sender, command, s, args);
            } else {
                sender.sendMessage(message.getMessage(Messages.MISSING_PERMISSION).replace("%permission_required%", getPermission()));
            }
        }
        return true;
    }

    private boolean checkSubCommandPermission(CommandSender sender, SubCommand subCommand, org.bukkit.command.Command command, String s, String[] args) {
        List<Settings> settingsList = subCommand.getSettingsList();
        Player player = null;
        if (settingsList.contains(Settings.PLAYER) && !(sender instanceof Player)) {
            sender.sendMessage(message.getMessage(Messages.NOT_A_PLAYER));
            return true;
        } else if (sender instanceof Player){
            player = (Player) sender;
        }
        subCommandInitialize();
        subCommandCode(sender, player, command, s, args);

        if (subCommand.getPermission() == null || subCommand.getPermission() != null && sender.hasPermission(subCommand.getPermission())) {
            Code code = subCommand.getCode();
            code.functionToExecute();
        } else {
            sender.sendMessage(message.getMessage(Messages.MISSING_PERMISSION).replace("%permission_required%", subCommand.getPermission()));
        }
        return true;
    }

    // Execute, SubCommand initialisierung, Command Parameter werden Initialisiert

    @Override
    public boolean execute(CommandSender commandSender, org.bukkit.command.Command command, String cmd, String[] args) {
        return false;
    }

    @Override
    public abstract void subCommandInitialize();

    @Override
    public abstract void subCommandCode(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args);

    @Override
    public CommandFramework getCommandFramework() {
        return commandFramework;
    }

    @Override
    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getPermission() {
        return permission;
    }
    // Tab Complete

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        List<String> tabComplete = new ArrayList<>();
        for (SubCommand subCommand : subCommands) {
            if (subCommand.getPermission() == null || subCommand.getPermission() != null && sender.hasPermission(subCommand.getPermission())) {
                if (subCommand.getTabCompletable()) {
                    for (String name : subCommand.getNameList()) {
                        if (name != null) {
                            String[] subParts = name.split(" ");
                            String subCommandName = subParts[subParts.length - 1];
                            int commandLength = subParts.length;

                            if (args.length == 1) {
                                tabComplete.add(subParts[0]);
                            }
                            if (args.length > 1) {
                                for (int i = 1; i < commandLength; i++) {
                                    if (args.length - 1 == i && args[0].equalsIgnoreCase(subParts[0])) {
                                        tabComplete.add(subParts[i]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return tabComplete;
    }
}