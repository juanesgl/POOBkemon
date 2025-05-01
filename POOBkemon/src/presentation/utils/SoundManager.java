package presentation.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class SoundManager {
    private Clip backgroundMusic;
    private Map<String, Clip> soundEffects;

    public SoundManager() {
        soundEffects = new HashMap<>();
    }

    public void playBackgroundMusic(String filePath) {
        stopBackgroundMusic();

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }

    public void playSoundEffect(String name, String filePath) {
        try {
            if (!soundEffects.containsKey(name)) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
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