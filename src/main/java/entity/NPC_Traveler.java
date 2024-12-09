package entity;

import org.example.Gamepanel;
import org.example.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class NPC_Traveler extends Entity {


    public NPC_Traveler(Gamepanel gp) {
        super(gp);

        direction = "down";
        speed = 1;

        getImage();
        setDialogue();

        dialogueSet = -1;
    }

    public void getImage() {
        up1 = setup("/npc/shop_npc_back", gp.tilesize, gp.tilesize);
        up2 = setup("/npc/shop_npc_back2", gp.tilesize, gp.tilesize);
        down1 = setup("/npc/Shop_Npc", gp.tilesize, gp.tilesize);
        down2 = setup("/npc/Shop_Npc2", gp.tilesize, gp.tilesize);
        left1 = setup("/npc/shop_npc_left1", gp.tilesize, gp.tilesize);
        left2 = setup("/npc/shop_npc_left2", gp.tilesize, gp.tilesize);
        right1 = setup("/npc/shop_npc_right1", gp.tilesize, gp.tilesize);
        right2 = setup("/npc/shop_npc_right2", gp.tilesize, gp.tilesize);
        // resources folder needs to be marked as root resources and in the main (not root) directory
        // i.e. the one before java
    }

    public void setDialogue() {
        dialogues[0][0] = "Have I seen you before?";
        dialogues[0][1] = "well anyways you should\ncome see this.";
        dialogues[0][2] = "follow me";

        dialogues[1][0] = "Just in that chest over here";

        dialogues[2][0] = "Press \"b\" to switch to your\ninventory and select\nthe rock smasher";

        dialogues[3][0] = "now you can break rocks";
    }


    public void setAction() {
        if (onPath) {
            int goalCol = 8;
            int goalRow = 44;

            searchPath(goalCol, goalRow, true);
        }
        //remove that and uncomment to fix
//        } else {
//
//            actionLockCounter++;
//
//            if (actionLockCounter == 120) {
//                Random random = new Random();
//                int i = random.nextInt(100) + 1; // 1-100
//                if (i <= 25) {
//                    direction = "up";
//                } else if (i > 25 && i <= 50) {
//                    direction = "down";
//                } else if (i > 50 && i <= 75) {
//                    direction = "left";
//                } else if (i > 75 && i <= 100) {
//                    direction = "right";
//                }
//                actionLockCounter = 0;
//            }
//
//        }
    }

    @Override
    public void update() {
        setAction();
        checkCollision();

        if (!collisionOn && onPath) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }

            spriteCounter++;
            if (spriteCounter > frameUpdateCounter) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1; // first foot second foot
                }
                spriteCounter = 0;
            }
        }


        if (invincible) {
            invincibleCounter++;
            if(invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if(shotAvalibleCounter < 85) {
            shotAvalibleCounter++;
        }
    }

    public void speak() {
        onPath = true;
        dialogueSet++;
        if(dialogues[dialogueSet][0] == null) {
            dialogueSet = 1;
        }

        facePlayer();
        startDialogue(this, dialogueSet);





    }
}