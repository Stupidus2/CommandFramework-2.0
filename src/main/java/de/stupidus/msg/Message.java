package de.stupidus.msg;

import de.stupidus.api.CMDFWMessageManager;
import de.stupidus.api.Messages;
import de.stupidus.api.CMDFWMessage;

import java.util.HashMap;

public class Message implements CMDFWMessage {

    private MessageManager messageManager;
    private HashMap<Messages, Translator> messages;

    public Message(HashMap<Messages, Translator> messages) {
        this.messages = messages;
    }

    @Override
    public Translator getMessage(Messages message) {
        return messages.get(message);
    }

    @Override
    public CMDFWMessageManager getMessageManager(Messages message) {
        if (messageManager == null) {
            return messageManager = new MessageManager(message, this.messages);
        }
        return messageManager;
    }

    @Override
    public void newMessage(Messages messageKey, Translator translator) {
        messages.putIfAbsent(messageKey, translator);
    }
}
