package presentation.utils;

import javax.sound.sampled.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import presentation.utils.UIConstants;
/*
 * SoundManager.java
 * This class manages background music and sound effects for the game.
 * It allows playing, stopping, and looping sounds.
 */

public class SoundManager {
    private Clip backgroundMusic;
    private final Map<String, Clip> soundEffects;
    private boolean isMuted = false;
    private float musicVolume = 0.4f;  // Volume for background music
    private float effectsVolume = 0.5f;  // Volume for sound effects
    private long pausePosition = 0;
    private boolean isPaused = false;

    /*
     * Constructor initializes the sound effects map.
     */

    public SoundManager() {
        soundEffects = new HashMap<>();
        loadSoundEffects();
    }

    /*
     * Loads predefined sound effects into the soundEffects map.
     * Each sound effect is loaded from a specific resource path.
     */

    private void loadSoundEffects() {
        //loadSoundEffect("scape", UIConstants.SCAPE_SOUND_PATH);
        //loadSoundEffect("save", UIConstants.SAVE_SOUND_PATH);
        //loadSoundEffect("victory", UIConstants.VICTORY_SOUND_PATH);
        //loadSoundEffect("item", UIConstants.ITEM_SOUND_PATH);
    }

    /*
     * Loads a sound effect from the specified resource path.
     * The sound effect is stored in the soundEffects map with the given name.
     */

    private void loadSoundEffect(String name, String path) {
        try {
           InputStream audioSrc = getClass().getResourceAsStream(path);
        if (audioSrc != null) {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(audioSrc));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(effectsVolume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
            
            soundEffects.put(name, clip);
        } else {
            System.err.println("Could not find sound effect: " + path);
            // Imprime la ruta absoluta para debug
            System.err.println("Full attempted path: " + new File("").getAbsolutePath() + "/" + path);
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
                
                // Set volume for background music
                FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(musicVolume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
                
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundMusic.start();
            } else {
                System.err.println("Could not find background music: " + path);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing background music: " + path);
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
        
        // Stop any currently playing sound effects
        for (Clip clip : soundEffects.values()) {
            if (clip.isRunning()) {
                clip.stop();
                clip.setFramePosition(0);
            }
        }
        
        // Stop background music if playing
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.setFramePosition(0);
        }
        
        Clip clip = soundEffects.get(name);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        } else {
            System.err.println("Sound effect not found: " + name);
        }
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        if (backgroundMusic != null) {
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(musicVolume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }

    public void setEffectsVolume(float volume) {
        this.effectsVolume = Math.max(0.0f, Math.min(1.0f, volume));
        
        for (Clip clip : soundEffects.values()) {
            if (clip != null) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(effectsVolume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            }
        }
    }




    // TODO
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