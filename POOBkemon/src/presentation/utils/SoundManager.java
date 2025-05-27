package presentation.utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/*
 * SoundManager.java
 * This class manages background music and sound effects for the game.
 * It allows playing, stopping, and looping sounds.
 */

public class SoundManager {
    private Clip backgroundMusic;
    private final Map<String, Clip> soundEffects;
    private boolean isMuted = false;
    private float volume = 0.2f;
    private long pausePosition = 0;
    private boolean isPaused = false;

    /*
     * Constructor initializes the sound effects map.
     */
    public SoundManager() {
        soundEffects = new HashMap<>();
        loadSoundEffects();
    }

    private void loadSoundEffects() {
        // Load sound effects
        loadSoundEffect("attack", "/resources/sounds/attack.wav");
        loadSoundEffect("switch", "/resources/sounds/switch.wav");
        loadSoundEffect("item", "/resources/sounds/item.wav");
        loadSoundEffect("victory", "/resources/sounds/victory.wav");
        loadSoundEffect("defeat", "/resources/sounds/defeat.wav");
    }

    private void loadSoundEffect(String name, String path) {
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                soundEffects.put(name, clip);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading sound effect: " + name);
            e.printStackTrace();
        }
    }

    /*
     * Plays background music from the specified resource path.
     * If music is already playing, it stops it before starting the new one.
     */

    public void playBackgroundMusic(String path) {
        if (isMuted) return;
        
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                if (backgroundMusic != null) {
                    backgroundMusic.stop();
                    backgroundMusic.close();
                }
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioIn);
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                
                // Set volume
                FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing background music");
            e.printStackTrace();
        }
    }

    /*
     * Stops the background music if it is currently playing.
     */

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.setFramePosition(0);
            pausePosition = 0;
            isPaused = false;
        }
    }

    public void pauseBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            pausePosition = backgroundMusic.getMicrosecondPosition();
            backgroundMusic.stop();
            isPaused = true;
        }
    }

    public void resumeBackgroundMusic() {
        if (backgroundMusic != null && isPaused) {
            backgroundMusic.setMicrosecondPosition(pausePosition);
            backgroundMusic.start();
            isPaused = false;
        }
    }

    /*
     * Plays a sound effect from the specified resource path.
     * If the sound effect is already loaded, it reuses the existing clip.
     */

    public void playSoundEffect(String name) {
        if (isMuted) return;
        
        Clip clip = soundEffects.get(name);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
        if (backgroundMusic != null) {
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }

    public void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            if (backgroundMusic != null) {
                backgroundMusic.stop();
            }
        } else {
            if (backgroundMusic != null) {
                backgroundMusic.start();
            }
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