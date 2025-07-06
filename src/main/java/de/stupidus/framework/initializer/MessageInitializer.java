package de.stupidus.framework.initializer;

import de.stupidus.framework.CommandFramework;
import de.stupidus.messages.Messages;
import de.stupidus.messages.Message;
import de.stupidus.messages.Translation;
import de.stupidus.messages.Translator;

public class MessageInitializer {
    public static void messageInit() {

        Message message = CommandFramework.getMessages();

        //Unknown Command message
        Translator unknownCommandTranslator = new Translator();
        unknownCommandTranslator.setTranslationMessage(Translation.English, "§eCould not find this command");
        unknownCommandTranslator.setTranslationMessage(Translation.German, "§eDieser Command konnte nicht gefunden werden");
        message.newMessage(Messages.UNKNOWN_COMMAND_NAME, unknownCommandTranslator);

        //ONLY PLAYER CAN EXECUTE
        Translator noPlayerTranslator = new Translator();
        noPlayerTranslator.setTranslationMessage(Translation.English, "§eYou have to be a player to execute this command");
        noPlayerTranslator.setTranslationMessage(Translation.German, "§eDu musst ein Spieler sein um diesen Command auszuführen");
        message.newMessage(Messages.NOT_A_PLAYER, noPlayerTranslator);

        //Missing Permission message
        Translator missingPermissionTranslator = new Translator();
        missingPermissionTranslator.setTranslationMessage(Translation.English, "§eYou need following permission: §c%permission_required%");
        missingPermissionTranslator.setTranslationMessage(Translation.German, "§eDu benötigst folgende Berechtigungen: §c%permission_required%");
        message.newMessage(Messages.MISSING_PERMISSION, missingPermissionTranslator);

        //ONLY CONSOLE AND ONLY PLAYER CANNOT BE EXECUTED

        Translator consoleAndPlayerCannotExecute = new Translator();
        consoleAndPlayerCannotExecute.setTranslationMessage(Translation.English, "§eIt's not possible that only console and only players can execute this command");
        consoleAndPlayerCannotExecute.setTranslationMessage(Translation.German, "§eEs ist nicht möglich, dass nur die Konsole und nur Spieler diesen Command diesen Command ausführen können");
        message.newMessage(Messages.ONLY_CONSOLE_ONLY_PLAYER_ERROR, consoleAndPlayerCannotExecute);

        //ONLY CONSOLE CAN EXECUTE

        Translator onlyConsoleExecute = new Translator();
        onlyConsoleExecute.setTranslationMessage(Translation.English, "§eThis is a command witch can only be executed by the console");
        onlyConsoleExecute.setTranslationMessage(Translation.German, "§eDieser Command kann nur von der Konsole ausgeführt werden");
        message.newMessage(Messages.ONLY_CONSOLE_COMMAND, onlyConsoleExecute);

        Translator noCodeSetError = new Translator();
        noCodeSetError.setTranslationMessage(Translation.English, "§eCurrently there is no code set to this subCommand");
        noCodeSetError.setTranslationMessage(Translation.German, "§eDerzeit ist kein Code für diesen SubCommand gesetzt");
        message.newMessage(Messages.NO_CODE_SET, noCodeSetError);

        Translator bothCodeSetError = new Translator();
        bothCodeSetError.setTranslationMessage(Translation.English, "§eYou're not allowed to use both 'setCode' methods");
        bothCodeSetError.setTranslationMessage(Translation.German, "§eDu darfst nicht beide 'setCode' Methoden verwenden");
        message.newMessage(Messages.BOTH_CODE_CANNOT_SET, bothCodeSetError);
    }
}
