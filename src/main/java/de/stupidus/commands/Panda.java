package de.stupidus.commands;

import de.stupidus.api.Initialize;
import de.stupidus.api.Settings;
import de.stupidus.command.others.Code;
import de.stupidus.command.command.Command;
import de.stupidus.command.syntax.Syntax;
import de.stupidus.framework.CommandFramework;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Initialize
public class Panda extends Command {
    SubCommand save = new SubCommand(getCommandFramework(),null,true);
    Syntax syntax = CommandFramework.getSyntax();
    public Panda() {
        super("panda");
    }

    @Override
    public void initialize() {
        save.addSetting(Settings.PLAYER);
        addSetting(Settings.COMMAND_SYNTAX);
        addSetting(Settings.SOUND);
        save.addChoose("gvvv asdfs");
        save.addChoose("gvvdfv asdasdsda");
        syntax.replaceArg("panda", 2, "Test");
    }

    @Override
    public void subCommandCode(CommandSender sender, Player player, org.bukkit.command.Command command, String s, String[] args) {
        save.setCode(() -> panda(sender));
    }

    public void panda(CommandSender sender) {
        sender.sendMessage("Test");
    }
}

