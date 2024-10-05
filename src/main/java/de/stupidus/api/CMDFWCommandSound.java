package de.stupidus.api;

import org.bukkit.Sound;

public interface CMDFWCommandSound {

    /**
     * Sets the sound to be played upon a successful event.
     *
     * @param sound The sound to be used for the success event.
     *              It should be a valid Minecraft sound provided by the
     *              {@link org.bukkit.Sound} class.
     *              For example, {@link org.bukkit.Sound#ENTITY_PLAYER_LEVELUP}.
     */
    void setSuccessSound(Sound sound);

    /**
     * Sets the sound to be played upon a failed event.
     *
     * @param sound The sound to be used for the failure event.
     *              It should be a valid Minecraft sound from the
     *              {@link org.bukkit.Sound} class.
     *              For example, {@link org.bukkit.Sound#ENTITY_ELDER_GUARDIAN_CURSE}.
     */
    void setFailSound(Sound sound);

    /**
     * Retrieves the sound that is set to play for a successful event.
     *
     * @return The sound configured for success events,
     *         or null if no sound has been set.
     */
    Sound getSuccessSound();

    /**
     * Retrieves the sound that is set to play for a failed event.
     *
     * @return The sound configured for failure events,
     *         or null if no sound has been set.
     */
    Sound getFailureSound();
}
