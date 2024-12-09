package object;

import entity.Entity;
import org.example.Gamepanel;

public class OBJ_Sign extends Entity {
    Gamepanel gp;
    public static final String objIDName = "Sign";
    int setNum = 0;

    public OBJ_Sign(Gamepanel gp) {
        super(gp);
        this.gp = gp;

        name = objIDName;
        collision = true;
        type = EntityType.INTERACTABLE;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        down1 = setup("/objects/sign", gp.tilesize, gp.tilesize);

        dialogues[0][0] = "Bad magic has corrupted\nthis sign";
        dialogues[0][1] = "No message was specified\nto be written here";
    }

    public void setInformation(String messages[][], int setNum) {
        dialogues = messages;
        this.setNum = setNum;
    }

    public boolean interact(Entity e) {
        gp.ui.setToolTip("Press Enter to read");
        if(gp.keyH.enterPressed) {
            startDialogue(this, setNum);
        }

        return false;
    }
}
