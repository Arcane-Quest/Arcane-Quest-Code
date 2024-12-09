package object;

import entity.Entity;
import org.example.Gamepanel;

public class OBJ_Mana_Crystal extends Entity {
    Gamepanel gp;
    public static final String objIDName = "Mana Crystal";

    public OBJ_Mana_Crystal(Gamepanel gp) {
        super(gp);

        this.gp = gp;

        type = EntityType.COLLECTABLE;
        stackable = true;
        name = objIDName;
        down1 = setup("/objects/mana crystal", gp.tilesize, gp.tilesize);
    }
    public boolean use(Entity entity) {
        //TODO: play sound
        gp.player.mana += value;
        if (gp.player.mana > gp.player.maxMana) {
            gp.player.mana = gp.player.maxMana;
        }
        return true;
    }
}
