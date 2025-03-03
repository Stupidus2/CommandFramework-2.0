package de.stupidus.subCommand;

import de.stupidus.api.CMDFWSubCommand;
import de.stupidus.api.Settings;
import de.stupidus.command.others.Code;
import de.stupidus.command.command.CommandManager;
import de.stupidus.framework.CommandFramework;

import java.util.*;
import java.util.stream.Collectors;

public class SubCommand implements CMDFWSubCommand {

    // FIELDS
    private final CommandFramework commandFramework;
    private final CommandManager commandManager;
    private final boolean tabComplete;
    private Code code;
    private String permission;
    private final List<Settings> settings = new ArrayList<>();
    private final HashMap<String, List<String>> varArg = new HashMap<>();
    private HashMap<String, List<UUID>> nameList = new HashMap<>();
    private Runnable runnableCode;

    // CONSTRUCTOR
    public SubCommand(CommandFramework commandFramework, String name, boolean tabComplete) {
        this.commandFramework = commandFramework;
        this.commandManager = this.commandFramework.getCommandManager();

        addChoose(name);
        this.tabComplete = tabComplete;
        commandManager.addSubCommand(this);
    }

    // ADD CHOOSE (TO EXECUTE SUBCOMMAND)
    @Override
    public void addChoose(String name) {
        if (name != null && !nameList.containsKey(name)) {
            List<UUID> uuidList = new ArrayList<>();
            nameList.putIfAbsent(name, uuidList);

            // Check if subCommand args contain <[ ]> and store in list
            List<String> varArgList = Arrays.stream(name.split(" "))
                    .filter(s -> s.startsWith("<[") && s.endsWith("]>"))
                    .collect(Collectors.toList());

            if (!varArgList.isEmpty()) {
                varArg.putIfAbsent(name, varArgList);
            }
        }
    }
    @Override
    public void addChoose(String name, UUID uuid) {
        if (name != null && !nameList.containsKey(name)) {
            List<UUID> uuidList = new ArrayList<>();
            uuidList.add(uuid);
            nameList.putIfAbsent(name, uuidList);

            // Check if subCommand args contain <[ ]> and store in list
            List<String> varArgList = Arrays.stream(name.split(" "))
                    .filter(s -> s.startsWith("<[") && s.endsWith("]>"))
                    .collect(Collectors.toList());

            if (!varArgList.isEmpty()) {
                varArg.putIfAbsent(name, varArgList);
            }
        }
    }
    public void addAccess(String nameSubCommand, UUID uuid) {
        if (nameList.get(nameSubCommand) == null || nameList.get(nameSubCommand).contains(uuid)) return;
        nameList.get(nameSubCommand).add(uuid);
    }
    public void removeAccess(String nameSubCommand, UUID uuid) {
        if (nameList.get(nameSubCommand) == null) return;
        nameList.get(nameSubCommand).remove(uuid);
    }
    public void removeChoose(String name) {
        if (nameList.containsKey(name)) {
            nameList.remove(name);

            List<String> varArgList = Arrays.stream(name.split(" "))
                    .filter(s -> s.startsWith("<[") && s.endsWith("]>"))
                    .collect(Collectors.toList());

            if (!varArgList.isEmpty()) {
                varArg.remove(name);
            }
        }
    }

    // UTIL FUNCTIONS
    @Override
    @Deprecated()
    public void filterChoose() {}

    public void cleanChoose() {
        nameList = new HashMap<>();
    }

    @Override
    public boolean containsVarArg() {
        return !varArg.isEmpty();
    }

    // GETTER, SETTER, ADDER
    @Override
    public void addSetting(Settings setting) {
        settings.add(setting);
    }

    @Override
    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    @Deprecated
    public void setCode(Code codeToExecute) {
        this.code = codeToExecute;
    }

    @Override
    public void setCode(Runnable code) {
        this.runnableCode = code;
    }

    @Override
    public Code getCode() {
        return code;
    }

    @Override
    public Runnable getRunnableCode() {
        return runnableCode;
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
}