package monster;

import entity.Entity;
import object.*;
import org.example.Gamepanel;

import java.util.Random;

public class MON_Goose extends Entity{
    // ITS THE SAME AS A DUCK
    // peace was never an option

    Gamepanel gp;
    public static final String objIDName = "MON_Goose";

    public MON_Goose(Gamepanel gp) {
        super(gp);
        this.gp = gp; // THIS IS NEEDED, it crashes otherwise
        name = objIDName;
        type = Entity.EntityType.MONSTER;
        attack = 1;
        defense = 0;
        defaultSpeed = gp.player.speed;
        speed = defaultSpeed;
        knockBackPower = 2;
        getImage();
        setDialogue();
    }

    public void getImage() {
        //FIXME: gp is null, therefore we have to use the tileSize thing as ints

        up1 = setup("/monster/duck_up1", 64, 64);
        up2 = setup("/monster/duck_up2", 64, 64);
        left1 = setup("/monster/duck_left1", 64, 64);
        left2 = setup("/monster/duck_left2", 64, 64);
        right1 = setup("/monster/duck_right1", 64, 64);
        right2 = setup("/monster/duck_right2", 64, 64);
        down1 = setup("/monster/duck_down1", 64, 64);
        down2 = setup("/monster/duck_down2", 64, 64);
    }

    public void setAction() {
        int tileDistance = getTileDistance(gp.player);
        int goalCol = gp.player.worldX/gp.tilesize;
        int goalRow = gp.player.worldY/gp.tilesize;

        if(tileDistance > 7) {
            speed = defaultSpeed;
            if(!(goalCol > gp.maxWorldCol || goalRow > gp.maxWorldRow)) {
                searchPath(goalCol, goalRow, true);
                aggro = false;
            }
        } else if (!aggro && tileDistance < 8) {
            speed = defaultSpeed - 1;
            chooseRandomDirection();
            if(gp.player.attackingTargetIndex > -1) {
                aggro = true;
            }

        } else {
            speed = defaultSpeed;
            if(gp.player.attackingTargetIndex < gp.monster[1].length + 1 &&
                    gp.monster[gp.currentMap][gp.player.attackingTargetIndex] != null) {
                searchPath(
                        gp.monster[gp.currentMap][gp.player.attackingTargetIndex].worldX / gp.tilesize,
                        gp.monster[gp.currentMap][gp.player.attackingTargetIndex].worldY / gp.tilesize,
                        true
                );
            } else {
                gp.player.attackingTargetIndex = -1;
                aggro = false;
            }
        }

        if(tileDistance > 12) {
            worldX = gp.player.worldX;
            worldY = gp.player.worldY;
        }

    }

    @Override
    public void damageReaction() {
        health = 0;
        invincible = false;
        alive = true;
    }

    @Override
    public void checkCollision() {
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
        gp.cChecker.checkEntity(this, gp.interactiveTile);

        if(monsterIndex != 999 && gp.monster[gp.currentMap][monsterIndex] != null) {
            gp.player.damageMonster(monsterIndex, attack, knockBackPower, this);
        }
    }

    public void setDialogue() {
        dialogues[0][0] = "Goose: *Honk*\nPeace was never an option";

        dialogues[1][0] = "Goose: *Honk*";

        dialogues[2][0] = "Goose: *Honk*\nHas present for master";
        dialogues[2][1] = "The goose gave you\nsomething... but there\nwas an issue";
        dialogues[2][3] = "";
    }

    public void speak() {
        int rand = new Random().nextInt(100) + 1;
        if(rand > 90) {
            // the goose has something for you
            rand = new Random().nextInt(4) + 1;

            Entity present = null;
            boolean overrideDialogue = false;
            switch (rand) {
                case 1:
                    gp.player.coin++;
                    dialogues[2][1] = "The goose gave you a\nGold Coin. nice!";
                    gp.ui.addMessage("+1 Gold");
                    overrideDialogue = true;
                    break;
                case 2: present = new OBJ_Mana_Potion(gp); break;
                case 3: present = new OBJ_Health_Potion(gp); break;
                case 4:
                    dialogues[2][1] = "The goose gave you a\nLesson, +8 xp!";
                    gp.player.xp += 8;
                    gp.ui.addMessage("+8 xp");
                    overrideDialogue = true;
                    break;
            }
            dialogues[2][3] = "";
            if(!overrideDialogue) {
                dialogues[2][1] = "The goose gave you a\n" + present.name + " Nice!";
                if(!gp.player.tryAddInventory(present)) {
                    dialogues[2][3] = "But you can't carry it...";
                } else {
                    gp.ui.addMessage("+1 " + present.name);
                }
            }
            startDialogue(this, 2);
        } else {
            rand = new Random().nextInt(2) + 1;
            if(rand == 1) {
                startDialogue(this, 1);
            } else {
                startDialogue(this, 0);
            }
        }
    }

}
