package Klient.src;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.util.TimerTask;
import java.awt.image.BufferStrategy;
import java.awt.Component;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Klasa odpowiedzialna za wyświetlanie jednego poziomu rozgrywki oraz
 * aktualizację klatek ruchu rakiety.
 */
public class Rocket implements Runnable {
    public boolean goRight = false, goLeft = false, goUp = false, pause = false, lost = false, won = false;
    public int x, y;
    float vy = 1, vx = 0, ax = 0.045f, ay = 0.045f;
    public String image = "./Klient/resources/rocket.png";
    GameFrame gameWindow;
    LandingPlace currentLanding;
    Polygon mountains;
    int[] xPoints = new int[9], yPoints = new int[9];
    double fuel = 100;

    /**
     * @param temp         referencja do komponentu na którym wyświetlana jest
     *                     rozgrywka
     * @param currentLevel obecny poziom
     */
    public Rocket(GameFrame temp, LevelConfig currentLevel) {
        this.x = Math.round(currentLevel.xPosOfRocket_ * temp.mainWindow.getWidth() / 20);
        this.y = Math.round(currentLevel.yPosOfRocket_ * temp.mainWindow.getHeight() / 20);
        this.gameWindow = temp;
        currentLanding = new LandingPlace(Math.round(currentLevel.xPosOfLanding_ * temp.mainWindow.getWidth() / 20),
                Math.round(currentLevel.yPosOfLanding_ * temp.mainWindow.getHeight() / 20));

        for (int i = 0; i < currentLevel.xMountains.length; i++) {
            this.xPoints[i] = currentLevel.xMountains[i];
            this.yPoints[i] = currentLevel.yMountains[i];

        }

        xPoints[0] = 0;
        xPoints[1] = 0;
        xPoints[7] = gameWindow.mainWindow.getWidth();
        xPoints[8] = gameWindow.mainWindow.getWidth();
        for (int i = 2; i < 7; i++) {

            xPoints[i] = Math.round(xPoints[i] * gameWindow.mainWindow.getWidth() / 20);
        }

        yPoints[0] = gameWindow.mainWindow.getHeight();

        yPoints[8] = gameWindow.mainWindow.getHeight();
        for (int i = 1; i < 8; i++) {
            yPoints[i] = Math.round(yPoints[i] * gameWindow.mainWindow.getHeight() / 20);
        }

        mountains = new Polygon(xPoints, yPoints, xPoints.length);
    }

    /**
     * Metoda rysująca aktualną klatkę
     * 
     */

    void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        Image temp = new ImageIcon("./Klient/resources/rocket.png").getImage();
        g2D.setColor(Color.GREEN);

        g2D.setColor(Color.lightGray);

        g2D.fill(mountains);

        Rectangle2D playerHitbox = new Rectangle2D.Float(this.x, this.y, 33, 33);
        g2D.drawImage(temp, this.x, this.y, 40, 40, null);
        Rectangle2D endPoint = new Rectangle2D.Float(currentLanding.xPosition, currentLanding.yPosition, 100, 20);
        g2D.setColor(Color.red);
        g2D.fill(endPoint);
        if (playerHitbox.intersects(endPoint)) {

            if (fuel > 0)
                gameWindow.score += Math.round(fuel * 10);
            pause = true;
            won = true;

        }
        if (mountains.intersects(playerHitbox)) {

            lost = true;
            pause = true;

        }
        if (this.x < -7 || this.x > gameWindow.getWidth() - 33 || this.y < -7) {
            lost = true;
            pause = true;
        }
    }

    /**
     * Metoda odpowiedzialna za ruch rakiety.
     */

    public void run() {

        while (!pause) {
            System.out.print("");
            if (!gameWindow.paused) {

            if (goUp && fuel > 0) {
                this.vy -= ay;
                if (ay > 0) {
                    ay -= 0.0001;
                }
            } else {
                ay = 0.045f;
                this.vy += ay;
            }
            if (goRight && fuel > 0) {
                this.vx += ax;
                if (ax > 0) {
                    ax -= 0.0001;
                }
            }
            if (goLeft && fuel > 0) {
                this.vx -= ax;
                if (ax > 0) {
                    ax -= 0.0001;
                }
            }
            if ((!goLeft && !goRight) || fuel < 0) {
                ax = 0.045f;
                if (vx < 0) {
                    vx += ax;
                }
                if (vx > 0) {
                    vx -= ax;
                }
            }
            if (goLeft || goRight || goUp) {
                fuel -= 0.2;
            }
            if (fuel > 0) {
                gameWindow.fuelDisplay.setText("Fuel: " + Math.round(fuel) + "%");
            } else {
                gameWindow.fuelDisplay.setText("Fuel: 0%");
            }
            this.x += Math.round(this.vx);
            this.y += Math.round(this.vy);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    }

}
