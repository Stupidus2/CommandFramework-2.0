package de.stupidus.commands;

import de.stupidus.api.Initialize;
import de.stupidus.command.Code;
import de.stupidus.command.Command;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
@Initialize
public class Test2 extends Command {

    SubCommand subCommand = new SubCommand(getCommandFramework(), "test", true);
    public Test2() {
        super("test2");
    }

    @Override
    public void subCommandInitialize() {}

    @Override
    public void subCommandCode(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args) {
        subCommand.setCode(new Code() {
            @Override
            public void functionToExecute() {
                test(player);
            }
        });
    }

    public void test(Player player) {
        player.sendMessage("hi");
    }
}
