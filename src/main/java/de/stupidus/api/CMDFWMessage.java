package de.stupidus.api;

import de.stupidus.msg.Translator;

public interface CMDFWMessage {

    /**
     * Get a message
     * @param message Use CMDWMessages class to get the message key
     * @return Returns the message as a String
     */
    Translator getMessage(Messages message);

    /**
     * Get the messageManager
     * @param message Use CMDWMessages class to get the message name
     * @return Returns messageManager
     */
    CMDFWMessageManager getMessageManager(Messages message);

    /**
     * Create a new message. Only used for creating new messages not editing already existing.
     * @param messageKey Use CMDFWMessages class to set the message key
     * @param translator Set the default message
     */
    void newMessage(Messages messageKey, Translator translator);

}
