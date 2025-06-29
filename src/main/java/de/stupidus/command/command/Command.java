package de.stupidus.command.command;

import de.stupidus.command.others.Code;
import de.stupidus.command.others.CommandUtils;
import de.stupidus.command.syntax.Syntax;
import de.stupidus.messages.Message;
import de.stupidus.messages.Messages;
import de.stupidus.framework.CommandFramework;
import de.stupidus.api.Settings;
import de.stupidus.sound.CommandSound;
import de.stupidus.subCommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class Command extends BaseCommand {

    private final Message message = CommandFramework.getMessages();
    private Syntax syntaxCreator = CommandFramework.getSyntax();
    private CommandSound commandSound = CommandFramework.getCommandSound();

    public Command(String name) {
        super(name);
        commandFramework.addCommand(this);
    }


    //COMMAND SYSTEM


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            if (initializer.containsExecuteMethod(this)) {
                if (!settings.contains(Settings.NO_INITIALIZE_UPDATE_ON_COMMAND_EXECUTE)) initialize();
                return checkCommand(sender, this, s, args);
            }
        }

        String commandString = String.join(" ", args);

        for (SubCommand subCommand : subCommands) {
            if (!settings.contains(Settings.NO_INITIALIZE_UPDATE_ON_COMMAND_EXECUTE)) initialize();

            for (String name : subCommand.getNameList().keySet()) {
                if (!subCommand.getNameList().get(name).isEmpty()) {
                    if (!(sender instanceof Player)) {
                        continue;
                    }
                    Player player = (Player) sender;
                    if (!subCommand.getNameList().get(name).contains(player.getUniqueId())) {
                        continue;
                    }
                }

                if (name == null) continue;
                if (!syntaxCreator.contains(getName() + " " + name)) syntaxCreator.addCommandString(getName() + " " + name);

                String[] nameArray = name.split(" ");
                if (subCommand.containsVarArg()) {
                    for (int i = 0; i < nameArray.length; i++) {
                        if (i < args.length && nameArray[i].startsWith("<[") && nameArray[i].endsWith("]>")) {
                            nameArray[i] = args[i];
                        }
                    }
                }
                String nameArrayString = String.join(" ", nameArray);

                boolean isMatch = args.length == nameArray.length && nameArrayString.equalsIgnoreCase(commandString);
                if (!isMatch) continue;

                List<Integer> argLengthList = CommandUtils.generateArgLengthList(name);
                String[] subParts = name.split(" ");
                int commandLength = subParts.length;

                if (commandLength > 1) {
                    boolean argumentsMatch = true;

                    for (int i = 0; i < commandLength; i++) {
                        if (i >= args.length || !args[i].equalsIgnoreCase(subParts[i])) {
                            if (subCommand.containsVarArg() && !argLengthList.contains(i)) {
                                argumentsMatch = false;
                                break;
                            }
                        }
                    }

                    if (argumentsMatch) {
                        return checkSubCommand(sender, subCommand, this, s, args);
                    }
                } else if (args.length == commandLength && (args[commandLength - 1].equalsIgnoreCase(name) || subCommand.containsVarArg())) {
                    return checkSubCommand(sender, subCommand, this, s, args);
                }
            }
        }
        //SYNTAX IMPLEMENT / NO COMMAND FOUND MSG

        if (getSettings().contains(Settings.SOUND) && sender instanceof Player && commandSound.getFailureSound() != null)
            ((Player) sender).playSound(((Player) sender).getLocation(), commandSound.getFailureSound(), 1.0f, 1.0f);

        if (getSettings().contains(Settings.COMMAND_SYNTAX)) {
            if (!settings.contains(Settings.NO_INITIALIZE_UPDATE_ON_COMMAND_EXECUTE)) initialize();
            syntaxCreator.sendSyntax(sender, getName() + " " + commandString, getSettings().contains(Settings.SYNTAX_CLICKABLE));
            return true;
        }
        sender.sendMessage(message.getMessage(Messages.UNKNOWN_COMMAND_NAME).getTranslatedMessage(sender));
        return true;
    }

    //CHECK COMMAND
    private boolean checkCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {

        //CHECKS
        if (CommandUtils.checkPermissionsAndSettings(sender, getPermission(), this.settings, true, null)) {

            //GET PLAYER
            Player player = sender instanceof Player ? (Player) sender:null;

            //INITIALIZE COMMAND
            onCommandCustom(sender, player, command, s, args);
            if (getSettings().contains(Settings.SOUND) && sender instanceof Player)
                ((Player) sender).playSound(((Player) sender).getLocation(), commandSound.getSuccessSound(), 1.0f, 1.0f);
            return true;
        }
        return true;
    }



    //CHECK SUBCOMMAND
    private boolean checkSubCommand(CommandSender sender, SubCommand subCommand, org.bukkit.command.Command command, String s, String[] args) {
        List<Settings> settingsList = subCommand.getSettings();
        Player player = sender instanceof Player ? (Player) sender : null;
        subCommandCode(sender, player, command, s, args);
        //CHECKS
        if (CommandUtils.checkPermissionsAndSettings(sender, subCommand.getPermission(), subCommand.getSettings(), true, subCommand)) {

            //INITIALIZING SUBCOMMANDS
            Code code = subCommand.getCode();
            Runnable runnableCode = subCommand.getRunnableCode();

            //INITIALIZE

            if (code != null) {
                code.functionToExecute();
            }
            if (runnableCode != null) {
                runnableCode.run();
            }
            if (getSettings().contains(Settings.SOUND) && sender instanceof Player && commandSound.getSuccessSound() != null)
                ((Player) sender).playSound(((Player) sender).getLocation(), commandSound.getSuccessSound(), 1.0f, 1.0f);
            return true;
        }
        return true;
    }
}