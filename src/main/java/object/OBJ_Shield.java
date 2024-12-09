package object;

import entity.Entity;
import org.example.Gamepanel;

public class OBJ_Shield extends Entity {
    public static final String objIDName = "Shield";

    public OBJ_Shield(Gamepanel gp) {
        super(gp);

        name = objIDName;
        rarityLevel = rarity.NORMAL;
        description = "[" + name + "]\n" + "A little better\nthan a pot lid!";
        type = EntityType.SHIELD;
        down1 = setup("/objects/Shield", gp.tilesize, gp.tilesize);
        defenseValue = 1;
        price = 15;
    }
}
