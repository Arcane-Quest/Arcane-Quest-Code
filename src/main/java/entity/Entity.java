package entity;

import org.example.Gamepanel;
import org.example.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Entity {
    public Entity contents;
    public Boolean opened = false;
    public int useSound;

    public boolean canBreakRock = false;
    Gamepanel gp;
    public String name;
    public EntityType type;
    public int defaultSpeed = 3;
    public int price = 5;
    public int speed = defaultSpeed;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int xp;
    public int nextLevelExp;
    public int coin;
    public int startingMana;
    public int maxMana;
    public float mana;
    public Entity currentWeapon;
    public Entity[] currentSpell = new Entity[10]; // or magical weapon
    public Entity currentAmulet; // ring or something else that gives you power
    public Entity currentRing;
    public Entity currentShield;
    public Projectile projectile; //TODO: have 10 spell slots
    public int useCost;
    public int shotAvalibleCounter = 0;
    public int value;
    public boolean sellable = true;
    public int frameUpdateCounter = 8;
    public int knockBackPower = 0;
    public int lightRadius;
    public int guardCounter = 0;
    int offBalanceCounter = 0;
    public boolean offBalance = false;
    public int attackingTargetIndex = -1;
    public String objectUseKey;
    public String saveAbleKey;
    public EntitySerializableData saveableData = null;
    public int dialogueSignal;


    public enum rarity {
        NORMAL,
        UNCOMMON,
        RARE,
        LEGENDARY
    }
    public int attackValue;
    public int defenseValue;
    public boolean knockback = false;
    int knockBackCounter = 0;
    public rarity rarityLevel;
    public String description = "";

    public int worldX, worldY;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackRight1, attackRight2, attackLeft1, attackLeft2,
                        attackDown1, attackDown2, guardDown, guardUp, guardLeft, guardRight;
    public BufferedImage image, image2, image3;
    public String direction = "down";
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public boolean alive = true;
    public boolean dyingState = false;
    int dyingStateCounter = 0;
    boolean hpBarOn = false;
    int hpBarCounter; // not the value, just to measure when to show
    public boolean onPath = false;
    public boolean aggro = false;
    public boolean guarding = false;


    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); // default collider
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    // attack area changes based on the attack size
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn;
    public boolean collision = false;

    public int actionLockCounter = 0;
    public boolean attacking = false;

    public String[][] dialogues = new String[20][20];
    public int dialogueSet = 0;
    public int dialogueIndex = 0;

    public boolean invincible = false;
    public int invincibleCounter;

    public enum EntityType {
        PLAYER,
        NPC,
        MONSTER,
        OBJECT,
        WEAPON,
        SHIELD,
        SPELL,
        CONSUMABLE,
        COLLECTABLE,
        INTERACTABLE,
        RING,
        AMULET,
        LIGHT
    }


    // Entity Status
    public int maxHealth; // 1 = half a heart
    public int health;

    // Inventory
    public ArrayList<Entity> inventory = new ArrayList<>();
    public int maxInventorySize = 20;
    public boolean stackable = false;
    public int amount = 1;
    public Entity attacker; // for knockback collision logic
    public String knockbackDirection;


    public Entity(Gamepanel gp) {
        this.gp = gp;

    }

    public BufferedImage setup(String imageName, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage scaledImage = null;

        try {
            scaledImage = ImageIO.read(getClass().getResourceAsStream(imageName + ".png"));
            scaledImage = uTool.scaleImage(scaledImage, width, height);

        } catch (IOException e){
            e.printStackTrace();
        }
        return scaledImage;
    }

    public void facePlayer() {
        switch (gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }

    public void startDialogue(Entity entity, int setNum) {
        /*
        start dialogue sets the gamestate to DIALOGUE, this does not interrupt the update() or any other function.
        because of this the startDialogue only starts after update() and every other function is completed.
        This means if you set the setNum to something after you call the startDialogue, the ui class will be using that rather than
        the value you actually want. This means you need to offload it to the next update.
         */

        System.out.println("Starting dialogue: " + setNum);
        gp.ui.dialogueWithOptionsFlag = false;
        gp.gameState = Gamepanel.GameStates.DIALOGUE;
        gp.ui.npc = entity;
        dialogueSet = setNum;
    }

    public void startDialogue(Entity entity, int setNum, String options[]) {
        System.out.println("Starting dialogue: " + setNum);
        gp.ui.dialogueWithOptionsFlag = true;
        gp.ui.dialogueOptions = options;
        gp.gameState = Gamepanel.GameStates.DIALOGUE;
        gp.ui.npc = entity;
        dialogueSet = setNum;
    }

    public void speak() {}


    public void checkDrop() {

    }

    public void dropItem(Entity droppedItem) {
        for(int i = 0; i < gp.obj[1].length; i++) { // more black magic
            if(gp.obj[gp.currentMap][i] == null) {                 // in other words ignore the [1]
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].worldX = worldX;
                gp.obj[gp.currentMap][i].worldY = worldY;
                break;
            }
        }
    }

    public void dropItem(Entity droppedItem, int modX, int modY) {
        for(int i = 0; i < gp.obj[1].length; i++) { // more black magic
            if(gp.obj[gp.currentMap][i] == null) {                 // in other words ignore the [1]
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].worldX = worldX + modX;
                gp.obj[gp.currentMap][i].worldY = worldY + modY;
                break;
            }
        }
    }

    public void update() {
        if (knockback) {
            checkCollision();
            handleKnockBackReaction(collisionOn);
        } else if (attacking) {
            attack();
        } else {

            setAction();
            checkCollision();

            if (!collisionOn) {
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
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
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (shotAvalibleCounter < 85) {
            shotAvalibleCounter++;
        }
        if(offBalance) {
            offBalanceCounter++;
            if(offBalanceCounter > 60) {
                offBalance = false;
                offBalanceCounter = 0;
            }
        }
    }

    public void damagePlayer(int attack, int knockBackAmount) {
        for(int i = 0; i < gp.monster[1].length; i++) {
            if(gp.monster[gp.currentMap][i] != null && gp.monster[gp.currentMap][i] == this) {
                gp.player.attackingTargetIndex = i;
                break;
            }
        }

        if(gp.player.guarding) {
            String oppositeDirection = "";
            switch(direction) {
                case "left": oppositeDirection = "right"; break;
                case "right": oppositeDirection = "left"; break;
                case "up": oppositeDirection = "down";    break;
                case "down": oppositeDirection = "up";    break;
            }

            if(Objects.equals(gp.player.direction, oppositeDirection)) {
                // if the player is facing the attacker with a shield up, stop
                // Parry
                knockBack(this, gp.player, 2);
                if(gp.player.guardCounter < 10) {
                    attack = 0;
                    knockBack(this, gp.player, knockBackPower);
                    //Yes, this is supposed to be the target
                    offBalance = true; // make critical hits possible
                    spriteCounter -= 60;
                    // can't move for 1 second
                } else {
                    if (attack > gp.player.defense * 3) {
                        attack /= 3;
                    } else {
                        return;
                    }
                }
                // if the attack is so massive that the player just can't handle it
                // then let it through
            }
        }
        if(!gp.player.invincible) {
            int damage = attack - gp.player.defense;
            if(damage <= 0) {
                damage = 1;
            }
            gp.player.health -= damage;
            knockBack(gp.player, this, knockBackAmount);
            gp.player.invincible = true;
        }
    }

    public void setAction() {}

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        BufferedImage image = null;

        if (worldX + gp.tilesize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tilesize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tilesize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tilesize < gp.player.worldY + gp.player.screenY) {

            int tempScreenX = screenX;
            int tempScreenY = screenY;


            switch(direction) {
                case "up":
                    if (!attacking) {
                        if (spriteNum == 1) {image = up1;}
                        if (spriteNum == 2){image = up2;}
                    } else {
                        // because this image is 16*32 we need to move the player
                        // this doesn't apply to down and right
                        if (spriteNum == 1) {image = attackUp1;}
                        if (spriteNum == 2){image = attackUp2; tempScreenY = screenY - gp.tilesize;}
                        // this is the attack frame
                        //TODO: change this to both if you update the sprites
                    }
                    break;
                case "down":
                    if (!attacking) {
                        if (spriteNum == 1) {image = down1;}
                        if (spriteNum == 2) {image = down2;}
                    } else {
                        if (spriteNum == 1) {image = attackDown1;}
                        if (spriteNum == 2) {image = attackDown2;}
                    }
                    break;
                case "left":
                    if (!attacking) {
                        if (spriteNum == 1) {image = left1;}
                        if (spriteNum == 2) {image = left2;}
                    } else {
                        // see reasoning in case "up"
                        if (spriteNum == 1) {image = attackLeft1;}
                        if (spriteNum == 2) {image = attackLeft2; tempScreenX = screenX - gp.tilesize;}
                        //TODO: change this to both if you update the sprites
                    }
                    break;
                case "right":
                    if (!attacking) {
                        if (spriteNum == 1) {image = right1;}
                        if (spriteNum == 2) {image = right2;}
                    } else {
                        if (spriteNum == 1) {image = attackRight1;}
                        if (spriteNum == 2) {image = attackRight2;}
                    }
                    break;
            }

            if(type == EntityType.MONSTER && hpBarOn) {
                double oneScale = (double)gp.tilesize/maxHealth;
                double hpBarValue = oneScale*health;

                int x = screenX;
                int y = screenY;

                if(tempScreenX > worldX) {
                    x = worldX;
                }
                if(tempScreenY > worldY) {
                    y = worldY;
                }
                int rightOffset = gp.screenWidth - screenX;
                if(rightOffset > gp.worldWidth - worldX) {
                    x = gp.screenWidth - (gp.worldWidth - worldX);
                }
                int bottomOffset = gp.screenHeight - screenY;
                if(bottomOffset > gp.worldHeight - worldY) {
                    y = gp.screenHeight - (gp.worldHeight - worldY);
                }
                g2.setColor(Color.BLACK);
                g2.fillRoundRect(x-2, y-17, gp.tilesize+4, 14, 3, 4);
                g2.setColor(new Color(174, 46, 46));
                g2.fillRoundRect(x, y - 15, (int)hpBarValue, 10, 3, 3);

                hpBarCounter++;
                if(hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible) {
                hpBarOn = true;
                hpBarCounter = 0; // reset the counter if attacked again
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4F));
            }
            if (dyingState) {
                dyingAnimation(g2);
            }

            int x = tempScreenX;
            int y = tempScreenY;

            if(tempScreenX > worldX) {
                x = worldX;
            }
            if(tempScreenY > worldY) {
                y = worldY;
            }
            int rightOffset = gp.screenWidth - screenX;
            if(rightOffset > gp.worldWidth - worldX) {
                x = gp.screenWidth - (gp.worldWidth - worldX);
            }
            int bottomOffset = gp.screenHeight - screenY;
            if(bottomOffset > gp.worldHeight - worldY) {
                y = gp.screenHeight - (gp.worldHeight - worldY);
            }

            g2.drawImage(image, x, y,null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
        }
    }
    public void damageReaction() {}

        public void dyingAnimation(Graphics2D g2) {
            dyingStateCounter++;

            if(dyingStateCounter <= 5) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1F));
            }

            if(dyingStateCounter > 5 && dyingStateCounter <= 10) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
            }

            if(dyingStateCounter > 10 && dyingStateCounter <= 15) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1F));
            }

            if(dyingStateCounter > 15 && dyingStateCounter <= 20) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
            }
            if(dyingStateCounter > 20 && dyingStateCounter <= 25) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1F));
            }

            if(dyingStateCounter > 25 && dyingStateCounter <= 30) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1F));
            }
            if(dyingStateCounter > 30 && dyingStateCounter <= 35) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
            }
            if(dyingStateCounter > 35 && dyingStateCounter <= 40) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1F));
            }
            if(dyingStateCounter > 45) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
                alive = false;
            }
        }

        public boolean use(Entity entity) {
        gp.gameState = Gamepanel.GameStates.DIALOGUE;
        gp.ui.currentDialogue = "i forgor how to use this...\nhope no one finds out.";
        return false;
    }
        public boolean interact(Entity entity) {return false;} // true means to remove on complete


    public void generateParticle(Entity generator, Entity target) {
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleLifetime();

        Particle particle = new Particle(gp, target, color, size, speed, maxLife, -2, -1);
        Particle particle2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);
        Particle particle3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);
        Particle particle4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);

        gp.particleList.add(particle);
        gp.particleList.add(particle2);
        gp.particleList.add(particle3);
        gp.particleList.add(particle4);
    }

    public Color getParticleColor() {
        return new Color(237, 0, 255);
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

    public void checkCollision() {
        collisionOn = false;

        int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        if (this.type == EntityType.PLAYER) {
            if(gp.npc[gp.currentMap][npcIndex] != null &&
                    Objects.equals(gp.npc[gp.currentMap][npcIndex].name, "Goose")) {
                collisionOn = false;
            }
        }
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);

        gp.cChecker.checkEntity(this, gp.monster);
        gp.cChecker.checkEntity(this, gp.interactiveTile);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if(this.type == EntityType.MONSTER && contactPlayer) {
            damagePlayer(attack, 1);

        }
    }

    public void searchPath(int goalCol, int goalRow, boolean finishOnDestination) {
        int startCol = (worldX + solidArea.x)/gp.tilesize;
        int startRow = (worldY + solidArea.y)/gp.tilesize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);
        if(gp.pFinder.search() == true) {
            // next worldX and Y
            int nextX = gp.pFinder.pathList.get(0).col * gp.tilesize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tilesize;

            // solid area positions
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;
            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tilesize) {
                direction = "up";
            }
            else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tilesize) {
                direction = "down";
            }
            else if (enTopY >= nextY && enBottomY < nextY + gp.tilesize) {
                // either left or right
                if(enLeftX > nextX) {
                    direction = "left";
                }
                if(enLeftX < nextX) {
                    direction = "right";
                }
            }
            else if (enTopY > nextY && enLeftX > nextX) {
                // either up or left
                direction = "up";
                checkCollision();
                if(collisionOn) {
                    direction = "left";
                }
            }
            else if (enTopY > nextY && enLeftX < nextX) {
                // up or right
                direction = "up";
                checkCollision();
                if(collisionOn) {
                    direction = "right";
                }
            }
            else if (enTopY < nextY && enLeftX > nextX) {
                // down or left
                direction = "down";
                checkCollision();
                if(collisionOn) {
                    direction = "left";
                }
            }
            else if (enTopY < nextY && enLeftX < nextX) {
                // down or right
                direction = "down";
                checkCollision();
                if(collisionOn) {
                    direction = "right";
                }
            }
            // once goal reached stop
            int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;
            if (finishOnDestination && nextCol == goalCol && nextRow == goalRow) {
                onPath = false;
            }
        }
    }

    public int getDetected(Entity user, Entity target[][], String targetString) {
        int index = 999;

        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();

        switch (user.direction) {
            case "up": nextWorldY = user.getTopY() - gp.player.speed; break;
            case "down": nextWorldY = user.getBottomY() + gp.player.speed; break;
            case "left": nextWorldX = user.getLeftX() - gp.player.speed; break;
            case "right": nextWorldX = user.getRightX() + gp.player.speed; break;
        }
        int col = nextWorldX/gp.tilesize;
        int row = nextWorldY/gp.tilesize;

        for (int i = 0; i < target[1].length; i++) {
            if (target[gp.currentMap][i] != null) {
                if (target[gp.currentMap][i].getCol() == col &&
                        target[gp.currentMap][i].getRow() == row &&
                        Objects.equals(target[gp.currentMap][i].name, targetString)) {
                    index = i;
                    break;
                }
            }
        }

        return index;

    }

    public int getCol() {
        return (worldX + solidArea.x)/gp.tilesize;
    }

    public int getRow() {
        return (worldY + solidArea.y)/gp.tilesize;
    }

    public int getLeftX() {
        return worldX + solidArea.x;
    }

    public int getRightX() {
        return worldX + solidArea.x + solidArea.width;
    }

    public int getTopY() {
        return worldY + solidArea.y;
    }

    public int getBottomY() {
        return worldY + solidArea.y + solidArea.height;
    }

    public int getXDistance(Entity target) {
        return Math.abs(worldX - target.worldX);
    }
    public int getYDistance(Entity target) {
        return Math.abs(worldY - target.worldY);
    }

    public int getTileDistance(Entity target) {
        return (getXDistance(target) + getYDistance(target))/gp.tilesize;
    }

    public int getTargetCol(Entity target) {
        return (target.worldX + target.solidArea.x)/gp.tilesize;
    }

    public int getTargetRow(Entity target) {
        return (target.worldY + target.solidArea.y)/gp.tilesize;
    }

    public boolean checkStopFollowing(Entity target, int distance, int rate) {
        if(getTileDistance(target) > distance) {
            int i = new Random().nextInt(rate);
            if(i == 0) {
                return true;
            }
        }
        return false;
    }

    public void checkShootConditions(int rate, int shotInterval) {
        int i = new Random().nextInt(rate);
        if (i == 0 && !projectile.alive && shotAvalibleCounter >= projectile.maxHealth) {
            projectile.set(worldX, worldY, direction, true, this);
            // add the projectile to the array
            for (int ii = 0; ii < gp.projectile[1].length; ii++) {
                // yes, it burns my i s too
                if(gp.projectile[gp.currentMap][ii] == null) {
                    gp.projectile[gp.currentMap][ii] = projectile;
                    break;
                }
            }

            shotAvalibleCounter = 0;
        }
    }

    public void determineAggro(Entity target, int distance, int rate) {
        if(getTileDistance(target) > distance) {
            int i = new Random().nextInt(rate);
            if(i == 0) {
                aggro = true;
            }
        }
    }

    public void chooseRandomDirection() {
        actionLockCounter++;

        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1; // 1-100
            if (i <= 25) {direction = "up";}
            else if (i > 25 && i <= 50) {direction = "down";}
            else if (i > 50 && i <= 75) {direction = "left";}
            else if (i > 75 && i <= 100) {direction = "right";}
            actionLockCounter = 0;
        }
    }

    public void knockBack(Entity target, Entity attacker, int knockBackPower) {
        this.attacker = attacker;
        target.knockbackDirection = attacker.direction;
        target.speed += knockBackPower;
        target.knockback = true;

    }


    public void attack() {
        spriteCounter++;

        if(spriteCounter <= 5) {
            spriteNum = 1;
        }
        if(spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;

            // save player's position values
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // adjust values temporarily to calculate attack box
            switch (direction) {
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }

            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // check using updated values
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            attackingTargetIndex = monsterIndex;

            // restore to original values
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

            if(type == EntityType.MONSTER) {
                if(gp.cChecker.checkPlayer(this)) {
                    damagePlayer(attack, knockBackPower);
                } else if (currentWeapon != null) {
                    damagePlayer(attack, currentWeapon.knockBackPower+knockBackPower);
                    knockBack(gp.player, this, knockBackPower);

                }
            } else {
                gp.player.damageMonster(monsterIndex, attack, currentWeapon.knockBackPower, this);
                int iTileIndex = gp.cChecker.checkEntity(this, gp.interactiveTile);
                gp.player.damageInteractiveTile(iTileIndex);

                int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
                gp.player.damageProjectile(projectileIndex);
            }
        }
        if(spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void attemptAttack(int rate, int straight, int horizontal) {
        boolean targetInRange = false;
        int xDistance = getXDistance(gp.player);
        int yDistance = getYDistance(gp.player);

        switch (direction) {
            case "up":
                if(gp.player.worldY < worldY && yDistance < straight && xDistance < horizontal) {
                    targetInRange = true;
                }
                break;
            case "down":
                if(gp.player.worldY > worldY && yDistance < straight && xDistance < horizontal) {
                    targetInRange = true;
                }
                break;
            case "left":
                if(gp.player.worldX < worldX && xDistance < straight && yDistance < horizontal) {
                    targetInRange = true;
                }
                break;
            case "right":
                if(gp.player.worldX > worldX && xDistance < straight && yDistance < horizontal) {
                    targetInRange = true;
                }
                break;
        }
        if(targetInRange) {
            int i = new Random().nextInt(rate);
            if (i == 0) {
                attacking = true;
                spriteNum = 1;
                spriteCounter = 0;
                shotAvalibleCounter = 0;
                knockBack(gp.player, this, knockBackPower);
            }
        }
    }

    public void handleKnockBackReaction(boolean isColliding) {
        if(isColliding) {
            knockBackCounter = 0;
            knockback = false;
            speed = defaultSpeed;
        } else {
            switch (knockbackDirection) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }
        knockBackCounter++;
        if(knockBackCounter == 10) {
            knockBackCounter = 0;
            knockback = false;
            speed = defaultSpeed;
        }
    }
    public void setContents(Entity contents) {}
    public void setInformation(String messages[][], int setNum) {}

    public boolean loadfromsaveabledata(){
        try {
            if (saveAbleKey == null || saveAbleKey.equals("")) {
                return false;
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("saves/main_save/entity_data/" + saveAbleKey + ".dat")));
            saveableData = (EntitySerializableData) ois.readObject();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveData() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("saves/main_save/entity_data/" + saveAbleKey + ".dat")));
            if (saveableData == null) {
                saveableData = new EntitySerializableData();
            }
            oos.writeObject(saveableData);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleDialogueChosenEvent(String event) {}

}