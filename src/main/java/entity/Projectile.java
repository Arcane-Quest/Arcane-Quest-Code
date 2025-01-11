package entity;

import org.example.Gamepanel;

import java.awt.*;

public class Projectile extends Entity{
    Entity user;

    public Projectile(Gamepanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.health = this.maxHealth;
    }

    public void update() {
        if (user == gp.player) {
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if(monsterIndex != 999) {
                gp.player.damageMonster(monsterIndex, attack, knockBackPower, this);
                generateParticle(user.projectile, gp.monster[gp.currentMap][monsterIndex]);
                alive = false;
            }
        } else if(user != gp.player){
            boolean contactingPlayer = gp.cChecker.checkPlayer(this);
            if(!gp.player.invincible && contactingPlayer) {
                damagePlayer(attack, knockBackPower);
                generateParticle(user.projectile, gp.player);
                alive = false;
            }
        }

        switch(direction) {

            case "up": worldY -= speed;    break;
            case "down": worldY += speed;  break;
            case "left": worldX -= speed;  break;
            case "right": worldX += speed; break;
        }

        health--; // entity dies after certain amount of time
        if(health <= 0) {
            alive = false;
        }

        spriteCounter++;
        if(spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if(spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }


    }
    public boolean hasResource(Entity user) {
        return false; // this function is only to be overridden
    }

    public void completeResourceTransaction(Entity user) {}

}
