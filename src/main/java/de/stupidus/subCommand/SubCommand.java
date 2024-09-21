package de.stupidus.subCommand;

import de.stupidus.api.CMDFWSubCommand;
import de.stupidus.command.Code;
import de.stupidus.command.CommandManager;
import de.stupidus.framework.CommandFramework;
import de.stupidus.framework.Settings;

import java.util.ArrayList;
import java.util.List;

public class SubCommand implements CMDFWSubCommand {

    private final List<String> nameList;
    private final CommandFramework commandFramework;
    private CommandManager commandManager;
    private final boolean tabComplete;
    private Code code;
    public SubCommand(CommandFramework commandFramework, String name, boolean tabComplete) {
        this.commandFramework = commandFramework;
        this.commandManager = this.commandFramework.getCommandManager();

        List<String> nameList = new ArrayList<>();
        nameList.add(name);
        this.nameList = nameList;
        this.tabComplete = tabComplete;
        commandManager.addSubCommand(this);
    }
    @Override
    public void addChoose(String name) {
        if (!nameList.contains(name)) {
            nameList.add(name);
        }
    }

    @Override
    public void addSetting(Settings settings) {

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
    public List<String> getNameList() {
        return nameList;
    }

    @Override
    public boolean getTabCompletable() {
        return tabComplete;
    }
}
