package de.stupidus.commands;

import de.stupidus.api.Initialize;
import de.stupidus.api.Settings;
import de.stupidus.command.Code;
import de.stupidus.command.Command;
import de.stupidus.framework.CommandFramework;
import de.stupidus.framework.Initializer;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
@Initialize
public class Panda extends Command {

    SubCommand panda = new SubCommand(getCommandFramework(), "spawn", true);

    SubCommand schem = new SubCommand(getCommandFramework(),null, true);

    public Panda() {
        super("panda");
    }

    @Override
    public boolean execute(CommandSender commandSender, org.bukkit.command.Command command, String cmd, String[] args) {
        return true;
    }
    @Override
    public void initialize() {
        panda.setPermission("PANDASPAWN");
        setPermission("TEST");
        panda.addSetting(Settings.PLAYER);

    }

    @Override
    public void subCommandCode(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args) {
        panda.setCode(new Code() {
            @Override
            public void functionToExecute() {
                setPanda(player, args);
            }
        });
    }
    public void setPanda(Player player, String[] args) {
        player.sendMessage("TEST");
    }
}
