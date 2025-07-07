package de.stupidus.command.command;

import de.stupidus.api.Initialize;
import de.stupidus.framework.CommandFramework;
import de.stupidus.framework.initializer.Initializer;

import java.util.ArrayList;
import java.util.List;

public class GeneratePluginYML {

    public static void generate() {

        Initializer initializer = CommandFramework.getInitializer();
        initializer.updateInitializeMethods();
        initializer.executeMethod(null, "initialize", Initialize.class);

        List<BaseCommand> copy = new ArrayList<>(CommandFramework.getCommands());

        for (BaseCommand command : copy) {

            command.getDescription();
            PluginYMLUtil.newCommand(command.getName(), command.getDescription(), "");
        }
    }
}
