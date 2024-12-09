package object;

import org.example.Gamepanel;
import entity.Entity;

public class OBJ_Lantern extends Entity {
    Gamepanel gp;
    public static final String objIDName = "Lantern";

    public OBJ_Lantern(Gamepanel gp) {
        super(gp);
        this.gp = gp;

        type = EntityType.LIGHT;
        name = objIDName;
        down1 = setup("/objects/lantern", gp.tilesize, gp.tilesize);
        description = "[" + name + "]\n" + "Lights your\nsurroundings";
        price = 200;
        sellable = false;
        lightRadius = 350;
    }
}
