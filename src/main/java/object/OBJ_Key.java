package object;

import entity.Entity;
import org.example.Gamepanel;


public class OBJ_Key extends Entity {
    Gamepanel gp;
    public static final String objIDName = "Key";

    // Because just "Key" is confusing, it could be a dictionary key or something else
    public OBJ_Key(Gamepanel gp) {
        super(gp);
        this.gp = gp;

        name = objIDName;
        description = "[" + name + "]\n" + "Opens doors, use\nwisely.";
        type = EntityType.CONSUMABLE;
        down1 = setup("/objects/key", gp.tilesize, gp.tilesize);
        sellable = false;
        price = 30;
        stackable = true;

        setDialogues();

    }

    public void setDialogues() {
        dialogues[0][0] = "*Click*\nThe door unlocked.";
        dialogues[0][1] = "WHERE'S MY KEY?!?";

        dialogues[1][0] = "There isn't anything to\nunlock......";
    }

    @Override
    public boolean use(Entity entity) {
        gp.gameState = Gamepanel.GameStates.DIALOGUE;
        int objIndex = getDetected(entity, gp.obj, "Door");
        if(objIndex != 999) {
            startDialogue(this, 0);
            gp.obj[gp.currentMap][objIndex] = null;
            return true;
        } else {
            startDialogue(this, 1);
            return false;
        }
    }

}
