package de.stupidus.command.command;

import de.stupidus.api.CMDFWCommand;
import de.stupidus.api.Settings;
import de.stupidus.framework.CommandFramework;
import de.stupidus.framework.initializer.Initializer;
import de.stupidus.subCommand.SubCommand;
import de.stupidus.tabCompleter.CustomTabCompleter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommand implements CMDFWCommand, TabCompleter, CommandExecutor, Listener {

    //VARIABLES AND CONSTRUCTOR
    protected CommandFramework commandFramework;
    protected ArrayList<SubCommand> subCommands;
    protected CustomTabCompleter commandTabCompleter;

    protected String name;
    protected String permission = null;
    protected List<Settings> settings = new ArrayList<>();
    protected Initializer initializer = CommandFramework.getInitializer();

    public BaseCommand(String name) {
        this.name = name;
        this.commandFramework = new CommandFramework();
        this.subCommands = commandFramework.getCommandManager().getSubCommands();
        this.commandTabCompleter = new CustomTabCompleter(subCommands);
    }


    //GETTER AND SETTER
    @Override
    public List<Settings> getSettings() {
        return settings;
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public void addSetting(Settings settings) {
        this.settings.add(settings);
    }

    @Override
    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public CommandFramework getCommandFramework() {
        return commandFramework;
    }

    @Override
    public String getName() {
        return name;
    }

    public CustomTabCompleter getTabCompleter() {
        return commandTabCompleter;
    }

    //ABSTRACT METHODS
    @Override
    public abstract void initialize();
    @Override
    public abstract void subCommandCode(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args);
    @Override
    public boolean execute(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args) {
        return true;
    }

    //TAB COMPLETER
    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        return commandTabCompleter.onTabComplete(sender, command, s, args);
    }
}