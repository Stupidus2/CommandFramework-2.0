package de.stupidus.subCommand;

import de.stupidus.api.CMDFWSubCommand;
import de.stupidus.api.Settings;
import de.stupidus.command.Code;
import de.stupidus.command.CommandManager;
import de.stupidus.framework.CommandFramework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubCommand implements CMDFWSubCommand {
    private final CommandFramework commandFramework;
    private CommandManager commandManager;
    private final boolean tabComplete;
    private Code code;
    private String permission = null;
    private ArrayList<Settings> settings = new ArrayList<>();
    private HashMap<String, List<String>> varArg = new HashMap<>();
    private List<String> nameList = new ArrayList<>();

    public SubCommand(CommandFramework commandFramework, String name, boolean tabComplete) {
        this.commandFramework = commandFramework;
        this.commandManager = this.commandFramework.getCommandManager();

        addChoose(name);
        this.tabComplete = tabComplete;
        commandManager.addSubCommand(this);
    }
    @Override
    public void addChoose(String name) {
        if (!nameList.contains(name) && name != null) {
            nameList.add(name);

            //Check if arg subCommand contains <[ and ]>, store in list

            String[] nameArray = name.split(" ");
            List<String> varArgList = new ArrayList<>();
            for (String s: nameArray) {
                if (s.startsWith("<[") && s.endsWith("]>")) {
                    varArgList.add(s);
                }
            }
            if (!varArgList.isEmpty()) {
                varArg.putIfAbsent(name, varArgList);
            }
        }
    }
    @Override
    public void filterChoose() {
        List<String> cleanedList = new ArrayList<>();
        for (String name: nameList) {
            if (!cleanedList.contains(name)) {
                cleanedList.add(name);
            }
        }
        nameList = cleanedList;
    }
    @Override
    public boolean containsVarArg() {
        return !varArg.isEmpty();
    }
    @Override
    public void addSetting(Settings setting) {
        settings.add(setting);
    }

    @Override
    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public void setCode(Code codeToExecute) {
        code = codeToExecute;
    }

    @Override
    public Code getCode() {
        return code;
    }

    @Override
    public List<Settings> getSettingsList() {
        return settings;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public List<String> getNameList() {
        return nameList;
    }

    @Override
    public boolean getTabCompletable() {
        return tabComplete;
    }
}
