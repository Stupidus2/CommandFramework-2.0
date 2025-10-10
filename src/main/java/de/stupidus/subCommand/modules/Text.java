package de.stupidus.subCommand.modules;

import de.stupidus.subCommand.SubCommand;
import de.stupidus.subCommand.SubCommandTemp;

import java.util.HashMap;

public class Text {

    private static String identifierStart = "[";
    private static String identifierEnd = "]";

    HashMap<String, SubCommand> subCommands = new HashMap<>();


    protected static boolean containsText(String string) {

        String[] split = string.split(" ");
        for (String s : split) {
            if (s.startsWith(identifierStart) && s.endsWith(identifierEnd)) {
                return true;
            }
        }
        return false;
    }
    public static void setIdentifierStart(String identifierStart) {
        Text.identifierStart = identifierStart;
    }
    public static void setIdentifierEnd(String identifierEnd) {
        Text.identifierEnd = identifierEnd;
    }

    public static String getText(String commandInput, SubCommandTemp subCommand) {

    }
}
