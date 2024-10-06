package de.stupidus.main;

import de.stupidus.command.syntax.Syntax;
import de.stupidus.sound.CommandSound;
import de.stupidus.framework.CommandFramework;
import de.stupidus.framework.initializer.Initializer;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main plugin;

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        Initializer initializer = CommandFramework.getInitializer();
        initializer.addPackage("de.stupidus.commands");
        initializer.register(this);

        Syntax syntaxCreator = CommandFramework.getSyntax();

        syntaxCreator.setSyntaxPattern(ChatColor.RED+"<command>"+ChatColor.BLUE+"<arg>");
        syntaxCreator.setSyntaxPattern("Use of Command <command>", "Hallo",ChatColor.RED+"<command>"+ChatColor.BLUE+"<arg>");

        CommandSound sound = CommandFramework.getCommandSound();

    }
}
