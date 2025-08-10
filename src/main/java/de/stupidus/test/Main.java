package de.stupidus.test;

import de.stupidus.framework.CommandFramework;
import de.stupidus.framework.initializer.Initializer;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Initializer initializer = CommandFramework.getInitializer();
        initializer.addPackage("de.stupidus");
        initializer.register(this);
    }
}
