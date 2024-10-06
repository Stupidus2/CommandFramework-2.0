package de.stupidus.sound;

import de.stupidus.api.CMDFWCommandSound;
import org.bukkit.Sound;

public class CommandSound implements CMDFWCommandSound {
    private Sound successSound = Sound.ENTITY_PLAYER_LEVELUP;
    private Sound failureSound = Sound.ENTITY_ELDER_GUARDIAN_CURSE;

    @Override
    public void setSuccessSound(Sound sound) {
        successSound = sound;
    }

    @Override
    public void setFailureSound(Sound sound) {
        failureSound = sound;
    }

    @Override
    public Sound getSuccessSound() {
        return successSound;
    }

    @Override
    public Sound getFailureSound() {
        return failureSound;
    }
}
