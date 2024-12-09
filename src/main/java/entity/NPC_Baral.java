package entity;

import object.OBJ_Health_Potion;
import object.OBJ_Mana_Potion;
import object.OBJ_Rune_Paper;
import object.OBJ_satchel;
import org.example.Gamepanel;

import java.util.Objects;

public class NPC_Baral extends Entity{
    int nextUpdateSet = -1;
    /*
    This has to be used to update the dialogue set here's why:
    start dialogue sets the gamestate to DIALOGUE, this does not interrupt the update() or any other function.
    because of this the startDialogue only starts after update() and every other function is completed.
    This means if you set the setNum to something after you call the startDialogue, the ui class will be using that rather than
    the value you actually want. This means you need to offload it to the next update.
     */

    public NPC_Baral(Gamepanel gp) {
        super(gp);

        name = "Baral";
        direction = "down";
        speed = 1;
        saveableData = new EntitySerializableData();

        getImage();
        setDialogue();
        dialogueSet = 0;
        saveableData.saveTrait("dialogueSet", dialogueSet);
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
        dialogues[0][0] = "Baral:\nGood to see you up early.\nYou:\nIt's mostly a habit now.";
        dialogues[0][1] = "Baral:\nA good one. Before we continue \nyour training, I need you to fetch\nsome ingredients.";
        dialogues[0][2] = "you: which ones?";
        dialogues[0][3] = "Baral:\nI'll give you a shopping list,\nand some gold.";
        dialogues[0][4] = "Baral:\nIn the forest (just west of here)\nis a small shop.";
        dialogues[0][5] = "Baral:\nThey should have the necessary\nsupplies there.";
        dialogues[0][6] = "Baral:\nThere are some slimes, but\nI trust you can handle yourself?";
        dialogues[0][7] = "you: I should be able to handle it";
        dialogues[0][8] = "Baral:\nBye then.\n\nYou: Bye.";
        dialogues[0][9] = "You obtained a shopping list\n(and some gold)";

        dialogues[1][0] = "Baral:\nJust follow the sign just outside\nmy hut and buy those ingredients.";

        dialogues[2][0] = "Baral:\nGood, today I will teach you\nhow to brew a health potion.";
        dialogues[2][1] = "And if you do well, then I will\nteach you something I think you\nwill really like";
        dialogues[2][2] = "Go over to that brewing stand and\nbrew a health potion.";
        dialogues[2][3] = "quickly now.";

        dialogues[4][0] = "Baral:\nGood, I see you brewed quite a...";
        dialogues[4][1] = "Wait did you just drink that?!?!";
        dialogues[4][2] = "You:\n...";
        dialogues[4][3] = "Baral:\n*looking disconcerted*\nthere must be a powerful\ndark force about.";
        dialogues[4][4] = "Baral:\nVery well, Now I will teach you\nhow to make a mana potion.";
        dialogues[4][5] = "Here are some ingredients. I will\nteach you the use\nafterwards.";

        dialogues[5][0] = "Baral:\nGood, I see you have brewed quite\na fine potion.";
        dialogues[5][1] = "Now, here are some more\ningredients, this time\nfor a \"mana\" potion.";
        dialogues[5][2] = "I'll teach you how to use it\nafterwards.";

        dialogues[7][0] = "Baral:\nSplendid, and a great potion\nyou brewed";
        dialogues[7][1] = "As I promised, I will\nteach you the use of\nthis potion.";
        dialogues[7][2] = "Mana is the measurement\nmagical power.";
        dialogues[7][3] = "I trust you are trained\nin the theory of\nour craft?";
        dialogues[7][4] = "You:\nyes, but I have never\ntried to-";
        dialogues[7][5] = "Baral:\nQuite fine, now read\nthe runes on this\nmanuscript";
        dialogues[7][6] = "[Baral gives you a strange\nlooking paper]";
        dialogues[7][7] = "[Press B to open your inventory]";

        dialogues[8][0] = "Baral:\nSplendid, and a...";
        dialogues[8][1] = "WHERE DID IT GO?!?!";
        dialogues[8][2] = "the dark powers must\nbe stronger then I\nfeared";
        dialogues[8][3] = "You:\n...";
        dialogues[8][4] = "As I promised, I will\nteach you the use of\nthis potion.";
        dialogues[8][5] = "Mana is the measurement\nmagical power.";
        dialogues[8][6] = "I trust you are trained\nin the theory of\nour craft?";
        dialogues[8][7] = "You:\nyes, but I have never\ntried to-";
        dialogues[8][8] = "Baral:\nQuite fine, now read\nthe runes on this\nmanuscript";
        dialogues[8][9] = "[Baral gives you a strange\nlooking paper]";
        dialogues[8][10] = "[Press B to open your inventory]\n[Number keys to cast spells]";

        dialogues[9][0] = "Baral:\nYou don't have enough space\nfor the spell...";
        dialogues[9][1] = "I'll just Use a memory link.";
        dialogues[9][2] = "[Your number keys link to spells]";

        dialogues[10][0] = "Baral:\ndid you try the spell?";

        dialogues[11][0] = "Good to hear.\nYou may go get to know\nthe town";
        dialogues[11][1] = "and come rest when you are ready.";

        dialogues[12][0] = "Well, get on with it.";
        dialogues[12][1] = "[Press B to open your inventory\nand equip the rune paper\nthen cast it using number\nkeys].";

        dialogues[13][0] = "Baral:\nYou are free of today's\nlearning.";
        dialogues[13][1] = "Go get to know the village,\n and return at sundown.";

        dialogues[14][0] = "Are you ready to rest for\nthe night?";

        dialogues[15][0] = "Very well, good night then.";
    }


    public void setAction() {}
    public void update() {}

    public boolean loadfromsaveabledata() {
        if(saveableData != null) {
            this.dialogueSet = (int) saveableData.loadTrait("dialogueSet");
        }
        return true;
    }

    public void speak() {
        if(nextUpdateSet != -1) {
            dialogueSet = nextUpdateSet;
            nextUpdateSet = -1;
        }
        var foundPotion = false;
        var dialogueHaltFlag = false;

        if(dialogueSet < 2) {
            foundPotion = findItemKey("healthPotionIngredients");

            if(foundPotion) {
                dialogueSet = 2;
            }
        }

        facePlayer();

        switch (dialogueSignal) {
            case 1:
                foundPotion = hasPotion(OBJ_Health_Potion.objIDName);
                if(foundPotion) {
                    dialogueSet = 5;
                } else {
                    dialogueSet = 4;
                }
                dialogueSignal = -1;
                break;
            case 2:
                foundPotion = hasPotion(OBJ_Mana_Potion.objIDName);
                if (foundPotion) {
                    dialogueSet = 7;
                    dialogueSignal = -1;
                    break;
                } else {
                    dialogueSet = 8;
                    dialogueSignal = -1;
                    break;
                }
        }

        if(!dialogueHaltFlag) {
            assignDialog();
        } else {
            dialogueHaltFlag = false;
        }
        saveableData.saveTrait("dialogueSet", dialogueSet);
    }

    private void assignDialog() {
        switch (dialogueSet) {
            case -1, 0:
                dialogueSet++;
                startDialogue(this, dialogueSet);
                break;
            case 1, 2:
                startDialogue(this, dialogueSet);
                break;
            case 4, 5:
                forceAddIngredients();
                startDialogue(this, dialogueSet);
                break;
            case 7, 8:
                addFireSpell();
                startDialogue(this, dialogueSet);
                nextUpdateSet = 10;
                break;
            case 10, 14:
                var options = new String[2];
                options[0] = "Yes";
                options[1] = "No";
                startDialogue(this, dialogueSet, options);
                break;
            case 13:
                startDialogue(this, dialogueSet);
                nextUpdateSet = 14;
                break;
        }
    }

    private boolean hasPotion(String potionName) {
        for(Entity item : gp.player.inventory) {
            if(item.name.equals(potionName)) {
                return true;
            }
        }

        return false;
    }

    private boolean findItemKey(String itemName) {
        for(Entity item : gp.player.inventory) {
            if(item.objectUseKey != null && item.objectUseKey.equals(itemName)) {
                return true;
            }
        }

        return false;
    }



    private void addFireSpell() {
        var fireSpell = new OBJ_Rune_Paper(gp);
        fireSpell.entitySetup(1);
        var addedSuccessfully = gp.player.tryAddInventory(fireSpell);
        if(!addedSuccessfully) {
            addToSpells(fireSpell);
            startDialogue(this, 9);
        }
    }

    private void forceAddIngredients() {
        var manaPotionIngredients = new OBJ_satchel(gp);
        manaPotionIngredients.setContents(null, false, "Mana potion\ningredients", "manaPotionIngredients");
        if(!gp.player.tryAddInventory(manaPotionIngredients)) {
            gp.player.inventory.set(gp.player.maxInventorySize, manaPotionIngredients);
        }
    }

    private void addToSpells(OBJ_Rune_Paper fireSpell) {
        for (int i = 0; i < gp.player.spells.length; i++) {
            if(gp.player.spells[i] == null) {
                gp.player.spells[i] = fireSpell;
            }
        }
    }

    public void handleDialogueChosenEvent(String event) {
        if (dialogueSet == 10) {
            if(event.equals("Yes")) {
                startDialogue(this, 11);
                nextUpdateSet = 13;
                saveableData.saveTrait("dialogueSet", dialogueSet);

            } else if (event.equals("No")) {
                startDialogue(this, 12);
                nextUpdateSet = 10;
            }
        } else if (dialogueSet == 14) {
            if(event.equals("Yes")) {
                startDialogue(this, 15);
            } else if (event.equals("No")) {
                startDialogue(this, 13);
            }
        }
    }
}
