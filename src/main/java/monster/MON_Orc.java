package monster;

import entity.Entity;
import object.*;
import org.example.Gamepanel;

import java.util.Random;

public class MON_Orc extends Entity{
    Gamepanel gp;
    public static final String objIDName = "Orc";

    public MON_Orc(Gamepanel gp) {
        super(gp);
        this.gp = gp; // THIS IS NEEDED, it crashes otherwise
        name = "Orc";
        type = Entity.EntityType.MONSTER;
        maxHealth = 20;
        health = maxHealth;
        attack = 7;
        defense = 3;
        xp = 8;
        defaultSpeed = 1;
        speed = defaultSpeed;
        knockBackPower = 0; // on top of the weapon's power
        speed = 1;

        // slimes are a lot smaller than most Entities
        solidArea.x = 4;
        solidArea.y = 4;
        solidArea.width = 40;
        solidArea.height = 44;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        attackArea.width = gp.tilesize;
        attackArea.height = gp.tilesize;

        currentWeapon = new OBJ_Sword(gp);

        getImage();
    }

    public void getImage() {
        //FIXME: gp is null, therefore we have to use the tileSize thing as ints

        up1 = setup("/monster/orc_up1", 64, 64);
        up2 = setup("/monster/orc_up2", 64, 64);
        left1 = setup("/monster/orc_left_1", 64, 64);
        left2 = setup("/monster/orc_left_2", 64, 64);
        right1 = setup("/monster/orc_right_1", 64, 64);
        right2 = setup("/monster/orc_right_2", 64, 64);
        down1 = setup("/monster/orc_down1", 64, 64);
        down2 = setup("/monster/orc_down2", 64, 64);

        attackUp1 = up1;
        attackUp2 = setup("/monster/orc_attack_up1", gp.tilesize, gp.tilesize*2);
        attackDown1 = down1;
        attackDown2 = setup("/monster/orc_attack_down1", gp.tilesize, gp.tilesize*2);
        attackRight1 = right1;
        attackRight2 = setup("/monster/orc_attack_left1", gp.tilesize*2, gp.tilesize);
        attackLeft1 = left1;
        attackLeft2 = setup("/monster/orc_attack_right1", gp.tilesize*2, gp.tilesize);
        //TODO: rename those
    }

    public void getAttackImage() {

    }

    public void setAction() {
        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int tileDistance = getTileDistance(gp.player);

        if(aggro) {
            if (checkStopFollowing(gp.player, 15, 100)) {aggro = false;}
            searchPath(getTargetCol(gp.player), getTargetRow(gp.player), false);

        } else {
            if(tileDistance < 2) {
                int i = new Random().nextInt(100) + 1;
                if(i > 50) {
                    aggro = true;
                }
            }

            chooseRandomDirection();

        }

        if(!attacking) {
            attemptAttack(40, gp.tilesize * 2, gp.tilesize * 2);
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
