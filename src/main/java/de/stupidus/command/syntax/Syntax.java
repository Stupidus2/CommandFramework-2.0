package de.stupidus.command.syntax;

import de.stupidus.api.CMDFWSyntax;
import de.stupidus.command.command.Command;
import de.stupidus.subCommand.SubCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class Syntax implements CMDFWSyntax {
    private String syntaxPattern = ChatColor.RED + "/<command>" + ChatColor.YELLOW + "<arg>";
    private String headPattern = ChatColor.RED + "Usage:";
    private String bottomPattern = null;
    private HashMap<String, List<String>> commandStrings = new HashMap<>();
    private HashMap<String, List<String>> finalCommandStrings = new HashMap<>();
    private int currentSizeList;
    private String hoverPattern = ChatColor.YELLOW + "/<command><arg>";


    //SET SYNTAX PATTERN

    @Override
    public void setSyntaxPattern(String syntax) {
        syntaxPattern = syntax;
    }

    public void setHoverPattern(String hover) {
        this.hoverPattern = hover;
    }

    @Override
    public void setSyntaxPattern(String headline, String bottomLine, String syntax) {
        bottomPattern = bottomLine;
        headPattern = headline;
        syntaxPattern = syntax;
    }

    //ADD SYNTAX

    @Override
    public void addCommandString(String fullCommandString) {
        String[] commandArray = fullCommandString.split(" ");
        if (!commandStrings.containsKey(commandArray[0])) {
            commandStrings.put(commandArray[0], new ArrayList<>());
        }
        String result = String.join(" ", Arrays.copyOfRange(commandArray, 1, commandArray.length));
        commandStrings.get(commandArray[0]).add(result);
    }

    //CHECK IF IT CONTAINS SYNTAX

    @Override
    public boolean contains(String fullCommandString) {
        String[] commandArray = fullCommandString.split(" ");
        if (commandStrings.containsKey(commandArray[0])) {
            String result = String.join(" ", Arrays.copyOfRange(commandArray, 1, commandArray.length));
            return commandStrings.get(commandArray[0]).contains(result);
        }
        return false;
    }

    //GET SYNTAX AND SEND

    @Override
    public void sendSyntax(CommandSender sender, String fullCommandString, boolean clickable) {
        String[] commandArray = fullCommandString.split(" ");
        String[] syntaxArray = getSyntax(fullCommandString).split("\n");

        //SEND HEADLINE MESSAGE
        if (headPattern != null) sender.sendMessage(headPattern.replace("<command>", commandArray[0]));

        for (String s : syntaxArray) {
            if (clickable) {
                TextComponent message = new TextComponent(syntaxPattern.replace("<command>", commandArray[0]).replace("<arg>", s));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + commandArray[0] + s));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverPattern.replace("<command>", commandArray[0]).replace("<arg>", s))));
                sender.spigot().sendMessage(message);
            } else {
              sender.sendMessage(syntaxPattern.replace("<command>", commandArray[0]).replace("<arg>", s));
            }
        }

        //SEND BOTTOM MESSAGE
        if (bottomPattern != null) sender.sendMessage(bottomPattern.replace("<command>", commandArray[0]));

    }

    private String getSyntax(String fullCommandString) {
        setCommandStringFinal();

        boolean found = false;

        String[] commandArray = fullCommandString.split(" ");
        StringBuilder result = new StringBuilder();
        if (finalCommandStrings.containsKey(commandArray[0])) {
            if (commandArray.length > 1) {
                for (String s : finalCommandStrings.get(commandArray[0])) {
                    String[] commandStringArray = s.split(" ");
                    if (commandStringArray[0].equalsIgnoreCase(commandArray[1])) {

                        //RESULT arg > 1
                        if (!found) found = true;

                        for (String ss : commandStringArray) {
                            result.append(" ").append(ss);
                        }
                        result.append("\n");
                    }
                }
                if (!found) {
                    for (String s : finalCommandStrings.get(commandArray[0])) {
                        result.append(" ").append(s).append("\n");
                    }
                }
                return result.toString();
            }

            //RESULT arg < 1 OR ARG[0] NOT FOUND

            for (String s : finalCommandStrings.get(commandArray[0])) {
                result.append(" ").append(s).append("\n");
            }
            return result.toString();
        }
        return "";
    }

    @Override
    public void replaceArg(Command command, SubCommand subCommand, int argsLength, String replacement) {
        setCommandStringFinal();

        String[] sArray;
        List<String> tempList = new ArrayList<>();
        if (finalCommandStrings.containsKey(command.getName())) {
            for (String s : finalCommandStrings.get(command.getName())) {
                sArray = s.split(" ");
                if (subCommand.getNameList().contains(s)) {
                    if (sArray.length >= argsLength) {
                        sArray[argsLength - 1] = replacement;
                    }
                }
                tempList.add(String.join(" ", sArray));
            }
            finalCommandStrings.remove(command.getName());
            finalCommandStrings.putIfAbsent(command.getName(), clearDuplicates(tempList));
        }
    }

    private List<String> clearDuplicates(List<String> liste) {
        List<String> filteredList = new ArrayList<>();
        for (String s : liste) {
            if (!filteredList.contains(s)) {
                filteredList.add(s);
            }
        }
        return new ArrayList<>(filteredList);
    }

    private void setCommandStringFinal() {
        if (!(currentSizeList == commandStrings.size())) {
            finalCommandStrings = commandStrings;
            currentSizeList = commandStrings.size();
        }
    }
}
