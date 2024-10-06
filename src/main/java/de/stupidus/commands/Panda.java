package de.stupidus.commands;

import de.stupidus.api.Initialize;
import de.stupidus.api.Settings;
import de.stupidus.command.command.Command;
import de.stupidus.command.others.Code;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
@Initialize
public class Panda extends Command {

    SubCommand pa = new SubCommand(getCommandFramework(), "as", true);
    public Panda() {
        super("panda");
    }


    @Override
    public void initialize() {
        addSetting(Settings.SOUND);
    }

    @Override
    public void subCommandCode(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args) {
        pa.setCode(new Code() {
            @Override
            public void functionToExecute() {
                test(player);
            }
        });
    }
    private void test(Player sender) {
        sender.sendMessage("adsasd asd ");
    }
}
