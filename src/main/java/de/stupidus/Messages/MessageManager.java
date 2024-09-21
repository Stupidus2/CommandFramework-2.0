package de.stupidus.Messages;

import de.stupidus.api.Messages;
import de.stupidus.framework.CommandFramework;

import java.util.HashMap;

public class MessageManager implements de.stupidus.api.CMDFWMessageManager {

    private HashMap<Messages, String> messages;
    private Messages messageKey;

    public MessageManager(Messages messageKey, HashMap<Messages, String> msgHashMap) {
        this.messages = msgHashMap;
        this.messageKey = messageKey;
    }
    @Override
    public Message setMessage(String message) {
        Message manager = CommandFramework.getMessages();
        this.messages.put(messageKey, message);
        return new Message(this.messages);
    }
}
