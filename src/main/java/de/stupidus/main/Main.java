package de.stupidus.main;

import de.stupidus.framework.CommandFramework;
import de.stupidus.framework.Initializer;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        Initializer initializer = CommandFramework.getInitializer();
        initializer.addPackage("de.stupidus.commands");
        initializer.register(this);
    }
}