package Klient.src;

/**
 * Klasa, która jest odpowiedzialna za przechowywanie danych odnośnie poziomów
 * gry, takich jak koordynaty lądowiska, rakiety i ukształtowanie terenu.
 */
public class LevelConfig {
    int xPosOfLanding_;
    int yPosOfLanding_;
    int xPosOfRocket_;
    int yPosOfRocket_;
    int[] yMountains, xMountains;

    LevelConfig(int xPosOfLanding, int yPosOfLanding, int xPosOfRocket, int yPosOfRocket, int[] xPointsMountains,
            int[] yPointsMountains) {
        xMountains = xPointsMountains;
        yMountains = yPointsMountains;
        xPosOfLanding_ = xPosOfLanding;
        yPosOfLanding_ = yPosOfLanding;
        xPosOfRocket_ = xPosOfRocket;
        yPosOfRocket_ = yPosOfRocket;

    }

}
