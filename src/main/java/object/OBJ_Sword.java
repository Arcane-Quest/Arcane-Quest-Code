package object;

import entity.Entity;
import org.example.Gamepanel;

public class OBJ_Sword extends Entity {
    public static final String objIDName = "Sword";

    public OBJ_Sword(Gamepanel gp) {
        super(gp);

        name = objIDName;
        rarityLevel = rarity.NORMAL;
        description = "[" + name + "]\n" + "Certainly not a\nblade of legend";
        down1 = setup("/objects/sword", gp.tilesize, gp.tilesize);
        type = EntityType.WEAPON;
        attackValue = 1;
        attackArea.width = 2*(gp.tilesize/3);
        attackArea.height = gp.tilesize;
        canBreakRock = false;
        knockBackPower = 3;

        price = 15;
    }
}
