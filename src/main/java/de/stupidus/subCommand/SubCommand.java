package de.stupidus.subCommand;

import de.stupidus.api.CMDFWSubCommand;
import de.stupidus.api.Settings;
import de.stupidus.command.command.CommandBuilder;
import de.stupidus.command.others.Code;
import de.stupidus.command.command.CommandManager;
import de.stupidus.framework.CommandFramework;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

//

public class SubCommand implements CMDFWSubCommand {

    // FIELDS
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

    // CONSTRUCTOR
    public SubCommand(CommandFramework commandFramework, String name, boolean tabComplete) {
        this(commandFramework, name, tabComplete, null);
    }

    public SubCommand(CommandFramework commandFramework, String name, boolean tabComplete, CommandBuilder commandBuilder) {
        this.commandFramework = commandFramework;
        this.commandManager = this.commandFramework.getCommandManager();
        this.commandBuilder = commandBuilder;

        addChoose(name);
        this.tabComplete = tabComplete;
        commandManager.addSubCommand(this);
    }

    public SubCommand(CommandFramework commandFramework, List<String> nameList, boolean tabComplete) {
        this(commandFramework, nameList, tabComplete, null);
    }

    public SubCommand(CommandFramework commandFramework, List<String> nameList, boolean tabComplete, CommandBuilder commandBuilder) {
        this.commandFramework = commandFramework;
        this.commandManager = this.commandFramework.getCommandManager();
        this.commandBuilder = commandBuilder;

        for (String name : nameList) addChoose(name);
        this.tabComplete = tabComplete;
        commandManager.addSubCommand(this);
    }

    // ADD CHOOSE (TO EXECUTE SUBCOMMAND)
    @Override
    public SubCommand addChoose(String name) {
        if (name != null && !nameList.containsKey(name)) {
            List<UUID> uuidList = new ArrayList<>();
            nameList.putIfAbsent(name, uuidList);
            ArrayList<UUID> uuidArrayList = new ArrayList<>();
            bannedList.putIfAbsent(name, uuidArrayList);


            // CHECK FOR TEXT

            String[] nameArray = name.split(" ");
            if (nameArray[nameArray.length - 1].startsWith("[") && nameArray[nameArray.length - 1].endsWith("]"))
                text.add(name);


            // Check if subCommand args contain <[ ]> and store in list

            List<String> varArgList = Arrays.stream(name.split(" "))
                    .filter(s -> s.startsWith("<[") && s.endsWith("]>"))
                    .collect(Collectors.toList());

            if (!varArgList.isEmpty()) {
                varArg.putIfAbsent(name, varArgList);
            }
        }
        return this;
    }

    public SubCommand addChoose(List<String> nameList) {
        for (String name : nameList) {
            addChoose(name);
        }
        return this;
    }

    @Override
    public SubCommand addChoose(String name, UUID uuid) {
        if (name != null && !nameList.containsKey(name)) {
            List<UUID> uuidList = new ArrayList<>();
            uuidList.add(uuid);
            nameList.putIfAbsent(name, uuidList);
            ArrayList<UUID> uuidArrayList = new ArrayList<>();
            bannedList.putIfAbsent(name, uuidArrayList);

            // CHECK FOR TEXT

            String[] nameArray = name.split(" ");
            if (nameArray[nameArray.length - 1].startsWith("[") && nameArray[nameArray.length - 1].endsWith("]"))
                text.add(name);


            // Check if subCommand args contain <[ ]> and store in list
            List<String> varArgList = Arrays.stream(name.split(" "))
                    .filter(s -> s.startsWith("<[") && s.endsWith("]>"))
                    .collect(Collectors.toList());

            if (!varArgList.isEmpty()) {
                varArg.putIfAbsent(name, varArgList);
            }
        }
        return this;
    }

    public SubCommand addAccess(String nameSubCommand, UUID uuid) {
        if (nameList.get(nameSubCommand) == null || nameList.get(nameSubCommand).contains(uuid)) return this;
        nameList.get(nameSubCommand).add(uuid);
        return this;
    }

    public SubCommand removeAccess(String nameSubCommand, UUID uuid) {
        if (nameList.get(nameSubCommand) == null) return this;
        nameList.get(nameSubCommand).remove(uuid);
        return this;
    }

    public boolean hasEveryoneAccess(String nameSubCommand) {
        return nameList.get(nameSubCommand).isEmpty() || nameList.get(nameSubCommand) == null;
    }

    public SubCommand addBan(String nameSubCommand, UUID uuid) {
        if (nameList.get(nameSubCommand) == null || bannedList.get(nameSubCommand).contains(uuid)) return this;
        bannedList.get(nameSubCommand).add(uuid);
        return this;
    }

    public SubCommand removeBan(String nameSubCommand, UUID uuid) {
        if (nameList.get(nameSubCommand) == null || !bannedList.get(nameSubCommand).contains(uuid) || uuid == null)
            return this;
        bannedList.get(nameSubCommand).remove(uuid);
        return this;
    }

    public boolean isBanned(String nameSubCommand, UUID uuid) {
        if (uuid == null) return false;
        return bannedList.get(nameSubCommand).contains(uuid);
    }

    public SubCommand removeChoose(String name) {
        if (nameList.containsKey(name)) {
            nameList.remove(name);
            bannedList.remove(name);

            List<String> varArgList = Arrays.stream(name.split(" "))
                    .filter(s -> s.startsWith("<[") && s.endsWith("]>"))
                    .collect(Collectors.toList());

            if (!varArgList.isEmpty()) {
                varArg.remove(name);
            }
        }
        return this;
    }


    // UTIL FUNCTIONS
    @Override
    @Deprecated()
    public SubCommand filterChoose() {
        return this;
    }

    public SubCommand cleanChoose() {
        nameList = new HashMap<>();
        varArg = new HashMap<>();
        bannedList = new HashMap<>();
        text = new ArrayList<>();
        return this;
    }

    @Override
    public boolean containsVarArg() {
        return !varArg.isEmpty();
    }

    // GETTER, SETTER, ADDER

    public boolean containsText(String nameSubCommand) {
        return text.contains(nameSubCommand);
    }

    public String getCommandWithoutText(String nameSubCommand) {
        nameSubCommand = nameSubCommand.substring(0, nameSubCommand.lastIndexOf(" "));
        return nameSubCommand;
    }

    public int getTextBeginnIndex(String nameSubCommand) {
        return nameSubCommand.split(" ").length - 1;
    }

    @Override
    public SubCommand addSetting(Settings setting) {
        settings.add(setting);
        return this;
    }

    @Override
    public SubCommand setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    @Deprecated
    public SubCommand setCode(Code codeToExecute) {
        this.code = codeToExecute;
        return this;
    }

    @Override
    public SubCommand setCode(Runnable code) {
        this.runnableCode = code;
        return this;
    }

    public SubCommand setCode(Consumer<CommandBuilder> code) {
        this.consumerCode = code;
        return this;
    }

    @Override
    public Code getCode() {
        return code;
    }

    @Override
    public Runnable getRunnableCode() {
        return runnableCode;
    }

    public Consumer<CommandBuilder> getConsumerCode() {
        return consumerCode;
    }

    @Override
    public List<Settings> getSettings() {
        return settings;
    }

    @Override
    public HashMap<String, List<UUID>> getNameList() {
        return nameList;
    }

    @Override
    public boolean getTabCompletable() {
        return tabComplete;
    }

    public CommandBuilder getCommandBuilder() {
        return commandBuilder;
    }
}