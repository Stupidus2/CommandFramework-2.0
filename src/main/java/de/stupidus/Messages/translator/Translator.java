package de.stupidus.Messages.translator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Translator {

    private HashMap<String, String> translatedMessage = new HashMap<>();

    public void sendMessage(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String lang = player.getLocale();
            String[] langArray = lang.split("_");
            String message = translatedMessage.getOrDefault(langArray[0], translatedMessage.get("en"));

            player.sendMessage(message);
        } else {
            sender.sendMessage(translatedMessage.get("en"));
        }
    }
    public void sendMessage(Player player) {
            String lang = player.getLocale();
            String[] langArray = lang.split("_");
            String message = translatedMessage.getOrDefault(langArray[0], translatedMessage.get("en"));

            player.sendMessage(message);
    }
    public void addTranslationMessage (Translation translation, String message){
        if (translation == Translation.English) {
            translatedMessage.put("en", message);
        } else if (translation == Translation.German) {
            translatedMessage.put("de", message);
        }
    }
}
