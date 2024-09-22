package de.stupidus.msg;

import de.stupidus.api.Messages;

import java.util.HashMap;

public class MessageManager implements de.stupidus.api.CMDFWMessageManager {

    private HashMap<Messages, Translator> messages;
    private Messages messageKey;

    public MessageManager(Messages messageKey, HashMap<Messages, Translator> msgHashMap) {
        this.messages = msgHashMap;
        this.messageKey = messageKey;
    }
    @Override
    public Message setMessage(Translator translator) {
        this.messages.put(messageKey, translator);
        return new Message(this.messages);
    }
}
