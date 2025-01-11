package object;

import entity.Projectile;
import entity.Entity;
import org.example.Gamepanel;

import java.awt.*;
import java.io.Serializable;

public class OBJ_Fireball extends Projectile {

    Gamepanel gp;
    public static final String objIDName = "Fireball";

    public OBJ_Fireball(Gamepanel gp) {
        super(gp);
        this.gp = gp;

        name = objIDName;
        speed = 10;
        maxHealth = 80;
        health = maxHealth;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();
        knockBackPower = 2;
        useSound = 1;
    }


    public void getImage() {
        up1 = setup("/objects/fireball_up1", gp.tilesize, gp.tilesize);
        up2 = setup("/objects/fireball_up2", gp.tilesize, gp.tilesize);
        down1 = setup("/objects/fireball_down_1", gp.tilesize, gp.tilesize);
        down2 = setup("/objects/fireball_down2", gp.tilesize, gp.tilesize);
        left1 = setup("/objects/fireball1", gp.tilesize, gp.tilesize);
        left2 = setup("/objects/fireball2", gp.tilesize, gp.tilesize);
        right1 = setup("/objects/fireball_right1", gp.tilesize, gp.tilesize);
        right2 = setup("/objects/fireball_right2", gp.tilesize, gp.tilesize);
    }

    public boolean hasResource(Entity user) {
        if (user.mana >= useCost) {
            return true;
        } else {
            gp.ui.addMessage("Insufficient mana");
            return false;
        }
    }

    public void completeResourceTransaction(Entity user) {
        user.mana -= useCost;
    }


    public Color getParticleColor() {
        return new Color(255, 47, 0);
    }
    public int getParticleSize() {
        return 10;
    }

    public int getParticleSpeed() {
        return 1;
    }

    public int getParticleLifetime() {
        return 20;
    }
}
