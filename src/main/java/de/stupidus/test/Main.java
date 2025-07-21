package de.stupidus.test;

import de.stupidus.framework.CommandFramework;
import de.stupidus.framework.initializer.Initializer;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main plugin;

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {

        plugin = this;

        Initializer initializer = CommandFramework.getInitializer();
        initializer.addPackage("de.stupidus.test");
        initializer.register(this);
    }
}

