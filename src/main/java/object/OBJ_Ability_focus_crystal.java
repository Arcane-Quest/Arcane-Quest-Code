package object;

import entity.Entity;
import org.example.Gamepanel;

public class OBJ_Ability_focus_crystal extends Entity {
    Gamepanel gp;
    public static final String objIDName = "Arknium Crystal";

    public OBJ_Ability_focus_crystal(Gamepanel gp) {
        super(gp);
        this.gp = gp;

        dialogues[0][0] = "You don't have enough experience\nto level up.";

        price = 300;
        type = EntityType.CONSUMABLE;
        name = objIDName;
        description = "[" + name + "]\n" + "Mysterious ore that\ngrants ability\nincreases with skill";
        down1 = setup("/objects/level_up_crystal", gp.tilesize, gp.tilesize);
        value = 1;
    }
    public boolean use(Entity entity) {
        //TODO: play sound
        boolean result = false;
        result = gp.player.checkLevelUp();
        if(!result) {
            startDialogue(this, 0);
        }

        return result;
    }
}
