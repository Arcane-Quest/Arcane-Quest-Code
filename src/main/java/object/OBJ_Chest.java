package object;

import entity.Entity;
import entity.EntitySerializableData;
import org.example.Gamepanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Arrays;

public class OBJ_Chest extends Entity {

    Gamepanel gp;
    public static final String objIDName = "Chest";


    public OBJ_Chest(Gamepanel gp) {
        super(gp);
        this.gp = gp;

        name = objIDName;
        type = EntityType.INTERACTABLE;

        image = setup("/objects/chest", gp.tilesize, gp.tilesize);
        image2 = setup("/objects/chest_open", gp.tilesize, gp.tilesize);

        down1 = image;
        collision = true;
        solidArea.x = 4;
        solidArea.y = 16;
        solidArea.width = 40;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        saveableData = new EntitySerializableData();
        saveableData.saveTrait("opened", false);

        setDialogues();
    }

    public boolean loadfromsaveabledata() {
        opened = (boolean) saveableData.loadTrait("opened");
        return true;
    }

    public void setContents(Entity contents) {
        this.contents = contents;
        setDialogues(); // update to latest value
    }

    public void open() {
        saveableData.saveTrait("opened", true);
        opened = true;
        down1 = image2;
    }
    @Override
    public boolean interact(Entity entity) {
        gp.ui.setToolTip("Press Enter to open");

        if(!opened && contents != null) {
            dialogues[0][0] = "You open the chest and \nfind a " + contents.name + " inside.";
        } else {
            dialogues[0][0] = "there's nothing in here...";
        }

        if (gp.keyH.enterPressed) {
            if (!opened) {
                open();

                if (contents == null) {
                    startDialogue(this,0);
                    return false;
                }

                if (!gp.player.tryAddInventory(contents)) {
                    startDialogue(this, 2);
                } else {
                    gp.ui.addMessage("Obtained " + contents.name);
                    contents = null;
                    down1 = image2; // opened sprite
                    startDialogue(this, 0);
                    opened = true;
                }
            } else {
                startDialogue(this, 1);
            }
        }
        return false;
    }

    public void setDialogues() {
        if(!opened && contents != null) {
            dialogues[0][0] = "You open the chest and \nfind a " + contents.name + " inside.";
        } else {
            dialogues[0][0] = "there's nothing in here...";
        }

        dialogues[1][0] = "You already opened the chest...\n";
        dialogues[1][1] = "*Must act excited*\nWOW NOTHING!!!!";

        dialogues[2][0] = dialogues[0][0];
        dialogues[2][1] = "You can't carry anything else...";
    }
}
