package Klient.src;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;
import java.util.Timer;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

/**
 * Klasa wyświetlająca głowne okno aplikacji.
 */

public class MainFrame extends JFrame implements ActionListener {
    public Timer timer;
    JButton start;
    JButton leaderboard;
    JTextField textField;
    public ArrayList<LevelConfig> levelsList;
    App application;
    String nickname;
    JButton connectButton;

    /**
     * Konstruktor wypisujący referencję na obiekt aplikacji oraz liste poziomów
     * 
     * @param mainApp obiekt aplikacji głównej
     */

    MainFrame(App mainApp) {
        application = mainApp;

        levelsList = mainApp.levelsList_;

        this.displayMenu();
        // menu label

    }

    /**
     * Metoda odpowiedzialna za wyświetlenie komponentów menu głównego.
     */
    public void displayMenu() {
        // this.removeAll();
        JLabel title = new JLabel("Lunar Lander");

        title.setFont(new Font("Showcard gothic", Font.PLAIN, 60));
        title.setForeground(new Color(0xb83dba));
        JPanel titlePanel = new JPanel();

        titlePanel.setBackground(Color.white);

        titlePanel.add(title);

        start = new JButton();
        start.setSize(300, 70);
        start.addActionListener(this);
        start.setText("Let's play!");
        start.setFocusable(false);
        start.setFont(new Font("Showcard gothic", Font.PLAIN, 50));
        start.setForeground(new Color(0x00a8f3));
        start.setHorizontalAlignment(SwingConstants.CENTER);
        start.setVerticalAlignment(SwingConstants.BOTTOM);
        start.setBackground(Color.WHITE);

        JPanel startPanel = new JPanel();

        startPanel.setBackground(Color.white);
        startPanel.add(start);

        leaderboard = new JButton();
        leaderboard.setSize(300, 70);
        leaderboard.addActionListener(this);
        leaderboard.setText("Leaderboard");
        leaderboard.setFocusable(false);
        leaderboard.setFont(new Font("Showcard gothic", Font.PLAIN, 50));
        leaderboard.setForeground(new Color(0x00a8f3));
        leaderboard.setHorizontalAlignment(SwingConstants.CENTER);
        leaderboard.setVerticalAlignment(SwingConstants.BOTTOM);
        leaderboard.setBackground(Color.WHITE);

        JPanel leaderPanel = new JPanel();

        leaderPanel.setBackground(Color.white);

        leaderPanel.add(leaderboard);

        textField = new JTextField();
        textField.setSize(300, 70);
        textField.setFont(new Font("Showcard gothic", Font.PLAIN, 50));
        textField.setForeground(new Color(0x00a8f3));
        textField.setBackground(Color.white);
        textField.setText("enter your name...");

        JPanel textfieldPanel = new JPanel();
        textfieldPanel.setBackground(Color.white);
        textfieldPanel.add(textField);
        connectButton = new JButton("Connect");
        connectButton.setSize(300, 70);
        connectButton.setFont(new Font("Showcard gothic", Font.PLAIN, 50));
        connectButton.setForeground(new Color(0x00a8f3));
        connectButton.setBackground(Color.white);
        connectButton.addActionListener(this);

        JPanel connectPanel = new JPanel();
        connectPanel.setBackground(Color.white);
        connectPanel.add(connectButton);

        double whitespace = (this.getHeight() - 400) / 10;
        titlePanel.setBounds(0, (int) Math.round(whitespace), this.getWidth(), 100);
        textfieldPanel.setBounds(0, 100 + 2 * (int) Math.round(whitespace), this.getWidth(), 100);
        startPanel.setBounds(0, 200 + 3 * (int) Math.round(whitespace), this.getWidth(), 100);
        leaderPanel.setBounds(0, 300 + 4 * (int) Math.round(whitespace), this.getWidth(), 100);
        connectPanel.setBounds(0, 400 + 5 * (int) Math.round(whitespace), this.getWidth(), 100);

        this.setTitle("LunarLander");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 800);
        this.getContentPane().setBackground(Color.white);
        this.setResizable(true);

        this.setLayout(null);

        this.add(titlePanel);
        this.add(textfieldPanel);
        this.add(startPanel);
        this.add(leaderPanel);
        this.add(connectPanel);
        this.requestFocus();
        this.getContentPane().repaint();
        this.revalidate();
        MainFrame temp = this;
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {

                double whitespace = (temp.getHeight() - 400) / 10;

                titlePanel.setBounds(0, (int) Math.round(whitespace), temp.getWidth(), 100);

                textfieldPanel.setBounds(0, 100 + 2 * (int) Math.round(whitespace), temp.getWidth(), 100);
                startPanel.setBounds(0, 200 + 3 * (int) Math.round(whitespace), temp.getWidth(), 100);
                leaderPanel.setBounds(0, 300 + 4 * (int) Math.round(whitespace), temp.getWidth(), 100);
                connectPanel.setBounds(0, 400 + 5 * (int) Math.round(whitespace), temp.getWidth(), 100);
                temp.revalidate();
                temp.repaint();

            }

        });
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals("enter your name...")) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText("enter your name...");
                }
            }
        });
        this.setVisible(true);
        this.revalidate();
        this.repaint();
        this.requestFocus();

    }

    /**
     * Metoda odpowiedzialna za obsługę przycisków
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {

            nickname = textField.getText();
            new GameFrame(this);
        }
        if (e.getSource() == leaderboard) {
            new LeaderBoard(this);
        }

        if (e.getSource() == connectButton) {

            try {
                if (!application.online) {
                    Properties p = new Properties();
                    InputStream is = new FileInputStream("./Klient/resources/port.properties");
                    p.load(is);
                    int port = Integer.parseInt(p.getProperty("port"));
                    application.socket = new Socket("localhost", port);
                    application.online = true;
                    connectButton.setText("Connected");
                    Parser initiator = new Parser("./Klient/resources/config.txt", application);
                    application.levelsList_ = initiator.getConfigData();
                    this.getContentPane().repaint();
                    this.revalidate();

                }

            } catch (Exception g) {
                application.online = false;
                System.out.println("Connection could not be opened..");
                System.out.println("error: " + g);
            }
        }
    }

    void launchFrame() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(1);
            }
        });
        EventQueue.invokeLater(() -> setVisible(true));
    }

    /**
     * Metoda dodająca wynik gracza do listy rankigowej
     * 
     * @param score wynik gracza po zakończeniu rozgrywki
     */
    public void saveRank(int score) {
        ArrayList<Player> players = new ArrayList<Player>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./Klient/resources/ranking.txt"));
            String line = reader.readLine();

            while (line != null) {

                String[] dataHolder = line.split(",");

                Player temp = new Player(dataHolder[0], Integer.parseInt(dataHolder[1]));

                players.add(temp);

                line = reader.readLine();

            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        players.add(new Player(this.nickname, score));
        Collections.sort(players);
        try {
            FileWriter rankWriter = new FileWriter("./Klient/resources/ranking.txt");
            for (int i = 0; i < players.size(); i++)
                rankWriter.write(players.get(i).nickname + "," + players.get(i).score + "\n");
            rankWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

/**
 * Klasa przechowująca wynik gracza
 */

class Player implements Comparable {
    String nickname;
    int score;

    Player(String name, int points) {
        nickname = name;
        score = points;
    }

    @Override
    public int compareTo(Object o) {
        Player p = (Player) o;
        return p.score - this.score;
    }
}