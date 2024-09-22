package de.stupidus.main;

import de.stupidus.framework.CommandFramework;
import de.stupidus.framework.Initializer;
import de.stupidus.msg.Translation;
import de.stupidus.msg.Translator;
import org.bukkit.plugin.java.JavaPlugin;
public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Translator.setConsoleLanguage(Translation.German);
        Initializer initializer = CommandFramework.getInitializer();
        initializer.addPackage("de.stupidus.commands");
        initializer.register(this);
    }
}
