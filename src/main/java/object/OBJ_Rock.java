package object;

import entity.Projectile;
import org.example.Gamepanel;

import java.awt.*;

public class OBJ_Rock extends Projectile {

    Gamepanel gp;

    public static final String objIDName = "Rock";

    public OBJ_Rock(Gamepanel gp) {
        super(gp);
        this.gp = gp;

        name = objIDName;
        speed = 6;
        maxHealth = 80;
        health = maxHealth;
        attack = 1;
        useCost = 1;
        alive = false;
        getImage();
    }



    public void getImage() {
        up1 = setup("/objects/rock", gp.tilesize, gp.tilesize);
        up2 = setup("/objects/rock", gp.tilesize, gp.tilesize);
        down1 = setup("/objects/rock", gp.tilesize, gp.tilesize);
        down2 = setup("/objects/rock", gp.tilesize, gp.tilesize);
        left1 = setup("/objects/rock", gp.tilesize, gp.tilesize);
        left2 = setup("/objects/rock", gp.tilesize, gp.tilesize);
        right1 = setup("/objects/rock", gp.tilesize, gp.tilesize);
        right2 = setup("/objects/rock", gp.tilesize, gp.tilesize);
    }

    public Color getParticleColor() {
        return new Color(78, 32, 21);
    }
    public int getParticleSize() {
        return 6;
    }

    public int getParticleSpeed() {
        return 1;
    }

    public int getParticleLifetime() {
        return 20;
    }
}
