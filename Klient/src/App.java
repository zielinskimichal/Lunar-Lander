package Klient.src;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Klasa App służy do pobrania i przechowywania danych konfiguracyjnych.
 *
 */

public class App {

    public ArrayList<LevelConfig> levelsList_ = new ArrayList<LevelConfig>();
    Socket socket;
    Boolean online = false;

    public void run() {
        Parser initiator = new Parser("./Klient/resources/config.txt", this);
        levelsList_ = initiator.getConfigData();
        MainFrame frame = new MainFrame(this);
        frame.launchFrame();
    }

}
