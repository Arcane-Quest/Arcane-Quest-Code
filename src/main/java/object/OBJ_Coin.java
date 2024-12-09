package object;

import entity.Entity;
import org.example.Gamepanel;

public class OBJ_Coin extends Entity {
    Gamepanel gp;
    public static final String objIDName = "Gold Coin";

    public OBJ_Coin(Gamepanel gp) {
        super(gp);
        this.gp = gp;

        type = EntityType.COLLECTABLE;
        name = objIDName;
        down1 = setup("/objects/coin", gp.tilesize, gp.tilesize);
        value = 1;
    }

    public boolean use(Entity entity) {
        //TODO: play sound
        gp.player.coin += value;
        gp.ui.addMessage("+" + value + " gold");
        return true;
    }
}
