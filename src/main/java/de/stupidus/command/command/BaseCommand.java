package de.stupidus.command.command;

import de.stupidus.api.CMDFWCommand;
import de.stupidus.api.Settings;
import de.stupidus.command.others.Code;
import de.stupidus.command.others.CommandUtils;
import de.stupidus.command.syntax.Syntax;
import de.stupidus.framework.CommandFramework;
import de.stupidus.framework.initializer.Initializer;
import de.stupidus.messages.Message;
import de.stupidus.messages.Messages;
import de.stupidus.sound.CommandSound;
import de.stupidus.subCommand.SubCommand;
import de.stupidus.tabCompleter.CustomTabCompleter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class BaseCommand extends Command implements CMDFWCommand, Listener, CommandExecutor, TabCompleter {

    //VARIABLES AND CONSTRUCTOR
    protected CommandFramework commandFramework;
    protected ArrayList<SubCommand> subCommands;
    protected CustomTabCompleter commandTabCompleter;

    protected String name;
    protected String permission = null;
    protected List<Settings> settings = new ArrayList<>();
    protected Initializer initializer = CommandFramework.getInitializer();
    protected List<String> aliases = null;
    protected String description = null;
    protected String usage;
    protected String className;
    protected boolean normalRegistration;
    protected String text;

    public BaseCommand(String name) {
        super(name);
        whereCreated();
        this.name = name;
        this.commandFramework = new CommandFramework();
        this.subCommands = commandFramework.getCommandManager().getSubCommands();
        this.commandTabCompleter = new CustomTabCompleter(subCommands);

        if (permission != null) setPermission(permission);
        if (aliases != null) setAliases(aliases);
        if (description != null) setDescription(description);
    }


    // Class Creation


    public void whereCreated() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();

        for (StackTraceElement element : trace) {
            String className = element.getClassName();

            if (!className.startsWith("java.") && !className.equals("de.stupidus.command.command.CommandBuilder") && !className.equals("de.stupidus.command.command.BaseCommand") && !className.equals("de.stupidus.command.command.Command")) {

                this.className = className;
                break;
            }
        }
    }

    public String getCommandClassName() {
        return className;
    }

    public Class<?> getCommandClass() throws ClassNotFoundException {
        try {
            return Class.forName(className);
        } catch (Exception ignore) {
        }
        return null;
    }


    // EXECUTE

    private final Message message = CommandFramework.getMessages();
    private Syntax syntaxCreator = CommandFramework.getSyntax();
    private CommandSound commandSound = CommandFramework.getCommandSound();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return commandLogic(args, sender, s, command);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        return commandLogic(args, sender, s, this);
    }


    private boolean commandLogic(String[] args, CommandSender sender, String s, Command command) {
        if (args.length == 0) {
            if (initializer.containsExecuteMethod(this)) {
                if (!settings.contains(Settings.NO_INITIALIZE_UPDATE_ON_COMMAND_EXECUTE)) initialize();
                return checkCommand(sender, command, s, args);
            }
        }

        String commandString = String.join(" ", args);

        for (SubCommand subCommand : subCommands) {
            if (!settings.contains(Settings.NO_INITIALIZE_UPDATE_ON_COMMAND_EXECUTE)) initialize();

            for (String name : subCommand.getNameList().keySet()) {

                if (name == null) continue;

                if (!subCommand.getNameList().get(name).isEmpty()) {
                    if (sender instanceof Player player && !subCommand.getNameList().get(name).contains(player.getUniqueId()))
                        continue;
                }

                if (subCommand.isBanned(name, sender instanceof Player player ? player.getUniqueId() : null)) continue;

                if (!syntaxCreator.contains(getName() + " " + name))
                    syntaxCreator.addCommandString(getName() + " " + name);

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
                if (!isMatch && !subCommand.containsText(name)) continue;

                // TEXT CHECK

                if (subCommand.containsText(name)) {
                    int originLength = nameArray.length - 1;

                    if (args.length > originLength) {

                        text = Arrays.stream(args).skip(originLength).collect(Collectors.joining(" "));

                        return checkSubCommand(sender, subCommand, command, s, args);
                    }

                    // WITHOUT TEXT CHECK

                } else {

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
                            return checkSubCommand(sender, subCommand, command, s, args);
                        }
                    } else {

                        if (args.length == commandLength && (args[commandLength - 1].equalsIgnoreCase(name) || subCommand.containsVarArg())) {
                            return checkSubCommand(sender, subCommand, command, s, args);
                        }
                    }
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
            Player player = sender instanceof Player ? (Player) sender : null;

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
            Consumer<CommandBuilder> consumerCode = subCommand.getConsumerCode();

            //INITIALIZE

            if (code != null) {
                code.functionToExecute();
            }
            if (runnableCode != null) {
                runnableCode.run();
            } else if (consumerCode != null) {
                consumerCode.accept(subCommand.getCommandBuilder());
            }
            if (getSettings().contains(Settings.SOUND) && sender instanceof Player && commandSound.getSuccessSound() != null)
                ((Player) sender).playSound(((Player) sender).getLocation(), commandSound.getSuccessSound(), 1.0f, 1.0f);
            return true;
        }
        text = null;
        return true;
    }


    //ABSTRACT METHODS


    @Override
    public abstract void initialize();

    @Override
    public abstract void subCommandCode(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args);

    @Override
    public boolean onCommandCustom(CommandSender sender, Player player, org.bukkit.command.Command command, String cmd, String[] args) {
        return true;
    }


    // GETTER


    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public CommandFramework getCommandFramework() {
        return commandFramework;
    }

    @Override
    public List<Settings> getSettings() {
        return settings;
    }

    public CustomTabCompleter getTabCompleter() {
        return commandTabCompleter;
    }

    public boolean getNormalRegistration() {
        return normalRegistration;
    }

    public String getText() {
        return text;
    }


    //TAB COMPLETER


    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return Objects.requireNonNull(commandTabCompleter.onTabComplete(sender, this, alias, args));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return Objects.requireNonNull(commandTabCompleter.onTabComplete(sender, command, s, args));
    }
}