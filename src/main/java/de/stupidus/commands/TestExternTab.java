package de.stupidus.commands;

import de.stupidus.framework.CommandFramework;
import de.stupidus.tabCompleter.ExternTabCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TestExternTab implements CommandExecutor, TabCompleter {
    private final ExternTabCompleter tabCompleter;

    public TestExternTab() {
        List<String> tabCompleteList = new ArrayList<>();
        tabCompleteList.add("hallo test <[Panda]> test");
        tabCompleteList.add("asd asda aa");
        tabCompleter = new ExternTabCompleter("test", tabCompleteList);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return tabCompleter.onTabComplete(commandSender, command, s, strings);
    }
}
