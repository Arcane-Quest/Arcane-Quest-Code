package object;
import entity.Entity;
import org.example.Gamepanel;

public class OBJ_Rock_Smasher extends Entity {
    public static final String objIDName = "Rock Smasher";
    public OBJ_Rock_Smasher(Gamepanel gp) {
        super(gp);

        name = objIDName;
        rarityLevel = rarity.NORMAL;
        description = "[" + name + "]\n" + "Break Rocks";
        down1 = setup("/objects/sword", gp.tilesize, gp.tilesize);
        type = EntityType.WEAPON;
        attackValue = 1;
        attackArea.width = 2*(gp.tilesize/3);
        attackArea.height = gp.tilesize;
        canBreakRock = true;
        sellable = false;
        knockBackPower = 12;
    }
}
