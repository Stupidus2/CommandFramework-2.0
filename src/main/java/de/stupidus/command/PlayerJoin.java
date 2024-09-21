package de.stupidus.command;

import de.stupidus.framework.CommandFramework;
import de.stupidus.framework.Initializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoin implements Listener {
    private CommandFramework commandFramework;
    public PlayerJoin() {
    }
    public PlayerJoin(CommandFramework commandFramework) {
        this.commandFramework = commandFramework;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (commandFramework != null) {
            Initializer initializer = CommandFramework.getInitializer();
            commandFramework.getCommandManager().clearSubCommand();
            initializer.register(null);
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (commandFramework != null) {
            Initializer initializer = CommandFramework.getInitializer();
            commandFramework.getCommandManager().clearSubCommand();
            initializer.register(null);
        }
    }
}
