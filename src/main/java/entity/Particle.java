package entity;

import org.example.Gamepanel;

import java.awt.*;

public class Particle extends Entity {
    Entity generator;
    Color color;
    int size;
    int xd;
    int yd;

    public Particle(Gamepanel gp, Entity generator, Color color, int size, int speed, int maxHealth, int xd, int yd) {
        super(gp);

        this.generator = generator;
        this.color = color;
        this.size = size;
        this.xd = xd; // direction for x -1 = left
        this.yd = yd; // direction for y -1 = top
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.health = maxHealth;

        int offset = (gp.tilesize/2) - (size/2);

        worldX = generator.worldX + offset;
        worldY = generator.worldY + offset;

    }

    public void update() {
        health--;

        if(health < maxHealth/3) {
            yd++; // very simple gravity
        }

        worldX += xd*speed;
        worldY += yd*speed;

        if(health == 0) {
            alive = false;
        }
    }

    public void draw(Graphics2D g2) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.setColor(color);
        g2.fillRect(screenX, screenY, size, size);
    }
}
