package de.stupidus.msg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Translator {

    private HashMap<String, String> translatedMessage = new HashMap<>();

    public void sendMessage(CommandSender sender) {
        if (translatedMessage.containsKey("en") && translatedMessage.containsKey("de")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String lang = player.getLocale();
                String[] langArray = lang.split("_");
                String message = translatedMessage.getOrDefault(langArray[0], translatedMessage.get("en"));

                player.sendMessage(message);
            } else {
                sender.sendMessage(translatedMessage.get("en"));
            }
        } else {
            sender.sendMessage(ChatColor.RED+"You have to set a german and english translator");
        }
    }

    public void sendMessage(Player player) {
        if (translatedMessage.containsKey("en") && translatedMessage.containsKey("de")) {
            String lang = player.getLocale();
            String[] langArray = lang.split("_");
            String message = translatedMessage.getOrDefault(langArray[0], translatedMessage.get("en"));

            player.sendMessage(message);
        } else {
            player.sendMessage(ChatColor.RED+"You have to set a german and english translator");
        }
    }

    public void setTranslationMessage(Translation translation, String message) {
        if (translation == Translation.English) {
            translatedMessage.put("en", message);
        } else if (translation == Translation.German) {
            translatedMessage.put("de", message);
        }
    }

    public HashMap<String, String> getTranslatedMessages() {
        return translatedMessage;
    }

    public String getTranslatedMessage(CommandSender sender) {
        if (translatedMessage.containsKey("en") && translatedMessage.containsKey("de")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String lang = player.getLocale();
                String[] langArray = lang.split("_");

                return translatedMessage.getOrDefault(langArray[0], translatedMessage.get("en"));
            }
            return translatedMessage.get("en");
        }
        return ChatColor.RED+"You have to set a german and english translator";
    }

    public String getTranslatedMessage(Player player) {
        if (translatedMessage.containsKey("en") && translatedMessage.containsKey("de")) {
            String lang = player.getLocale();
            String[] langArray = lang.split("_");
            String message = translatedMessage.getOrDefault(langArray[0], translatedMessage.get("en"));

            return translatedMessage.get(message);
        }
        return ChatColor.RED+"You have to set a german and english translator";
    }
}
