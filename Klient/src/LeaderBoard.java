package Klient.src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * Klasa odpowiedzialna za wyświetlanie tablicy wyników po wybraniu przycisku
 * "Leaderboard" z menu głównego. Jeżeli klient jest połączony z serwerem
 * wyświetla listę najlepszych wyników z serwera, jeżeli natomiast nie to
 * lokalną.
 */

public class LeaderBoard extends JPanel implements ActionListener {

    JButton back;
    MainFrame window;

    LeaderBoard(MainFrame mainWindow) {
        window = mainWindow;
        mainWindow.getContentPane().removeAll();
        mainWindow.getContentPane().repaint();
        ArrayList<Player> players = new ArrayList<Player>();
        try {

            if (!window.application.online) {

                BufferedReader reader = new BufferedReader(new FileReader("./Klient/resources/ranking.txt"));
                String line = reader.readLine();

                while (line != null) {

                    String[] dataHolder = line.split(",");

                    Player temp = new Player(dataHolder[0], Integer.parseInt(dataHolder[1]));

                    players.add(temp);

                    line = reader.readLine();

                }

                reader.close();
            } else {

                OutputStream outputStream = window.application.socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream, true);
                printWriter.println("GET_SCORES,all");
                printWriter.flush();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(window.application.socket.getInputStream()));
                String fromServer = in.readLine();

                String[] playersData = fromServer.split(",");

                for (int i = 0; i < playersData.length; i += 2) {

                    Player temp = new Player(playersData[i], Integer.parseInt(playersData[i + 1]));
                    players.add(temp);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setBackground(Color.white);
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(mainWindow.getWidth(), mainWindow.getHeight()));
        mainWindow.add(this);
        JLabel title = new JLabel("Lunar Lander");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.TOP);
        title.setFont(new Font("Showcard gothic", Font.PLAIN, 60));
        title.setForeground(new Color(0xb83dba));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(title, gbc);
        for (int i = 0; i < players.size(); i++) {

            JTextArea temp = new JTextArea((i + 1) + ".  " + players.get(i).nickname + "\t" + players.get(i).score);
            temp.setBackground(null);
            temp.setFocusable(false);
            temp.setFont(new Font("Showcard gothic", Font.PLAIN, 35));
            temp.setForeground(Color.black);

            this.add(temp, gbc);
            if (i == 7)
                break;
        }

        back = new JButton();
        back.setSize(300, 70);
        back.addActionListener(this);
        back.setText("Back");
        back.setFocusable(false);
        back.setFont(new Font("Showcard gothic", Font.PLAIN, 50));
        back.setForeground(new Color(0x00a8f3));
        back.setBackground(Color.WHITE);
        this.add(back, gbc);
        mainWindow.getContentPane().repaint();
        mainWindow.revalidate();
    }

    /**
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            window.getContentPane().removeAll();
            window.displayMenu();
        }

    }
}
