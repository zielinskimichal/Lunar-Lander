package Klient.src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Klaa odpowiedzialna za odczyt danych konfiguracyjnych z pliku tekstowego oraz
 * serwera.
 */

public class Parser {

    private String fileName_;
    private App application;

    Parser(String fileName, App mainApp) {
        fileName_ = fileName;
        application = mainApp;
    }

    /**
     * Metoda tworząca i zwracająca listę dostępnych poziomów. W przypadku wykrycia
     * połączenia z serwerem pobiera listę z serwera, w przeciwnym razie korzysta z
     * lokalnego pliku konfiguracyjnego.
     * 
     * @return lista poziomów
     */
    public ArrayList<LevelConfig> getConfigData() {
        ArrayList<LevelConfig> levels = new ArrayList<LevelConfig>();
        try {
            String[] levelsData;
            if (application.online) {
                OutputStream outputStream = application.socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream, true);
                printWriter.println("GET_CONFIG,allLevels");
                printWriter.flush();

                BufferedReader in = new BufferedReader(new InputStreamReader(application.socket.getInputStream()));
                String fromServer = in.readLine();

                levelsData = fromServer.split(";");

            } else {
                BufferedReader reader = new BufferedReader(new FileReader(fileName_));
                String line = reader.readLine();
                String holder = "";
                while (line != null) {
                    holder = holder + line + ";";
                    line = reader.readLine();

                }
                levelsData = holder.split(";");
                reader.close();
            }
            for (int i = 0; i < levelsData.length; i++) {
                String line = levelsData[i];
                String[] dataHolder = line.split(",");

                int[] xHold, yHold;
                xHold = new int[9];
                yHold = new int[9];
                for (int j = 4; j < 9; j++) {
                    xHold[j - 2] = (Integer.parseInt(dataHolder[j]));
                }
                for (int j = 9; j < dataHolder.length; j++) {
                    yHold[j - 8] = (Integer.parseInt(dataHolder[j]));
                }
                LevelConfig tempLevel = new LevelConfig(Integer.parseInt(dataHolder[0]),
                        Integer.parseInt(dataHolder[1]), Integer.parseInt(dataHolder[2]),
                        Integer.parseInt(dataHolder[3]), xHold, yHold);

                levels.add(tempLevel);

            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return levels;
    }
}