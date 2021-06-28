package Klient.src;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.util.TimerTask;

/**
 * Klasa dziedziczy po klasie JPanel, jest komponenentem na którym wyświetlane
 * są wszystkie elementy gry.
 */

public class GameFrame extends JPanel implements ActionListener, KeyListener {
    GameFrame thisRef;
    MainFrame mainWindow;
    Rocket player;
    Timer timer;
    Image rocket;
    Thread mainThread;
    int currentLevel = 0;
    JPanel dashboard;
    int lives = 3;
    JTextArea livesDisplay, levelDisplay, fuelDisplay, scoreDisplay;
    int score = 0;
    boolean paused = false;

    /**
     * Konstruktor wyświetlający panel z infomacjami dla gracza.
     */
    public GameFrame(MainFrame app) {
        this.thisRef = this;
        this.mainWindow = app;
        timer = new Timer(5, this);
        dashboard = new JPanel();
        dashboard.setSize(new Dimension(200, 150));
        dashboard.setBounds(app.getWidth() - 200, 0, 200, 150);
        dashboard.setBackground(new Color(0, 0, 0, 0));
        dashboard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(dashboard);
        this.setVisible(true);
        this.setLayout(null);
        livesDisplay = new JTextArea("Lives: 3");
        livesDisplay.setBackground(null);
        livesDisplay.setFocusable(false);
        livesDisplay.setFont(new Font("Showcard gothic", Font.PLAIN, 23));
        livesDisplay.setForeground(new Color(0x00a8f3));

        levelDisplay = new JTextArea("Level: 1");
        levelDisplay.setBackground(null);
        levelDisplay.setFocusable(false);
        levelDisplay.setFont(new Font("Showcard gothic", Font.PLAIN, 23));
        levelDisplay.setForeground(new Color(0x00a8f3));

        fuelDisplay = new JTextArea("Fuel: 100%");
        fuelDisplay.setBackground(null);
        fuelDisplay.setFocusable(false);
        fuelDisplay.setFont(new Font("Showcard gothic", Font.PLAIN, 23));
        fuelDisplay.setForeground(new Color(0x00a8f3));

        scoreDisplay = new JTextArea("Score: 0");
        scoreDisplay.setBackground(null);
        scoreDisplay.setFocusable(false);
        scoreDisplay.setFont(new Font("Showcard gothic", Font.PLAIN, 23));
        scoreDisplay.setForeground(new Color(0x00a8f3));

        dashboard.add(levelDisplay, gbc);
        dashboard.add(livesDisplay, gbc);
        dashboard.add(fuelDisplay, gbc);
        dashboard.add(scoreDisplay, gbc);
        this.repaint();
        timer.start();
        try {
            this.startGame(app, currentLevel);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        app.repaint();
        app.revalidate();
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {

                dashboard.setBounds(app.getWidth() - 200, 0, 200, 150);

            }

        });
    }

    /**
     * Metoda "pilnująca" liczby żyć gracza oraz zapisująca wynik w przypadku
     * porażki (lokalnie oraz na serwerze - jeśli podłączony)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.repaint();
        if (player.lost) {

            try {
                this.lives--;
                this.livesDisplay.setText("Lives: " + lives);
                if (this.lives == 0) {

                    timer.stop();
                    if (score > 0 && !mainWindow.nickname.equals("enter your name...")
                            && !mainWindow.nickname.equals(""))
                        mainWindow.saveRank(score);
                    mainWindow.remove(this);
                    mainWindow.repaint();
                    if (mainWindow.application.online && score > 0
                            && !mainWindow.nickname.equals("enter your name...")) {
                        OutputStream outputStream = mainWindow.application.socket.getOutputStream();
                        PrintWriter printWriter = new PrintWriter(outputStream, true);
                        printWriter.println("SAVE_SCORE" + "," + mainWindow.nickname + "," + score);
                        printWriter.flush();
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(mainWindow.application.socket.getInputStream()));
                        in.readLine();
                    }

                } else
                    this.startGame(mainWindow, currentLevel);
            } catch (IOException ex) {

            }
        }
        if (player.won) {
            try {
                if (this.currentLevel == 8) {

                    timer.stop();
                    if (score > 0 && !mainWindow.nickname.equals("enter your name...")
                            && !mainWindow.nickname.equals(""))
                        mainWindow.saveRank(score);
                    mainWindow.remove(this);
                    mainWindow.repaint();

                } else {
                    currentLevel++;
                    scoreDisplay.setText("Score: " + score);

                    levelDisplay.setText("Level: " + (currentLevel + 1));
                    this.startGame(mainWindow, currentLevel);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * @param g
     */
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        this.player.paint(g);
        this.repaint();
        this.requestFocus();

    }

    /**
     * Metoda rozpoczynająca rozgrywkę, tworzy pojedynczą planszę.
     * 
     * @param app         Referencja do głównego okna programu.
     * @param levelNumber Numer aktualnego poziomu.
     * @throws IOException
     */

    void startGame(MainFrame app, int levelNumber) throws IOException {

        app.setLayout(new BorderLayout());

        this.player = new Rocket(this, app.levelsList.get(levelNumber));

        rocket = new ImageIcon("./Klient/resources/rocket.png").getImage();
        this.setPreferredSize(new Dimension(app.getWidth(), app.getHeight()));
        this.setBackground(Color.black);
        app.add(this);

        this.setVisible(true);
        this.setFocusable(true);

        this.requestFocus();

        this.addKeyListener(this);

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                thisRef.setPreferredSize(new Dimension(app.getWidth(), app.getHeight()));
                app.repaint();
                app.revalidate();
            }
        });
        mainThread = new Thread(player);

        mainThread.start();

    }

    /**
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'a':
                player.goLeft = true;

                break;
            case 'w':
                player.goUp = true;
                break;
            case 'd':

                player.goRight = true;
                break;

        }

        try {

            if (e.getKeyCode() == 32) {
                paused = !paused;

            }
        } catch (Exception g) {
            g.printStackTrace();
        }

    }

    /**
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'a':
                player.goLeft = false;

                break;
            case 'w':
                player.goUp = false;
                break;
            case 'd':
                player.goRight = false;
                break;

        }
    }

}
