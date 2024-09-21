package de.stupidus.commands;

import de.stupidus.api.Initialize;
import de.stupidus.command.Code;
import de.stupidus.command.Command;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
@Initialize
public class Panda extends Command {

    SubCommand panda = new SubCommand(getCommandFramework(), "spawn", true);

    public Panda() {
        super("panda");
    }

    @Override
    public boolean execute(CommandSender commandSender, org.bukkit.command.Command command, String cmd, String[] args) {
        commandSender.sendMessage("Hi");
        return true;
    }

    @Override
    public void subCommandInitialize() {}

    @Override
    public void subCommandCode(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args) {
        panda.setCode(new Code() {
            @Override
            public void functionToExecute() {
                setPanda(player);
            }
        });
    }
    public void setPanda(Player player) {
        player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.PANDA);
    }
}
