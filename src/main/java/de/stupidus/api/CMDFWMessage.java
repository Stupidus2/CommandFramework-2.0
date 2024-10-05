package de.stupidus.api;

import de.stupidus.messages.Messages;
import de.stupidus.messages.Translator;

public interface CMDFWMessage {

    /**
     * Retrieves a translated message based on the provided message key.
     *
     * @param message The message key, usually from the CMDWMessages class, that identifies which message to retrieve.
     * @return A {@link Translator} object containing the translated message as a String.
     */
    Translator getMessage(Messages message);

    /**
     * Retrieves the message manager responsible for handling messages.
     *
     * @param message The message key, typically from the CMDWMessages class, that identifies the message.
     * @return The {@link CMDFWMessageManager} that manages the specified message.
     */
    CMDFWMessageManager getMessageManager(Messages message);

    /**
     * Creates a new message in the system.
     * <p>
     * This method is used exclusively for adding new messages. If a message with the same key already exists, this method should not be used for editing it.
     *
     * @param messageKey The key that identifies the message, typically set using the CMDFWMessages class.
     * @param translator The default message to be set, provided as a {@link Translator} object.
     */
    void newMessage(Messages messageKey, Translator translator);
}
