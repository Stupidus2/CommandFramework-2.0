package de.stupidus.command;

import de.stupidus.msg.Message;
import de.stupidus.api.CMDFWCommand;
import de.stupidus.api.Messages;
import de.stupidus.framework.CommandFramework;
import de.stupidus.api.Settings;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Command implements CMDFWCommand, CommandExecutor, TabCompleter, Listener {

    //Variablen
    private CommandFramework commandFramework;
    private ArrayList<SubCommand> subCommands;
    private final Message message = CommandFramework.getMessages();
    private String name;
    private String permission = null;

    public Command(String name) {
        this.name = name;
        this.commandFramework = new CommandFramework(this.name, this, this, this);
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
                subCommand.filterChoose();
                initialize();
                innerLoop:
                for (String name : subCommand.getNameList()) {
                    if (name != null) {

                        String[] nameArray = name.split(" ");
                        Bukkit.getConsoleSender().sendMessage(nameArray);

                        if (subCommand.containsVarArg() && args.length == nameArray.length || name.equalsIgnoreCase(commandString)) {
                            Bukkit.getConsoleSender().sendMessage(name);
                            String[] subParts = name.split(" ");
                            int commandLength = subParts.length;
                            List<Integer> argLengthList = generateArgLengthList(name);

                            if (commandLength > 1) {
                                for (int i = 0; i < commandLength; i++) {

                                    if (!args[i].equalsIgnoreCase(subParts[i])) {
                                        Bukkit.getConsoleSender().sendMessage("§bsdfdsfdfsfds");
                                        Bukkit.getConsoleSender().sendMessage(args[0] + " " + subParts[0]);
                                        if (subCommand.containsVarArg() && !argLengthList.contains(i)) {
                                            Bukkit.getConsoleSender().sendMessage("§5sdfdsfdfsfds");
                                            continue innerLoop;
                                        }
                                    }
                                }
                                return checkSubCommand(sender, subCommand, command, s, args);

                            } else {

                                if (args.length == commandLength) {
                                    if (args[commandLength - 1].equalsIgnoreCase(name) || subCommand.containsVarArg()) {
                                        return checkSubCommand(sender, subCommand, command, s, args);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            sender.sendMessage(message.getMessage(Messages.UNKNOWN_COMMAND_NAME).getTranslatedMessage(sender));
        } else {
            if (permission == null || sender.hasPermission(getPermission())) {
                initialize();
                execute(sender, command, s, args);
            } else {
                sender.sendMessage(message.getMessage(Messages.MISSING_PERMISSION).getTranslatedMessage(sender));
            }
        }
        return true;
    }

    private boolean checkSubCommand(CommandSender sender, SubCommand subCommand, org.bukkit.command.Command command, String s, String[] args) {
        List<Settings> settingsList = subCommand.getSettingsList();
        Player player = null;

        //Player

        if (settingsList.contains(Settings.PLAYER) && sender instanceof Player) {
            player = (Player) sender;

        } else {
            sender.sendMessage(message.getMessage(Messages.NOT_A_PLAYER).getTranslatedMessage(sender));
            return true;
        }

        if (subCommand.getPermission() == null || subCommand.getPermission() != null && sender.hasPermission(subCommand.getPermission())) {
            subCommandCode(sender, player, command, s, args);
            Code code = subCommand.getCode();
            code.functionToExecute();
        } else {
            sender.sendMessage(message.getMessage(Messages.MISSING_PERMISSION).getTranslatedMessage(sender).replace("%permission_required%", subCommand.getPermission()));
        }
        return true;
    }

    private List<Integer> generateArgLengthList(String subCommandName) {
        String[] subCommandArray = subCommandName.split(" ");
        return IntStream.range(0, subCommandArray.length)
                .filter(i -> subCommandArray[i].startsWith("<[") && subCommandArray[i].endsWith("]>"))
                .boxed()
                .collect(Collectors.toList());
    }

    // Execute, SubCommand initialisierung, Command Parameter werden Initialisiert

    @Override
    public boolean execute(CommandSender commandSender, org.bukkit.command.Command command, String cmd, String[] args) {
        return false;
    }

    @Override
    public abstract void initialize();

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
            if (subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission())) {
                if (subCommand.getTabCompletable()) {

                    boolean containsVarArg = subCommand.containsVarArg();

                    for (String name : subCommand.getNameList()) {
                        if (name != null) {
                            String[] subParts = name.split(" ");
                            int commandLength = subParts.length;
                            List<Integer> argList = generateArgLengthList(name);

                            if (args.length == 1) {
                                tabComplete.add(subParts[0]);

                                if (args[0] != null) {
                                    String strSub = subParts[0];
                                    char[] lettersSub = strSub.toCharArray();

                                    if (containsVarArg && strSub.startsWith("<[") && strSub.endsWith("]>")) {
                                        tabComplete.add(" ");
                                    }

                                    char[] lettersArg = args[0].toCharArray();

                                    for (int i = 0; i < lettersArg.length; i++) {
                                        if (lettersSub.length - 1 >= i && lettersArg[i] != lettersSub[i] || containsVarArg && argList.contains(0)) {
                                            tabComplete.remove(subParts[0]);
                                            if (argList.contains(0)) {
                                                tabComplete.remove(" ");
                                            }
                                            break;
                                        }
                                    }
                                }
                            }

                            if (args.length > 1) {
                                tabComplete.remove(" ");
                                for (int i = 1; i < commandLength; i++) {
                                    if (args.length - 1 == i && args[0].equalsIgnoreCase(subParts[0])) {
                                        tabComplete.add(subParts[i]);

                                        if (args[i] != null) {
                                            String strSub = subParts[i];
                                            char[] lettersSub = strSub.toCharArray();

                                            if (containsVarArg && strSub.startsWith("<[") && strSub.endsWith("]>")) {
                                                tabComplete.add("  ");
                                            }

                                            String strArg = args[i];
                                            char[] lettersArg = strArg.toCharArray();

                                            for (int i2 = 0; i2 < lettersArg.length; i2++) {
                                                if (i2 < lettersSub.length) {
                                                    if (lettersArg[i2] != lettersSub[i2] || containsVarArg && argList.contains(i)) {
                                                        if (argList.contains(i)) {
                                                            tabComplete.remove(" ");
                                                        }
                                                        tabComplete.remove(subParts[i]);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
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