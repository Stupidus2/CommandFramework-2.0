package de.stupidus.command;

import de.stupidus.subCommand.SubCommand;

import java.util.ArrayList;

public class CommandManager {

    private ArrayList<SubCommand> subCommands = new ArrayList<>();
    public void addSubCommand(SubCommand subCommand) {
        if(!subCommands.contains(subCommand)) {
            subCommands.add(subCommand);
        }
    }
    public ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }
    public void clearSubCommand() {
        subCommands.clear();
    }
}
