package de.stupidus.subCommand;

import de.stupidus.api.Settings;
import de.stupidus.command.command.CommandBuilder;
import de.stupidus.command.command.CommandManager;
import de.stupidus.command.others.Code;
import de.stupidus.framework.CommandFramework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class SubCommandTemp {
    private final CommandFramework commandFramework;
    private final CommandManager commandManager;
    private final boolean tabComplete;
    private Code code;
    private String permission;
    private final List<Settings> settings = new ArrayList<>();
    private HashMap<String, List<String>> varArg = new HashMap<>();
    private List<String> text = new ArrayList<>();
    private CommandBuilder commandBuilder;
    private HashMap<String, List<UUID>> nameList = new HashMap<>();
    private HashMap<String, List<UUID>> bannedList = new HashMap<>();
    private Runnable runnableCode;
    private Consumer<CommandBuilder> consumerCode;

    public SubCommandTemp(CommandFramework commandFramework, String name, boolean tabComplete, CommandBuilder  commandBuilder) {
        this.commandFramework = commandFramework;
        this.commandManager = commandFramework.getCommandManager();
        this.tabComplete = tabComplete;
        this.commandBuilder = commandBuilder;
    }
    public SubCommandTemp(CommandFramework commandFramework, List<String> names, boolean tabComplete, CommandBuilder   commandBuilder) {
        this.commandFramework = commandFramework;
        this.commandManager = commandFramework.getCommandManager();
        this.tabComplete = tabComplete;
        this.commandBuilder = commandBuilder;
    }

    public SubCommandTemp(CommandFramework commandFramework, String name, boolean tabComplete) {
        this(commandFramework, name, tabComplete, null);
    }

    public SubCommandTemp(CommandFramework commandFramework, List<String> names, boolean tabComplete) {
        this(commandFramework, names, tabComplete, null);
    }

    public SubCommandTemp addChoose(String name, UUID uuid) {
        if (nameList.containsKey(name) && name == null) return this;
        addChoose(name, uuid);



        return this;
    }

    private void addToNameList(String name, UUID uuid) {
        ArrayList<UUID> uuidList = new ArrayList<>();
        if (uuid != null) uuidList.add(uuid);
        nameList.putIfAbsent(name, uuidList);
    }
}
