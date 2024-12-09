package environment;

import org.example.Gamepanel;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Lighting {
    Gamepanel gp;
    BufferedImage darknessFilter;

    enum DayStates {
        day,
        dusk, // sun going down
        night,
        dawn // sun going up
    }

    DayStates dayState = DayStates.day;

    int dayCounter;
    float filterAlpha = 0f;
    final int day = 0;

    public Lighting(Gamepanel gp) {
        this.gp = gp;
        setLightSource();
    }

    public void draw(Graphics2D g2) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
        g2.drawImage(darknessFilter, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void setLightSource() {
        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

        if(gp.player.currentRing == null) {
            g2.setColor(Color.black);
        } else if (gp.player.currentRing.lightRadius > 0) {
            int centerX = gp.player.screenX + (gp.tilesize) / 2;
            int centerY = gp.player.screenY + (gp.tilesize) / 2;
            double x = centerX - (gp.player.currentRing.lightRadius / 2);
            double y = centerY - (gp.player.currentRing.lightRadius / 2);

            // gradiant effect
            Color color[] = new Color[5];
            float fraction[] = new float[5];

            color[0] = new Color(0, 0, 0.1f, 0f);
            color[1] = new Color(0, 0, 0.1f, 0.25f);
            color[2] = new Color(0, 0, 0.1f, 0.5f);
            color[3] = new Color(0, 0, 0.1f, 0.75f);
            color[4] = new Color(0, 0, 0.1f, 1f);

            fraction[0] = 0f; // center of circle
            fraction[1] = 0.25f;
            fraction[2] = 0.5f;
            fraction[3] = 0.75f;
            fraction[4] = 1f; // edge of circle

            RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, (gp.player.currentRing.lightRadius/2), fraction, color);
            g2.setPaint(gPaint);
        }


        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.dispose();
    }

    public void update() {
        if(gp.player.lightUpdated) {
            setLightSource();
            gp.player.lightUpdated = false;
        }

        updateDayTime(10_800);

    }

    public void updateDayTime(int dayLength) {
        if(dayState == DayStates.day) {
            dayCounter++;
            if(dayCounter > dayLength) {
                dayState = DayStates.dusk;
                dayCounter = 0;
            }
        } else if (dayState == DayStates.dusk) {
            filterAlpha += 0.001f;
            if(filterAlpha > 1f) {
                filterAlpha = 1f;
                dayState = DayStates.night;
            }
        } else if (dayState == DayStates.night) {
            dayCounter++;
            if(dayCounter > dayLength) {
                dayState = DayStates.dawn;
                dayCounter = 0;
            }
        } else if(dayState == DayStates.dawn) {
            filterAlpha -= 0.001f;
            if(filterAlpha < 0f) {
                filterAlpha = 0f;
                dayState = DayStates.day;
            }
        }
    }

    public void resetDay() {
        dayState = DayStates.day;
        filterAlpha = 0f;
        dayCounter = 0;
    }
}

