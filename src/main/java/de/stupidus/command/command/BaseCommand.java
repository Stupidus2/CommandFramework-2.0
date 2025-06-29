package de.stupidus.command.command;

import de.stupidus.api.CMDFWCommand;
import de.stupidus.api.Settings;
import de.stupidus.framework.CommandFramework;
import de.stupidus.framework.initializer.Initializer;
import de.stupidus.subCommand.SubCommand;
import de.stupidus.tabCompleter.CustomTabCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BaseCommand extends Command implements CMDFWCommand, Listener {

    //VARIABLES AND CONSTRUCTOR
    protected CommandFramework commandFramework;
    protected ArrayList<SubCommand> subCommands;
    protected CustomTabCompleter commandTabCompleter;

    protected String name;
    protected String permission = null;
    protected List<Settings> settings = new ArrayList<>();
    protected Initializer initializer = CommandFramework.getInitializer();
    protected List<String> aliases = null;
    protected String description = null;

    public BaseCommand(String name) {
        super(name);
        this.name = name;
        this.commandFramework = new CommandFramework();
        this.subCommands = commandFramework.getCommandManager().getSubCommands();
        this.commandTabCompleter = new CustomTabCompleter(subCommands);

        if (permission != null) setPermission(permission);
        if (aliases != null) setAliases(aliases);
        if (description != null) setDescription(description);

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
    public void setCommandPermission(String permission) {
        this.permission = permission;
        setPermission(permission);
    }
    public void setCommandAliases(List<String> aliases) {
        this.aliases = aliases;
        setAliases(aliases);
    }

    public void setCommandDescription(String description) {
        this.description = description;
        setDescription(description);
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
    public boolean onCommandCustom(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args) {
        return true;
    }

    //TAB COMPLETER

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return Objects.requireNonNull(commandTabCompleter.onTabComplete(sender, this, alias, args));
    }
}