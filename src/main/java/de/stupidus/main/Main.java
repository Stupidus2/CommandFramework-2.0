package de.stupidus.main;

import de.stupidus.command.PlayerJoin;
import de.stupidus.framework.CommandFramework;
import de.stupidus.framework.Initializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
public class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);

        Initializer initializer = CommandFramework.getInitializer();
        initializer.addPackage("de.stupidus.commands");
        initializer.register(this);
    }
}
