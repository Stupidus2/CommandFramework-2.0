package de.stupidus.framework.initializer;

import de.stupidus.framework.CommandFramework;
import de.stupidus.messages.Messages;
import de.stupidus.messages.Message;
import de.stupidus.messages.Translation;
import de.stupidus.messages.Translator;
import org.bukkit.ChatColor;

public class MessageInitializer {
    public static void messageInit() {

        Message message = CommandFramework.getMessages();

        //Unknown Command message
        Translator unknownCommandTranslator = new Translator();
        unknownCommandTranslator.setTranslationMessage(Translation.English, ChatColor.YELLOW + "Could not find this command");
        unknownCommandTranslator.setTranslationMessage(Translation.German, ChatColor.YELLOW + "Dieser Command konnte nicht gefunden werden");
        message.newMessage(Messages.UNKNOWN_COMMAND_NAME, unknownCommandTranslator);

        //ONLY PLAYER CAN EXECUTE
        Translator noPlayerTranslator = new Translator();
        noPlayerTranslator.setTranslationMessage(Translation.English, ChatColor.YELLOW + "You have to be a player to execute this command");
        noPlayerTranslator.setTranslationMessage(Translation.German, ChatColor.YELLOW + "Du musst ein Spieler sein um diesen Command auszuführen");
        message.newMessage(Messages.NOT_A_PLAYER, noPlayerTranslator);

        //Missing Permission message
        Translator missingPermissionTranslator = new Translator();
        missingPermissionTranslator.setTranslationMessage(Translation.English, ChatColor.YELLOW + "You need following permission: " + ChatColor.RED + "%permission_required%");
        missingPermissionTranslator.setTranslationMessage(Translation.German, ChatColor.YELLOW + "Du benötigst folgende Berechtigungen: " + ChatColor.RED + "%permission_required%");
        message.newMessage(Messages.MISSING_PERMISSION, missingPermissionTranslator);

        //ONLY CONSOLE AND ONLY PLAYER CANNOT BE EXECUTED

        Translator consoleAndPlayerCannotExecute = new Translator();
        consoleAndPlayerCannotExecute.setTranslationMessage(Translation.English, ChatColor.YELLOW + "It's not possible that only console and only players can execute this command");
        consoleAndPlayerCannotExecute.setTranslationMessage(Translation.German, ChatColor.YELLOW + "Es ist nicht möglich, dass nur die Konsole und nur Spieler diesen Command diesen Command ausführen können");
        message.newMessage(Messages.ONLY_CONSOLE_ONLY_PLAYER_ERROR, consoleAndPlayerCannotExecute);

        //ONLY CONSOLE CAN EXECUTE

        Translator onlyConsoleExecute = new Translator();
        onlyConsoleExecute.setTranslationMessage(Translation.English, ChatColor.YELLOW + "This is a command witch can only be executed by the console");
        onlyConsoleExecute.setTranslationMessage(Translation.German, ChatColor.YELLOW + "Dieser Command kann nur von der Konsole ausgeführt werden");
        message.newMessage(Messages.ONLY_CONSOLE_COMMAND, onlyConsoleExecute);

        Translator noCodeSetError = new Translator();
        noCodeSetError.setTranslationMessage(Translation.English, ChatColor.YELLOW + "Currently there is no code set to this subCommand");
        noCodeSetError.setTranslationMessage(Translation.German, ChatColor.YELLOW + "Derzeit ist kein Code für diesen SubCommand gesetzt");
        message.newMessage(Messages.NO_CODE_SET, noCodeSetError);

        Translator bothCodeSetError = new Translator();
        bothCodeSetError.setTranslationMessage(Translation.English, ChatColor.YELLOW + "You're not allowed to use both 'setCode' methods");
        bothCodeSetError.setTranslationMessage(Translation.German, ChatColor.YELLOW + "Du darfst nicht beide 'setCode' Methoden verwenden");
        message.newMessage(Messages.BOTH_CODE_CANNOT_SET, bothCodeSetError);
    }
}
