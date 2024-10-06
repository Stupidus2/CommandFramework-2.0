package de.stupidus.traceCMD;

import de.stupidus.api.Initialize;
import de.stupidus.command.command.Command;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
@Initialize
public class TestCMD extends Command {
    SubCommand subCommand = new SubCommand(getCommandFramework(), "t", true);
    public TestCMD() {
        super("test");
    }

    @Override
    public void initialize() {

    }

    @Override
    public void subCommandCode(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args) {
        subCommand.setCode(() -> player.sendMessage("hi"));
    }
}
