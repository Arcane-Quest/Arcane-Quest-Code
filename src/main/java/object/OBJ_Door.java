package object;

import entity.Entity;
import entity.Player;
import org.example.Gamepanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OBJ_Door extends Entity {
    Gamepanel gp;
    public static final String objIDName = "Door";

    public OBJ_Door(Gamepanel gp) {
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

        down1 = setup("/objects/door", gp.tilesize, gp.tilesize);
    }

    @Override
    public boolean interact(Entity e) {
        boolean hasKey = false;
        gp.ui.setToolTip("Press Enter to open");
        if(gp.keyH.enterPressed) {
            for(int i = 0; i < gp.player.inventory.size(); i++) {
                if(gp.player.inventory.get(i).name.equals(OBJ_Key.objIDName)) {
                    hasKey = true;
                    if(gp.player.inventory.get(i).amount > 1) {
                        gp.player.inventory.get(i).amount--;
                    } else {
                        gp.player.inventory.remove(i);
                    }
                    break;
                }
            }
            gp.keyH.enterPressed = false;
            if(!hasKey) {
                dialogues[0][0] = "It's locked...\nBetter find a key\n[press b to open inventory]";
                startDialogue(this, 0);
            } else {
                for (int i = 0; i < gp.obj[1].length; i++) {
                    if(gp.obj[gp.currentMap][i] == this) {
                        gp.obj[gp.currentMap][i] = null;
                    }
                }
            }

        }
        return false;
    }
}
