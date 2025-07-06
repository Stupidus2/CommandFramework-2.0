package de.stupidus.command.command;

import de.stupidus.api.Settings;
import de.stupidus.framework.CommandFramework;

import java.util.List;

public abstract class Command extends BaseCommand {

    public Command(String name) {
        super(name);
        commandFramework.addCommand(this,this);
    }


    //GETTER AND SETTER

    public void addSetting(Settings setting) {
        this.settings.add(setting);
    }

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
    public void setCommandUsage(String usage) {
        this.usage = usage;
        setUsage(usage);
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
}