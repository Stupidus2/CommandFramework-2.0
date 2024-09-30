package de.stupidus.commands;

import de.stupidus.api.Initialize;
import de.stupidus.api.Settings;
import de.stupidus.command.Code;
import de.stupidus.command.Command;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Initialize
public class Panda extends Command {

    SubCommand panda = new SubCommand(getCommandFramework(),null, true);

    public Panda() {
        super("panda");
    }

    @Override
    public void initialize() {
        setPermission("TEST");
        panda.addSetting(Settings.PLAYER);
        panda.setPermission("PANDASPAWN");
        panda.addChoose("jaaaaa <[sdf]> <[sdf]> <[sdf]>");
        panda.addChoose("jaaaaa <[sdf]>");
        panda.addChoose("dfgvf dfgvfg");
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
