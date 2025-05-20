package presentation.utils;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/*
 * SoundManager.java
 * This class manages background music and sound effects for the game.
 * It allows playing, stopping, and looping sounds.
 */

public class SoundManager {
    private Clip backgroundMusic;
    private Map<String, Clip> soundEffects;


    /*
     * Constructor initializes the sound effects map.
     */

    public SoundManager() {
        soundEffects = new HashMap<>();
    }

    /*
     * Plays background music from the specified resource path.
     * If music is already playing, it stops it before starting the new one.
     */

    public void playBackgroundMusic(String resourcePath) {
        stopBackgroundMusic();

        try {

            InputStream inputStream = getClass().getResourceAsStream(resourcePath);
            if (inputStream == null) {
                System.err.println("Could not find resource: " + resourcePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Stops the background music if it is currently playing.
     */

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }

    /*
     * Plays a sound effect from the specified resource path.
     * If the sound effect is already loaded, it reuses the existing clip.
     */

    public void playSoundEffect(String name, String resourcePath) {
        try {
            if (!soundEffects.containsKey(name)) {
                InputStream inputStream = getClass().getResourceAsStream(resourcePath);
                if (inputStream == null) {
                    System.err.println("Could not find resource: " + resourcePath);
                    return;
                }

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                soundEffects.put(name, clip);
            }
            Clip clip = soundEffects.get(name);
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopAllSounds() {
        stopBackgroundMusic();
        for (Clip clip : soundEffects.values()) {
            if (clip.isRunning()) {
                clip.stop();
                clip.close();
            }
        }
        soundEffects.clear();
    }
}