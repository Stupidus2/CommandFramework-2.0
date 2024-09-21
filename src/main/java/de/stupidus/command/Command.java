package de.stupidus.command;

import de.stupidus.Messages.Message;
import de.stupidus.api.CMDFWCommand;
import de.stupidus.api.Messages;
import de.stupidus.framework.CommandFramework;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.Bukkit;
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

    public Command(String name) {
        this.name = name;
        this.commandFramework = new CommandFramework(this.name, this, this);
        this.subCommands = commandFramework.getCommandManager().getSubCommands();
    }

    //Command System
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(message.getMessage(Messages.NOT_A_PLAYER));
            return true;
        }
        Player player = (Player) sender;
        subCommandInitialize();
        subCommandCode(sender,player,command,s,args);

        String commandString = null;

        for (int i = 0; i < args.length; i++) {
            if (commandString == null) {
                commandString = args[i];
            } else {
                commandString = commandString + " " + args[i];
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
                                Code code = subCommand.getCode();
                                code.functionToExecute();
                                return true;
                            } else {

                                if (args.length == commandLength) {
                                    if (args[commandLength - 1].equalsIgnoreCase(name)) {
                                        Code code = subCommand.getCode();
                                        code.functionToExecute();
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            sender.sendMessage(message.getMessage(Messages.UNKNOWN_COMMAND_NAME));
      } else {
            execute(sender, command, s,args);
      }
     return true;
    }

    // Execute, SubCommand initialisierung, Command Parameter werden Initialisiert

    @Override
    public boolean execute(CommandSender commandSender, org.bukkit.command.Command command, String cmd, String[] args){return false;}

    @Override
    public abstract void subCommandInitialize();

    @Override
    public abstract void subCommandCode(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args);

    @Override
    public CommandFramework getCommandFramework() {
        return commandFramework;
    }

    // Tab Complete

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        List<String> tabComplete = new ArrayList<>();
            for (SubCommand subCommand: subCommands) {
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
        return tabComplete;
    }
}