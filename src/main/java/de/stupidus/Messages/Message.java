package de.stupidus.Messages;

import de.stupidus.api.CMDFWMessageManager;
import de.stupidus.api.Messages;
import de.stupidus.api.CMDFWMessage;

import java.util.HashMap;

public class Message implements CMDFWMessage {
    private MessageManager messageManager;

    private HashMap<Messages, String> messages;
    public Message(HashMap<Messages, String> messages) {
        this.messages = messages;
    }

    @Override
    public String getMessage(Messages message) {
        return messages.get(message);
    }
    @Override
    public CMDFWMessageManager getMessageManager(Messages message) {
        return messageManager = new MessageManager(message, this.messages);
    }

    @Override
    public void newMessage(Messages messageKey, String message) {
        messages.putIfAbsent(messageKey, message);
    }
}
