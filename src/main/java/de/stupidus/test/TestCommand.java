package de.stupidus.test;

import de.stupidus.api.InitializeMethod;
import de.stupidus.command.command.CommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class TestCommand implements Listener {

    @InitializeMethod
    public void initialize() {
        CommandBuilder commandBuilder = new CommandBuilder("panda")
                .setCommandPermission("Hallo")
                .createSubCommand("Hallo", true, (b) -> b.player().sendMessage("Hallo")).getCommandBuilder()
                .createSubCommand("df", true, () -> Bukkit.broadcastMessage("Hal")).getCommandBuilder()
                .setOnCommandCustomCode((b) -> b.player().sendMessage("Es geht"))
                .build(this)
                .setCommandUsage("/Test");
    }
}
