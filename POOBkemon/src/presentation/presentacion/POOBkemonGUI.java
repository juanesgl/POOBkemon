package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;

public class POOBkemonGUI extends JFrame {
    private boolean gameStarted = false;
    private JLabel labelCover, labelModalities, labelModes;
    private JButton startButton;
    private ImageIcon startIconNormal;
    private ImageIcon startIconHover;
    private ImageIcon startIconShiny;
    private Clip backgroundMusic;
    private Timer startButtonTimer;
    private boolean buttonState = false;
    private JComboBox<String> comboModalities, comboModes;

    public static final ArrayList<String> modalidades = new ArrayList<>();
    public static final ArrayList<String> modos = new ArrayList<>();

    static {
        modalidades.add("Jugador vs Jugador");
        modalidades.add("Jugador vs Maquina");
        modalidades.add("Maquina vs Maquina");
        modos.add("Normal");
        modos.add("Supervivencia");
    }

    private POOBkemonGUI() {
        setTitle("POOBkemon");
        prepareElements();
        prepareActions();
        prepareAnimations();
        setVisible(true); // Asegúrate de que la ventana sea visible desde el principio
    }

    // PREPARING
    private void prepareActions() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }

    private void prepareElements() {
        prepareMusic();
        setSize(new Dimension(1024, 720));
        setLocationRelativeTo(null);
        setLayout(null);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1024, 720);

        // Portada
        ImageIcon cover = new ImageIcon("resources/Cover/POOBkemonCover.png");
        labelCover = new JLabel(cover);
        labelCover.setBounds(0, 0, 1024, 720);
        layeredPane.add(labelCover, Integer.valueOf(0));

        // Botón de Inicio
        startIconNormal = new ImageIcon("resources/Cover/Startbutton.png");


        startButton = new JButton(startIconNormal);
        startButton.setBounds(423, 550, 179, 71);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(false);
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameStarted) {
                    initialize();
                } else {
                    stopMusic();
                    showUnderConstructionScreen();
                }
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
        layeredPane.add(startButton, Integer.valueOf(1));

        // Etiquetas y ComboBoxes (inicialmente ocultos)
        labelModalities = new JLabel("Modalidades");
        labelModalities.setBounds(256, 300, 190, 30);
        labelModalities.setVisible(false);
        labelModalities.setFont(loadCustomFont("editundo.ttf", 24f));
        labelModalities.setForeground(new Color(254, 254, 254));
        layeredPane.add(labelModalities, Integer.valueOf(2));

        labelModes = new JLabel("Modos");
        labelModes.setBounds(600, 300, 190, 30);
        labelModes.setVisible(false);
        labelModes.setFont(loadCustomFont("editundo.ttf", 24f));
        labelModes.setForeground(new Color(254, 254, 254));
        layeredPane.add(labelModes, Integer.valueOf(2));

        comboModalities = new JComboBox<>();
        comboModalities.setBounds(256, 350, 190, 30);
        comboModalities.setVisible(false);
        comboModalities.setFont(loadCustomFont("editundo.ttf", 18f));
        for (String modality : modalidades) {
            comboModalities.addItem(modality);
        }
        layeredPane.add(comboModalities, Integer.valueOf(2));

        comboModes = new JComboBox<>();
        comboModes.setBounds(600, 350, 190, 30);
        comboModes.setVisible(false);
        comboModes.setFont(loadCustomFont("editundo.ttf", 18f));
        for (String mode : modos) {
            comboModes.addItem(mode);
        }
        layeredPane.add(comboModes, Integer.valueOf(2));

        add(layeredPane); // ¡Asegúrate de agregar el layeredPane al JFrame!
    }

    private Font loadCustomFont(String fontFileName, float size) {
        String fontFilePath = "resources/fonts/" + fontFileName;
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontFilePath)).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            return customFont;
        } catch (IOException | FontFormatException e) {
            System.err.println("Error al cargar la fuente: " + fontFilePath);
            e.printStackTrace();
            return new Font("Georgia", Font.BOLD, (int) size); // Fuente de respaldo
        }
    }

    private void prepareAnimations() {
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

    private void prepareMusic() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("sounds-music/music-cover/Pokemon-Emerald-Opening.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Repite la música en bucle
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Methods
    private void exit() {
        int confirm = JOptionPane.showConfirmDialog(this, "Sure you want to get out?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    private void showUnderConstructionScreen() {
        JOptionPane.showMessageDialog(this, "¡En construcción!", "POOBkemon", JOptionPane.INFORMATION_MESSAGE);
    }

    private void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop(); // Detener la música
        }
    }

    public void initialize() {
        labelModalities.setVisible(true);
        labelModes.setVisible(true);
        comboModalities.setVisible(true);
        comboModes.setVisible(true);
        gameStarted = true;
    }

    public static void main(String[] args) {
        POOBkemonGUI game = new POOBkemonGUI();
    }
}