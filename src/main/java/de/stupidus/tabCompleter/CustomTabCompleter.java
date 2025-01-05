package de.stupidus.tabCompleter;

import de.stupidus.messages.Messages;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomTabCompleter extends TabCompleterSuper implements TabCompleter {
    private final List<SubCommand> subCommands;

    public CustomTabCompleter(List<SubCommand> subCommands) {
        this.subCommands = subCommands;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        List<String> tabComplete = new ArrayList<>();
        for (SubCommand subCommand : subCommands) {
            if (subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission())) {
                if (subCommand.getTabCompletable()) {

                    boolean containsVarArg = subCommand.containsVarArg();

                    for (String name : subCommand.getNameList().keySet()) {
                        if (name != null) {

                            if (!subCommand.getNameList().get(name).isEmpty()) {
                                if (!(sender instanceof Player)) {
                                    continue;
                                }
                                if (!subCommand.getNameList().get(name).contains(((Player) sender).getUniqueId())) {
                                    continue;
                                }
                            }

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
                                    if (args.length - 1 == i && args[0].equalsIgnoreCase(subParts[0]) || args.length - 1 == i && argList.contains(0)) {
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