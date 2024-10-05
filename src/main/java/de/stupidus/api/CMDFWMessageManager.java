package de.stupidus.api;

import de.stupidus.messages.Message;
import de.stupidus.messages.Translator;

public interface CMDFWMessageManager {

    /**
     * Edits the current message with a new translated message.
     *
     * @param translator A {@link Translator} object that contains the language.
     *                   The translator can be used to manage different language versions of the message.
     * @return The updated {@link Message} object with the new translated content.
     * Example:
     * <pre>
     * Message newMessage = message.setMessage(translator);
     * </pre>
     */
    Message setMessage(Translator translator);

}
