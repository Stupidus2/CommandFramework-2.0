package de.stupidus.main;

import de.stupidus.framework.CommandFramework;
import de.stupidus.framework.initializer.Initializer;
import de.stupidus.sound.CommandSound;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {


    @Override
    public void onEnable() {
        Initializer initializer = CommandFramework.getInitializer();
        initializer.addPackage("de.stupidus.traceCMD");
        initializer.addPackage("de.stupidus.commands");
        initializer.register(this);
        CommandSound commandSound = CommandFramework.getCommandSound();
        commandSound.setFailureSound(Sound.ITEM_GOAT_HORN_SOUND_0);
        commandSound.setSuccessSound(Sound.ITEM_GOAT_HORN_SOUND_1);
    }
}
