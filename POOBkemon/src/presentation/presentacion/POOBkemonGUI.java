package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.*;

public class POOBkemonGUI extends JFrame {

    private JLabel labelCover;
    private JButton startButton;
    private ImageIcon startIconNormal;
    private ImageIcon startIconHover;
    private ImageIcon startIconShiny;
    private Clip backgroundMusic;
    private Timer startButtonTimer;
    private boolean buttonState = false;

    private POOBkemonGUI(){
        setTitle("POOBkemon");
        prepareElements();
        prepareActions();
        prepareAnimations();
    }

    private void prepareElements(){
        setSize(new Dimension(1024, 720));
        setLocationRelativeTo(null);
        setLayout(null);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1024, 720);

        ImageIcon cover = new ImageIcon("images/Cover/POOBkemonCover.png");
        labelCover = new JLabel(cover);
        labelCover.setBounds(0, 0, 1024, 720);
        layeredPane.add(labelCover, Integer.valueOf(0));

        startIconNormal = new ImageIcon("images/Cover/Startbutton.png");


        startButton = new JButton(startIconNormal);
        startButton.setBounds(423, 550, 179, 71);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(false);
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        layeredPane.add(startButton, Integer.valueOf(1));

        add(layeredPane);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopMusic(); // Detenemos la música al presionar Start
                showUnderConstructionScreen(); // Mostramos pantalla de "En construcción"
            }
        });

        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setIcon(startIconHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setIcon(startIconNormal);
            }
        });

        prepareMusic(); // Iniciamos la música de fondo
    }

    private void prepareActions(){
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }

    private void prepareAnimations(){
        startButtonTimer = new Timer(700, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!startButton.getModel().isRollover()) {
                    if (buttonState) {
                        startButton.setIcon(startIconNormal);
                    } else {
                        startButton.setIcon(startIconShiny);
                    }
                    buttonState = !buttonState;
                }
            }
        });
        startButtonTimer.start();
    }

    private void exit(){
        int confirm = JOptionPane.showConfirmDialog(this, "Sure you want to get out?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION){
            dispose();
            System.exit(0);
        }
    }

    private void showUnderConstructionScreen() {
        JOptionPane.showMessageDialog(this, "¡En construcción!", "POOBkemon", JOptionPane.INFORMATION_MESSAGE);
    }

    private void stopMusic(){
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop(); // Detener la música
        }
    }

    private void prepareMusic(){
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("sounds-music/music-cover/Pokemon-Emerald-Opening.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Repite la música en bucle
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        POOBkemonGUI game = new POOBkemonGUI();
        game.setVisible(true);
    }
}
