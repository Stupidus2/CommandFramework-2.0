package de.stupidus.test;

import de.stupidus.api.InitializeMethod;
import de.stupidus.command.command.CommandBuilder;

public class CommandTest {

    @InitializeMethod
    public void initialize() {
        new CommandBuilder("hallo")
                .createSubCommand("test <[var]> [cv]", true, (b) -> b.player().sendMessage(b.getText())).getCommandBuilder()
                .build(this, false);
    }

}
