package de.stupidus.tabCompleter;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ExternTabCompleter extends TabCompleterSuper implements TabCompleter {

    private final List<String> tabCompletes;
    private final String permission;
    public ExternTabCompleter(String permission, List<String> tabCompleteList) {
        this.tabCompletes = tabCompleteList;
        this.permission = permission;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        List<String> tabComplete = new ArrayList<>();
            if (permission != null && sender.hasPermission(permission)) {
                    for (String name : tabCompletes) {
                        if (name != null) {
                            String[] subParts = name.split(" ");
                            int commandLength = subParts.length;
                            List<Integer> argList = generateArgLengthList(name);

                            if (args.length == 1) {
                                tabComplete.add(subParts[0]);
                                if (args[0] != null) {
                                    boolean containsVarArg = subParts[0].startsWith("<[") && subParts[0].endsWith("]>");
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
                                boolean containsVarArg = subParts[0].startsWith("<[") && subParts[0].endsWith("]>");
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
        return tabComplete;
    }
}
