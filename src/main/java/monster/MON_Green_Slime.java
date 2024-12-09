package monster;

import entity.Entity;
import object.*;
import org.example.Gamepanel;

import java.util.Random;

public class MON_Green_Slime extends Entity {
    Gamepanel gp;
    public static final String objIDName = "Green_Slime";

    public MON_Green_Slime(Gamepanel gp) {
        super(gp);
        this.gp = gp; // THIS IS NEEDED, it crashes otherwise
        name = "Green Slime";
        type = EntityType.MONSTER;
        maxHealth = 4;
        health = maxHealth;
        attack = 4;
        defense = 0;
        xp = 3;
        defaultSpeed = 1;
        speed = defaultSpeed;
        knockBackPower = 1;

        // slimes are a lot smaller than most Entities
        solidArea.x = 3;
        solidArea.y = 42;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        projectile = new OBJ_Rock(gp);

        getImage();
    }

    public void getImage() {
        //FIXME: gp is null, therefore we have to use the tileSize thing as ints

        up1 = setup("/monster/green_slime", 64, 64);
        up2 = setup("/monster/green_slime_jump", 64, 64);
        left1 = setup("/monster/green_slime", 64, 64);
        left2 = setup("/monster/green_slime_jump", 64, 64);
        right1 = setup("/monster/green_slime", 64, 64);
        right2 = setup("/monster/green_slime_jump", 64, 64);
        down1 = setup("/monster/green_slime", 64, 64);
        down2 = setup("/monster/green_slime_jump", 64, 64);
    }

    public void setAction() {
        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int tileDistance = getTileDistance(gp.player);

        if(aggro) {
            if (checkStopFollowing(gp.player, 15, 100)) {aggro = false;}
            searchPath(getTargetCol(gp.player), getTargetRow(gp.player), false);
            checkShootConditions(200, 30); // shoot if it is possible

        } else {
            if(tileDistance < 2) {
                int i = new Random().nextInt(100) + 1;
                if(i > 50) {
                    aggro = true;
                }
            }

            chooseRandomDirection();

        }

    }

    @Override
    public void damageReaction() {
        aggro = true;
        actionLockCounter = 0;
        direction = gp.player.direction; // go away from the player
    }

    public void checkDrop() {
        int rand = new Random().nextInt(100) + 1;
        // drop a mana and...
        if (rand < 30) {
            dropItem(new OBJ_Mana_Crystal(gp), 0, gp.tilesize);
        }

        if (rand < 45 && rand > 20) {
            dropItem(new OBJ_Heart(gp));
        } else if (rand >= 45 && rand < 70) {
            dropItem(new OBJ_Coin(gp));
        }
    }
}