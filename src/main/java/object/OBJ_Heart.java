package object;

import entity.Entity;
import org.example.Gamepanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Heart extends Entity {
    public static final String objIDName = "Heart";

    Gamepanel gp;
    public OBJ_Heart(Gamepanel gp) {
        super(gp);
        this.gp = gp;
        name = objIDName;
        type = EntityType.COLLECTABLE;
        value = 2; // 2:1 heart

        down1 = setup("/objects/heart_full", gp.tilesize, gp.tilesize);;

        image = setup("/objects/heart_full", gp.tilesize, gp.tilesize);
        image2 = setup("/objects/heart_half", gp.tilesize, gp.tilesize);
        image3 = setup("/objects/heart_empty", gp.tilesize, gp.tilesize);
    }

    public boolean use(Entity entity) {
        gp.ui.addMessage("Gained " + (int)value/2 + "  health");
        entity.health += value;
        if(entity.health > entity.maxHealth) {
            entity.health = entity.maxHealth;
        }
        return true;
    }
}
