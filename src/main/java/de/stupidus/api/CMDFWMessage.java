package de.stupidus.api;

public interface CMDFWMessage {

    /**
     * Get a message
     * @param message Use CMDWMessages class to get the message key
     * @return Returns the message as a String
     */
    String getMessage(Messages message);

    /**
     * Get the messageManager
     * @param message Use CMDWMessages class to get the message name
     * @return Returns messageManager
     */
    CMDFWMessageManager getMessageManager(Messages message);

    /**
     * Create a new message. Only used for creating new messages not editing already existing.
     * @param messageKey Use CMDFWMessages class to set the message key
     * @param message Set the default message
     */
    void newMessage(Messages messageKey, String message);

}
