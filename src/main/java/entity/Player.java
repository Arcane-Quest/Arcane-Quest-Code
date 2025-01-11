package entity;

import object.*;
import org.example.Gamepanel;
import org.example.Keyhandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Player extends Entity {
    Keyhandler keyH;

    public final int screenX;
    public final int screenY;
    public boolean lightUpdated = false;
    public OBJ_Rune_Paper[] spells;


    public Player(Gamepanel gp, Keyhandler keyH) {
        super(gp);

        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tilesize/2);
        screenY = gp.screenHeight/2 - (gp.tilesize/2); // because it is relative to the top left image corner

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 22;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = gp.tilesize / 2;
        solidArea.height = gp.tilesize / 2;

        attackArea.width = 36;
        attackArea.height = 36; // these will be the default (sword)

        setDefaultValues();

        xp = 20;
        spells = new OBJ_Rune_Paper[10];
        spells[0] = new OBJ_Rune_Paper(gp);
        spells[0].entitySetup(1);
    }

    public void restoreStatus() {
        speed = defaultSpeed;
        health = maxHealth;
        mana = maxMana;
        invincible = false;
        attacking = false;
        guarding = false;
        knockback = false;
        lightUpdated = true;

        // reset counters
        spriteCounter = 0;
        actionLockCounter = 0;
        invincibleCounter = 0;
        shotAvalibleCounter = 0;
        dyingStateCounter = 0;
        hpBarCounter = 0;
        knockBackCounter = 0;
        guardCounter = 0;
        offBalanceCounter = 0;
    }

    public void setDefaultValues(){
        getPlayerImage();

        maxHealth = 6; // 1 is half a heart, therefore the player starts with 3 hearts
        health = maxHealth;
        worldX = gp.tilesize * 23;
        worldY = gp.tilesize * 21; // x tiles away from world origin
        defaultSpeed = 3;
        speed = defaultSpeed;
        direction = "down";
        level = 1;
        strength = 1;
        dexterity = 1;
        xp = 0;
        coin = 20;
        nextLevelExp = 5;
        currentWeapon = new OBJ_Sword(gp);
        currentShield = new OBJ_Shield(gp);
        currentRing = null;
        attack = getAttack(); // weapon strength * player strength
        defense = getDefense(); // shield strength * player dexterity
        projectile = new OBJ_Fireball(gp);
        startingMana = 4;
        maxMana = startingMana;
        mana = startingMana;
        restoreStatus();
        setItems();
    }

    public void setItems() {
        // restore player inventory
        inventory.clear();
        if(currentWeapon != null) {
            inventory.add(currentWeapon);
        }
        if(currentShield != null){
            inventory.add(currentShield);
        }

        inventory.add(new OBJ_Rock_Smasher(gp));

        inventory.add(new OBJ_Lantern(gp));

        inventory.add(new OBJ_Ability_focus_crystal(gp));


    }

    public int getAttack() {
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }
    public int getDefense() {
        if(currentShield != null){
            return defense = dexterity * currentShield.defenseValue;
        }
            return defense = dexterity;
    }

    public void getPlayerImage() {
        // resources folder needs to be marked as root resources and in the main (not root) directory
        // i.e. the one before java

        up1 = setup("/player/up1", gp.tilesize, gp.tilesize);
        up2 = setup("/player/up2", gp.tilesize, gp.tilesize);
        down1 = setup("/player/down1", gp.tilesize, gp.tilesize);
        down2 = setup("/player/down2", gp.tilesize, gp.tilesize);
        left1 = setup("/player/left1", gp.tilesize, gp.tilesize);
        left2 = setup("/player/left2", gp.tilesize, gp.tilesize);
        right1 = setup("/player/right1", gp.tilesize, gp.tilesize);
        right2 = setup("/player/right2", gp.tilesize, gp.tilesize);

        attackUp1 = setup("/player/up1", gp.tilesize, gp.tilesize);
        attackUp2 = setup("/player/attack_up", gp.tilesize, gp.tilesize*2);
        attackDown1 = setup("/player/down1", gp.tilesize, gp.tilesize);
        attackDown2 = setup("/player/attack_down1", gp.tilesize, gp.tilesize*2);
        attackRight1 = setup("/player/right1", gp.tilesize, gp.tilesize);
        attackRight2 = setup("/player/attack_right2", gp.tilesize*2, gp.tilesize);
        attackLeft1 = setup("/player/left1", gp.tilesize, gp.tilesize);
        attackLeft2 = setup("/player/attack_left2", gp.tilesize*2, gp.tilesize);

        guardDown = setup("/player/shield_down", gp.tilesize, gp.tilesize);
        guardLeft = setup("/player/shield_left", gp.tilesize, gp.tilesize);
        guardRight = setup("/player/shield_right", gp.tilesize, gp.tilesize);
        guardUp = up1;

    }


    public void update() {
        if(!guarding && mana < maxMana) {
            mana += 0.01F;
        }

        if(knockback) {
            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkEntity(this, gp.monster);
            gp.cChecker.checkEntity(this, gp.interactiveTile);
            gp.eHandler.checkEvent();

            handleKnockBackReaction(collisionOn);
        } else if(attacking) {
            guarding = false;
            guardCounter = 0;
            attack();
        } else if (keyH.shieldKeyPressed) {
            guarding = true;
            guardCounter++;
        } else if (keyH.upPressed || keyH.rightPressed ||
                keyH.downPressed || keyH.leftPressed || keyH.enterPressed) {
            guarding = false;
            guardCounter = 0;

            if(keyH.upPressed) {
                direction = "up";
            }
            else if(keyH.downPressed) {
                direction = "down";

            } else if(keyH.leftPressed) {
                direction = "left";

            } else if(keyH.rightPressed) {
                direction = "right";

            }

            if(gp.ui.removeToolTipDelayCounter > 0) {
                gp.ui.removeToolTipDelayCounter--;
            } else {
                gp.ui.setToolTip("", 0);
            }
            // set to empty if not changed

            collisionOn = false;
            gp.cChecker.checkTile(this);
            int objectIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objectIndex);

            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            if(npcIndex != 999 && gp.npc[gp.currentMap][npcIndex].name == "Goose") {
                collisionOn = false;
            }

            interactNPC(npcIndex);

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            gp.cChecker.checkEntity(this, gp.interactiveTile);

            gp.eHandler.checkEvent();

            spriteCounter++;
            if (!collisionOn && !keyH.enterPressed) {
                switch(direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
                }
            }
            gp.keyH.enterPressed = false;

        } else {
            guarding = false;
            guardCounter = 0;
        }
        for (int i = 0; i < spells.length; i++) {
            if(spells[i] != null) {
                spells[i].cooldownCounter--;
                if (spells[i].cooldownCounter < 0) {
                    spells[i].cooldownCounter = 0;
                }
            }
        }
        if (gp.keyH.spellSlotNum != -1 && spells[gp.keyH.spellSlotNum] != null) {
        if (gp.keyH.spellKeyPressed && shotAvalibleCounter >= 30 && spells[gp.keyH.spellSlotNum].cooldownCounter < 1) {

                spells[gp.keyH.spellSlotNum].cooldownCounter = spells[gp.keyH.spellSlotNum].cooldownTime;
                projectile = spells[gp.keyH.spellSlotNum].shoot(this);
            }


            gp.keyH.spellKeyPressed = false;
            gp.keyH.spellSlotNum = -1;

            shotAvalibleCounter = 0; // cooldown
        }



        if(spriteCounter > 8 && !attacking) { // attack() has it's own logic and relies on this
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1; // first foot second foot
            }
            spriteCounter = 0;
        }

        if (invincible) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }

        }
        if(shotAvalibleCounter < 85) {
            shotAvalibleCounter++;
        }

        gp.keyH.enterPressed = false; // after all logic checks requiring this
        gp.keyH.spellKeyPressed = false; // update every logic check

        if (health <= 0) {
            gp.gameState = Gamepanel.GameStates.GAMEOVER;
            gp.ui.commandNum = -1;
        }
    }

    public void pickUpObject(int i) {
        //TODO: see if the interaction is marked as automatic or manual and act
        // based on that.
        if (i != 999) {
            if (gp.obj[gp.currentMap][i].type == EntityType.INTERACTABLE) {
                if(gp.obj[gp.currentMap][i].interact(this)) {
                    gp.obj[gp.currentMap][i] = null;
                }
            } else if (gp.obj[gp.currentMap][i].type == EntityType.COLLECTABLE) {
                // only interact, dont collect
                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;
            } else {

                String text;
                // 999 is default and therefore the object doesn't exist
                if (tryAddInventory(gp.obj[gp.currentMap][i])) {
                    text = "+1 " + gp.obj[gp.currentMap][i].name;
                    gp.obj[gp.currentMap][i] = null;
                } else {
                    text = "Inventory full!";
                }
                gp.ui.addMessage(text);
            }
        }
    }

    public void interactNPC(int i) {
        if(gp.ui.removeToolTipDelayCounter > 0) {
            gp.ui.removeToolTipDelayCounter--;
        } else {
            gp.ui.setToolTip("", 0);
        }

        if(i != 999) {
            gp.ui.setToolTip("Press Enter to interact");
            if (gp.keyH.enterPressed) {
                if (i != 999) {
                    gp.npc[gp.currentMap][i].speak();
            }
        }
        } else if(gp.keyH.enterPressed) {
            attacking = true;
        }
    }

    public void contactMonster(int i) {
        if (i != 999) {
            if (!invincible && !gp.monster[gp.currentMap][i].dyingState) {
                int damage = gp.monster[gp.currentMap][i].attack - defense;
                if(damage <= 0) {
                    damage = 1;
                }

                health -= damage;
                invincible = true;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        int attackSpriteAddition = 0;


        switch(direction) {
            case "up":
                if (!attacking) {
                    if (spriteNum == 1) {image = up1;}
                    if (spriteNum == 2){image = up2;}
                } else {
                    // because this image is 16*32 we need to move the player
                    // this doesn't apply to down and right
                    if (spriteNum == 1) {image = attackUp1;}
                    if (spriteNum == 2) {image = attackUp2; tempScreenY = screenY - gp.tilesize; attackSpriteAddition = gp.tilesize;}
                    // this is the attack frame
                    //TODO: change this to both if you update the sprites
                }
                if(guarding) {image = guardUp;}
                break;
            case "down":
                if (!attacking) {
                    if (spriteNum == 1) {image = down1;}
                    if (spriteNum == 2) {image = down2;}
                } else {
                    if (spriteNum == 1) {image = attackDown1;}
                    if (spriteNum == 2) {image = attackDown2;}
                }
                if(guarding) {image = guardDown;}
                break;
            case "left":
                if (!attacking) {
                    if (spriteNum == 1) {image = left1;}
                    if (spriteNum == 2) {image = left2;}
                } else {
                    // see reasoning in case "up"
                    if (spriteNum == 1) {image = attackLeft1;}
                    if (spriteNum == 2) {image = attackLeft2; tempScreenX = screenX - gp.tilesize; attackSpriteAddition = gp.tilesize;}
                    //TODO: change this to both if you update the sprites
                }
                if(guarding) {image = guardLeft;}
                break;
            case "right":
                if (!attacking) {
                    if (spriteNum == 1) {image = right1;}
                    if (spriteNum == 2) {image = right2;}
                } else {
                    if (spriteNum == 1) {image = attackRight1;}
                    if (spriteNum == 2) {image = attackRight2;}
                }
                if(guarding) {image = guardRight;}
                break;
        }


        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F));
        }

        int x = tempScreenX;
        int y = tempScreenY;

        if(tempScreenX > worldX) {
            x = worldX;
            if(direction == "left") {
                x -= attackSpriteAddition;
            }
        }
        if(tempScreenY > worldY) {
            y = worldY;
            if(direction == "up") {
                y -= attackSpriteAddition;
            }
        }
        int rightOffset = gp.screenWidth - tempScreenX;
        if(rightOffset > gp.worldWidth - worldX) {
            x = gp.screenWidth - (gp.worldWidth - worldX);
            if(direction == "left") {
                x -= attackSpriteAddition;
            }
        }
        int bottomOffset = gp.screenHeight - tempScreenY;
        if(bottomOffset > gp.worldHeight - worldY) {
            y = gp.screenHeight - (gp.worldHeight - worldY);
            if(direction == "up") {
                y -= attackSpriteAddition;
            }
        }

        g2.drawImage(image, x, y, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));

    }

    public void damageInteractiveTile(int index) {
        if(index != 999 && gp.interactiveTile[gp.currentMap][index].destructible
                && gp.interactiveTile[gp.currentMap][index].checkItemRequirement(this)) {
            gp.interactiveTile[gp.currentMap][index].generateParticle(gp.interactiveTile[gp.currentMap][index],
                    gp.interactiveTile[gp.currentMap][index]);
            gp.interactiveTile[gp.currentMap][index] = null;
        }
    }



    public void damageProjectile(int i) {
        if (i != 999) {
            Entity projectile = gp.projectile[gp.currentMap][i];
            projectile.alive = false;
            generateParticle(projectile, projectile);
        }
    }

    public void damageMonster(int i, int attackDamage, int knockBackPower, Entity attacker) {
        if (i != 999) {
            if (!gp.monster[gp.currentMap][i].invincible) {
                if(knockBackPower > 0) {
                    knockBack(gp.monster[gp.currentMap][i], attacker, knockBackPower);
                }

                if(gp.monster[gp.currentMap][i].offBalance) {
                    attack *= 3;
                }

                int damage = attackDamage - gp.monster[gp.currentMap][i].defense;
                if (damage < 0) {
                    damage = 1;
                }
                gp.monster[gp.currentMap][i].health -= damage;
                gp.monster[gp.currentMap][i].invincible = true;
                gp.monster[gp.currentMap][i].damageReaction();

                if (gp.monster[gp.currentMap][i].health <= 0) {
                    gp.ui.addMessage(gp.monster[gp.currentMap][i].name + " slain");
                    xp += gp.monster[gp.currentMap][i].xp;
                    if (gp.monster[gp.currentMap][i].xp != 0) {
                        gp.ui.addMessage("+ " + gp.monster[gp.currentMap][i].xp + " xp");
                    }
                    gp.monster[gp.currentMap][i].dyingState = true;
                    // to the void with you fiend
                }
            }
        }
    }

    public boolean checkLevelUp() {
        if (xp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp*2;
            maxHealth += 2; // 2 -> 1 heart
            strength++;
            dexterity++;
            maxMana++;
            attack = getAttack();
            defense = getDefense();
            //TODO: level up sound
            gp.gameState = Gamepanel.GameStates.DIALOGUE;
            dialogues[0][0] = "Level " + level + " reached!\nYour abilities and\nopportunities increase.";
            startDialogue(this, 0);
            return true;
        }
        return false;
    }

    public void selectItem() {
        int itemIndex = gp.ui.getItemIndexForSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

        if(itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get((itemIndex));

            switch(selectedItem.type) {
                case WEAPON:
                    currentWeapon = selectedItem;
                    attack = getAttack();
                    break;
                case SHIELD:
                    currentShield = selectedItem;
                    defense = getDefense();
                    break;
                case CONSUMABLE, SPELL:
                    if(selectedItem.use(this)) {
                        if(selectedItem.amount > 1) {
                            selectedItem.amount--;
                        } else {
                            inventory.remove(itemIndex);
                        }
                    }
                    break;
                case RING, LIGHT:
                    if(currentRing != selectedItem) {
                        currentRing = selectedItem;
                    }
                    else {
                        currentRing = null;
                    }
                    lightUpdated = true;
                    break;
                case AMULET:
                    if(currentRing != selectedItem) {
                        currentAmulet = selectedItem;
                    } else {
                        currentAmulet = null;
                    }
                    break;
                default:
                    System.out.println("Error: no usable specified for item: " + selectedItem);
                    break;
            }
        }
    }

    public void retry() {
        worldX = gp.tilesize * 23;
        worldY = gp.tilesize * 21;
        direction = "down";

        health = maxHealth;
        mana = maxMana;
        invincible = false;
    }



    public int searchItemInInventory(String itemName) {
        int itemIndex = 999;
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i).name.equals(itemName)) {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }

    public boolean tryAddInventory(Entity item) {
        boolean canObtainItem = false;
        Entity newItem = gp.entityGenerator.getObjectFromString(item.name);
        newItem.saveableData = item.saveableData;
        newItem.loadfromsaveabledata();
        if(newItem.stackable) {
            int index = searchItemInInventory(newItem.name);
            if(index != 999) {
                inventory.get(index).amount++;
                canObtainItem = true;
            } else { // new item
                if(inventory.size() != maxInventorySize) {
                    inventory.add(newItem);
                    canObtainItem = true;
                }
            }
        } else {
            if(inventory.size() != maxInventorySize) {
                inventory.add(newItem);
                canObtainItem = true;
            }
        }
        return canObtainItem;
    }

    public int getCurrentWeaponSlot() {
        int currentWeaponSlot = 0;
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i) == currentWeapon) {
                currentWeaponSlot = i;
            }
        }
        return currentWeaponSlot;
    }

    public int getCurrentShieldSlot() {
        int currentShieldSlot = 0;
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i) == currentShield) {
                currentShieldSlot = i;
            }
        }
        return currentShieldSlot;
    }

    public int getCurrentRingSlot() {
        int currentRingSlot = 0;
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i) == currentRing) {
                currentRingSlot = i;
            }
        }
        return currentRingSlot;
    }

    public int getCurrentAmuletSlot() {
        int currentAmuletSlot = 0;
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i) == currentAmulet) {
                currentAmuletSlot = i;
            }
        }
        return currentAmuletSlot;
    }

}
