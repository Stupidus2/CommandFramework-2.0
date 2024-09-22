package de.stupidus.api;

import de.stupidus.msg.Message;
import de.stupidus.msg.Translator;

public interface CMDFWMessageManager {

    /**
     * Edit a message
     * @param translator translated message
     */
    Message setMessage(Translator translator);

}
