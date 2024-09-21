package de.stupidus.api;

import de.stupidus.Messages.Message;

public interface CMDFWMessageManager {

    /**
     * Edit a message
     * @param message Set a new message
     */
    Message setMessage(String message);

}
