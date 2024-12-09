package object;

import entity.Entity;
import org.example.Gamepanel;

import java.util.Objects;

public class OBJ_Alchemy_Table extends Entity {
    Gamepanel gp;
    public static final String objIDName = "Alchemy Table";

    public OBJ_Alchemy_Table(Gamepanel gp) {
        super(gp);
        this.gp = gp;

        name = objIDName;
        collision = true;
        type = EntityType.INTERACTABLE;

        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.tilesize;
        solidArea.height = gp.tilesize;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        down1 = setup("/objects/alchemy table", gp.tilesize, gp.tilesize);
        dialogues[0][0] = "Which potion do you want to\nbrew?";
        dialogues[1][0] = "You don't have enough room to\nbrew that.";
        dialogues[2][0] = "You don't have the ingredients\nto brew that.";
        dialogues[2][1] = "It's not like you have magic.";
    }

    @Override
    public boolean interact(Entity e) {
        gp.ui.setToolTip("Press Enter to interact");
        if(gp.keyH.enterPressed) {
            String[] options = new String[3];
            options[0] = "health potion";
            options[1] = "mana potion";
            options[2] = "back";


            startDialogue(this, 0, options);
        }
        return false;
    }

    public void handleDialogueChosenEvent(String event) {
        System.out.println("DialogueSelectedOption: " + event);
        boolean foundIngredients = false;
        switch (event) {
            case "back": gp.gameState = Gamepanel.GameStates.ACTIVE; dialogueIndex = 0; break;
            case "health potion":
                for (int i = 0; i < gp.player.inventory.size(); i++) {
                    if (Objects.equals(gp.player.inventory.get(i).name, OBJ_satchel.objIDName)) {
                        if(gp.player.inventory.get(i).objectUseKey != null) {
                            if (Objects.equals(gp.player.inventory.get(i).objectUseKey, "healthPotionIngredients")) {
                                    //TODO: add mana potion logic and see if the case is the selected input option
                                    foundIngredients = true;
                                    if(gp.player.tryAddInventory(new OBJ_Health_Potion(gp))) {
                                        tutorialBaralSetDialogue();
                                        if(gp.player.inventory.get(i).amount > 1) {
                                            gp.player.inventory.get(i).amount -= 1;
                                        } else {
                                            gp.player.inventory.remove(i);
                                        }
                                        gp.ui.addMessage("Brewed health potion");
                                        gp.gameState = Gamepanel.GameStates.ACTIVE;
                                    } else {
                                        startDialogue(this, 2);
                                    }
                            }
                            if(foundIngredients) {
                                break;
                            }
                        }
                    }
                }
                if(!foundIngredients) {
                    startDialogue(this, 2);
                }
                dialogueIndex = 0;
                break;
            case "mana potion":
                for (int i = 0; i < gp.player.inventory.size(); i++) {
                    if (Objects.equals(gp.player.inventory.get(i).name, OBJ_satchel.objIDName)) {
                        if(gp.player.inventory.get(i).objectUseKey != null) {
                            if (Objects.equals(gp.player.inventory.get(i).objectUseKey, "manaPotionIngredients")) {
                                //TODO: add mana potion logic and see if the case is the selected input option
                                foundIngredients = true;
                                if(gp.player.tryAddInventory(new OBJ_Mana_Potion(gp))) {
                                    tutorialBaralSetDialogue();
                                    if(gp.player.inventory.get(i).amount > 1) {
                                        gp.player.inventory.get(i).amount -= 1;
                                    } else {
                                        gp.player.inventory.remove(i);
                                    }
                                    gp.ui.addMessage("Brewed mana potion");
                                    gp.gameState = Gamepanel.GameStates.ACTIVE;
                                } else {
                                    startDialogue(this, 2);
                                }
                            }
                            if(foundIngredients) {
                                break;
                            }
                        }
                    }
                }
                if(!foundIngredients) {
                    startDialogue(this, 2);
                }
                dialogueIndex = 0;
                break;
            default:
                System.out.println("error: no dialogue option selected for object: " + this);
                break;


        }
    }

    public void tutorialBaralSetDialogue() {
        if (gp.currentMap != 0) { return; }

        for (Entity npc : gp.npc[gp.currentMap]) {
            if (npc == null || !npc.name.equals("Baral")) { continue; }

            if (npc.dialogueSet < 3) {
                npc.dialogueSignal = 1;
            } else if (npc.dialogueSet < 6 ) {
                npc.dialogueSignal = 2;
            }
        }
    }


//    public void tutorialBaralSetDialogue() {
//        if (gp.currentMap == 0) {
//           for (Entity npc : gp.npc[gp.currentMap]) {
//               if (npc != null) {
//                   if (Objects.equals(npc.name, "Baral")) {
//                       if (npc.dialogueSet < 3) {
//                           npc.dialogueSignal = 1;
//                       } else {
//                           if(npc.dialogueSet < 6) {
//                               npc.dialogueSignal = 2;
//                           }
//                       }
//                   }
//               }
//           }
//        }
//    }
}
