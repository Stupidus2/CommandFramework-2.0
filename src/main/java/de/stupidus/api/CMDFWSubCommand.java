package de.stupidus.api;

import de.stupidus.command.Code;
import de.stupidus.framework.Settings;

import java.util.List;

public interface CMDFWSubCommand {
    void addChoose(String name);
    void addSetting(Settings settings);

    void setCode(Code codeToExecute);

    Code getCode();

    List<String> getNameList();

    boolean getTabCompletable();
}
