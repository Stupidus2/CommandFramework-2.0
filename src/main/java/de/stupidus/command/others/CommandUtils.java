package de.stupidus.command.others;

import de.stupidus.api.Settings;
import de.stupidus.command.command.CommandBuilder;
import de.stupidus.framework.CommandFramework;
import de.stupidus.messages.Message;
import de.stupidus.messages.Messages;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommandUtils {

    //CHECK IF PLAYER CAN EXECUTE COMMAND

    public static boolean checkPermissionsAndSettings(CommandSender sender, String permission, List<Settings> settings, boolean sendMessage, SubCommand subCommand) {

        Message message = CommandFramework.getMessages();

        if (permission != null && !sender.hasPermission(permission)) {
            if (sendMessage)
                sender.sendMessage(message.getMessage(Messages.MISSING_PERMISSION).getTranslatedMessage(sender).replace("%permission_required%", permission));
            return false;
        }

        if (settings.contains(Settings.PLAYER) && settings.contains(Settings.ONLY_CONSOLE_EXECUTABLE)) {
            if (sendMessage)
                sender.sendMessage(message.getMessage(Messages.ONLY_CONSOLE_ONLY_PLAYER_ERROR).getTranslatedMessage(sender));
            return false;
        }

        if (settings.contains(Settings.ONLY_CONSOLE_EXECUTABLE) && !(sender instanceof ConsoleCommandSender)) {
            if (sendMessage)
                sender.sendMessage(message.getMessage(Messages.ONLY_CONSOLE_COMMAND).getTranslatedMessage(sender));
            return false;
        }

        if (settings.contains(Settings.PLAYER) && !(sender instanceof Player)) {
            if (sendMessage) sender.sendMessage(message.getMessage(Messages.NOT_A_PLAYER).getTranslatedMessage(sender));
            return false;
        }
        if (subCommand != null) {

            Code code = subCommand.getCode();
            Runnable runnableCode = subCommand.getRunnableCode();
            Consumer<CommandBuilder> consumerCode = subCommand.getConsumerCode();

            //CHECK IF BOTH CODE ARE SET OR BOTH CODE ARE NOT SET

            if (code != null && runnableCode != null) {
                sender.sendMessage(message.getMessage(Messages.BOTH_CODE_CANNOT_SET).getTranslatedMessage(sender));
                return true;
            } else if (code == null && runnableCode == null && consumerCode == null) {
                sender.sendMessage(message.getMessage(Messages.NO_CODE_SET).getTranslatedMessage(sender));
                return true;
            }
        }

        return true;
    }

    //GENERATE ARG LIST FOR SUBCOMMAND PARAMETERS
    public static List<Integer> generateArgLengthList(String subCommandName) {
        return IntStream.range(0, subCommandName.split(" ").length).filter(i -> subCommandName.split(" ")[i].matches("<\\[.*]>")).boxed().collect(Collectors.toList());
    }
}
