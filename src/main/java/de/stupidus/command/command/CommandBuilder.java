package de.stupidus.command.command;

import de.stupidus.api.Initialize;
import de.stupidus.api.Settings;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

@Initialize
public class CommandBuilder extends BaseCommand {

    private Runnable executeCode = null;
    private Consumer<CommandBuilder> consumerCode = null;

    private Player player;
    private Command command;
    private CommandSender sender;
    private String cmd;
    private String[] args;

    public CommandBuilder(String name) {
        super(name);
        commandFramework.addCommand(this, this);
    }

    public CommandBuilder build(Object instance) {
        return build(instance, false);
    }
    public CommandBuilder build(Object instance, Boolean normalRegistration) {
        this.normalRegistration = normalRegistration;
        commandFramework.addCommand(this, instance);
        return this;
    }


    @Override
    public boolean onCommandCustom(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args) {
        this.player = player;
        this.sender = sender;
        this.args = args;
        this.command = command;
        this.cmd = cmd;
        if (executeCode != null) {
            executeCode.run();
        } else if (consumerCode != null) {
            consumerCode.accept(this);
        }
        return true;
    }

    @Override
    public void subCommandCode(CommandSender sender, Player player, Command command, String cmd, String[] args) {
        this.player = player;
        this.sender = sender;
        this.args = args;
        this.command = command;
        this.cmd = cmd;
    }

    @Override
    public void initialize() {}


    // SUB COMMAND


    public SubCommand createSubCommand(String name, boolean tabCompletable, Consumer<CommandBuilder> code) {
        SubCommand subCommand = new SubCommand(this.getCommandFramework(), name, tabCompletable,this);
        subCommand.setCode(code);
        return subCommand;
    }

    public SubCommand createSubCommand(String name, boolean tabCompletable, Runnable code) {
        SubCommand subCommand = new SubCommand(this.getCommandFramework(), name, tabCompletable,this);
        subCommand.setCode(code);
        return subCommand;
    }

    public SubCommand createSubCommand(String name, boolean tabCompletable) {
        return new SubCommand(this.getCommandFramework(), name, tabCompletable, this);
    }

    public SubCommand createSubCommand(List<String> nameList, boolean tabCompletable, Consumer<CommandBuilder> code) {
        SubCommand subCommand = new SubCommand(this.getCommandFramework(), nameList, tabCompletable, this);
        subCommand.setCode(code);
        return subCommand;
    }

    public SubCommand createSubCommand(List<String> nameList, boolean tabCompletable, Runnable code) {
        SubCommand subCommand = new SubCommand(this.getCommandFramework(), nameList, tabCompletable, this);
        subCommand.setCode(code);
        return subCommand;
    }

    public SubCommand createSubCommand(List<String> nameList, boolean tabCompletable) {
        return new SubCommand(this.getCommandFramework(), nameList, tabCompletable, this);
    }


    // SETTER


    public CommandBuilder setOnCommandCustomCode(Runnable runnable) {
        executeCode = runnable;
        return this;
    }
    public CommandBuilder setOnCommandCustomCode(Consumer<CommandBuilder> consumerCode) {
        this.consumerCode = consumerCode;
        return this;
    }

    public CommandBuilder setCommandPermission(String permission) {
        this.permission = permission;
        setPermission(permission);
        return this;
    }

    public CommandBuilder setCommandAliases(List<String> aliases) {
        this.aliases = aliases;
        setAliases(aliases);
        return this;
    }

    public CommandBuilder setCommandDescription(String description) {
        this.description = description;
        setDescription(description);
        return this;
    }
    public CommandBuilder setCommandUsage(String usage) {
        this.usage = usage;
        setUsage(usage);
        return this;
    }

    public CommandBuilder addSetting(Settings setting) {
        this.settings.add(setting);
        return this;
    }


    // GETTER


    public Player player() {
        return player;
    }

    public CommandSender sender() {
        return sender;
    }

    public Command command() {
        return command;
    }

    public String[] args() {
        return args;
    }

    public String cmd() {
        return cmd;
    }
}
