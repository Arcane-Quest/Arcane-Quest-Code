package entity;

import object.*;
import org.example.Gamepanel;

public class NPC_Merchant extends Entity{
    public boolean completedinitialShoppingList = false;

    public NPC_Merchant(Gamepanel gp) {
        super(gp);

        direction = "down";
        speed = 1;
        frameUpdateCounter = 120;

        getImage();
        setDialogue();
        setItems();
        saveableData = new EntitySerializableData();
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
        dialogues[0][0] = "Hello Traveler,\nwelcome to my shop";
        dialogues[0][1] = "Do you wish to trade\nwith me?";

        dialogues[1][0] = "Come back soon...";

        dialogues[2][0] = "*You realize that isnt a great\nidea to sell*";

        dialogues[3][0] = "Uhh... You don't have room for that";

        dialogues[4][0] = "You don't have enough gold.";
        dialogues[4][1] = "Sorry, I don't have to\neat too.";

        dialogues[5][0] = "Shopkeeper:\nHi, welcome to my shop\nhow can I help you";
        dialogues[5][1] = "You:\nI have this list from Baral.";
        dialogues[5][2] = "Shopkeeper:\nGood. Yes, I should have these just\nover here";
        dialogues[5][3] = "*The shopkeeper rummages around*\n\n*You are jealous of his name, it\nsounds like a wrestling title*";
        dialogues[5][4] = "Shopkeeper:\nHere it is, that's 14 gold";
        dialogues[5][5] = "You:\nHe gave me this to pay";
        dialogues[5][6] = "You hand over the gold and receive\nthe ingredients.";
    }

    public void setItems() {
        inventory.add(new OBJ_Health_Potion(gp));
        inventory.add(new OBJ_Mana_Potion(gp));
        inventory.add(new OBJ_Shield(gp));
        inventory.add(new OBJ_Sword(gp));
    }

    @Override
    public void update() {}


    public void speak() {
        if(completedinitialShoppingList) {
            facePlayer();
            gp.gameState = Gamepanel.GameStates.TRADING;
            gp.ui.npc = this;
        } else {
            completedinitialShoppingList = true;
            saveableData.saveTrait("playerCompletedShoppingList", completedinitialShoppingList);
            startDialogue(this, 5);
            OBJ_satchel potionIngredients = new OBJ_satchel(gp);
            potionIngredients.setContents(null, false, "Ingredients", "healthPotionIngredients");
            gp.player.tryAddInventory(potionIngredients);
        }
    }

    public boolean loadfromsaveabledata() {
        if(saveableData.loadTrait("playerCompletedShoppingList") != null) {
            this.completedinitialShoppingList = (Boolean) saveableData.loadTrait("playerCompletedShoppingList");
        }
        return true;
    }

}
