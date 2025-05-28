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
    private float volume = 0.3f;  // Reduced from 0.7f to 0.3f (30% volume)
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
        // Solo cargamos los efectos de sonido que realmente necesitamos
        loadSoundEffect("attack", "/resources/sounds/attack.wav");
        loadSoundEffect("switch", "/resources/sounds/switch.wav");
        loadSoundEffect("scape", "/sounds-music/music-cover/scape.wav");
        loadSoundEffect("save", "/sounds-music/music-cover/saveGame.wav");
        loadSoundEffect("victory", "/sounds-music/music-cover/victorySound.wav");
    }

    private void loadSoundEffect(String name, String path) {
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                
                // Set volume for sound effects
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
                
                soundEffects.put(name, clip);
            } else {
                System.err.println("Could not find sound effect: " + path);
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
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
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
        
        Clip clip = soundEffects.get(name);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        } else {
            System.err.println("Sound effect not found: " + name);
        }
    }

    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
        
        // Update background music volume
        if (backgroundMusic != null) {
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
        
        // Update all sound effects volume
        for (Clip clip : soundEffects.values()) {
            if (clip != null) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            }
        }
    }

    public float getVolume() {
        return volume;
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