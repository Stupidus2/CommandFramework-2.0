package de.stupidus.commands;

import de.stupidus.api.Initialize;
import de.stupidus.api.Settings;
import de.stupidus.command.command.Command;
import de.stupidus.command.syntax.Syntax;
import de.stupidus.framework.CommandFramework;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Initialize
public class Doin extends Command {

    SubCommand subCommand = new SubCommand(getCommandFramework(), null, true);
    SubCommand testibus = new SubCommand(getCommandFramework(), null, true);

    @Override
    public boolean execute(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args) {
        player.setHealth(20);
        player.sendMessage("§aDu wurdest geheilt!");
        return true;
    }

    public Doin() {
        super("doin");
    }

    @Override
    public void initialize() {
        addSetting(Settings.PLAYER);
        addSetting(Settings.SOUND);
        addSetting(Settings.COMMAND_SYNTAX);
        subCommand.setPermission("sfdghasdgh");
        Syntax syntax = CommandFramework.getSyntax();

        subCommand.addChoose("start asdfsfd");
        subCommand.addChoose("start asdfsfd");
        subCommand.addChoose("start asdfsfd");
        subCommand.addChoose("start sdgsgbdh");
        subCommand.addChoose("stop sdgfsdf");
        subCommand.addChoose("asgfsdg d");
        testibus.addChoose("testibus muss getestet werden vertrau");
        testibus.addChoose("jaja");
        addSetting(Settings.SYNTAX_CLICKABLE);
        syntax.replaceArg(this, subCommand, 2, "player");
    }

    @Override
    public void subCommandCode(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args) {
        subCommand.setCode(() -> setHealth(player, args));
    }

    public void setHealth(Player player, String[] args) {
        if (Bukkit.getPlayer(args[0]) != null) {
            Player aim = Bukkit.getPlayer(args[0]);
            aim.setHealth(20);
            aim.sendMessage("§aYou got healed!");
            player.sendMessage("§aYou healed §e" + aim.getName() + ".");
        } else {
            player.sendMessage("§cThis player doesn't exist!");
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        initialize();
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        initialize();
    }
}
