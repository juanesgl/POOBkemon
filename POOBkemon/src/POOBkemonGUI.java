package presentation.main;

import presentation.screens.*;
import presentation.utils.SoundManager;
import presentation.controllers.GameController;
import presentation.utils.UIConstants;
import domain.game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class POOBkemonGUI extends JFrame {
    private CoverScreen coverScreen;
    private GameSetupScreen setupScreen;
    private GameScreen gameScreen;
    private SoundManager soundManager;
    private GameController gameController;

    public POOBkemonGUI() {
        setTitle("POOBkemon");
        setSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Initialize components
        soundManager = new SoundManager();
        gameController = new GameController(this);

        // Initialize screens
        coverScreen = new CoverScreen(gameController);
        setupScreen = new GameSetupScreen(gameController);
        gameScreen = new GameScreen(gameController);

        // Show initial screen
        showCoverScreen();

        // Setup window close event
        setupWindowCloseEvent();
    }

    public void showCoverScreen() {
        getContentPane().removeAll();
        getContentPane().add(coverScreen);
        soundManager.playBackgroundMusic(UIConstants.BACKGROUND_MUSIC_PATH);
        revalidate();
        repaint();
    }

    public void showSetupScreen() {
        getContentPane().removeAll();
        getContentPane().add(setupScreen);
        revalidate();
        repaint();
    }

    public void showGameScreen(Game game) {
        getContentPane().removeAll();
        gameScreen.setGame(game);
        getContentPane().add(gameScreen);
        revalidate();
        repaint();
    }

    private void setupWindowCloseEvent() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }

    public void exit() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Sure you want to get out?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            soundManager.stopAllSounds();
            dispose();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            POOBkemonGUI game = new POOBkemonGUI();
            game.setVisible(true);
        });
    }

}