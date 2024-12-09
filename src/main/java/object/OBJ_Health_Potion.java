package object;

import entity.Entity;
import org.example.Gamepanel;

public class OBJ_Health_Potion extends Entity {

    Gamepanel gp;
    public static final String objIDName = "Health Potion";

    public OBJ_Health_Potion(Gamepanel gp) {
        super(gp);
        this.gp = gp;

        type = EntityType.CONSUMABLE;
        name = objIDName;
        down1 = setup("/objects/health_potion", gp.tilesize, gp.tilesize);
        description = "[" + name + "]\n" + "Replenish your\nhealth by " + 6;
        value = 6;
        price = value/2;
        stackable = true;
        setDialogue();
    }

    public void setDialogue() {
        dialogues[0][0] = "YOU ARE FINE!!!!!!";
        dialogues[0][1] = "STOP WASTING POTIONS";

        dialogues[1][0] = "You drink the potion and\nyour wounds heal";
    }

    @Override
    public boolean use(Entity target) {
        boolean use = false;
        if (gp.player.health == maxHealth) {
            startDialogue(this, 0);

        } else {
            startDialogue(this, 1);
            target.health += value;
            if (gp.player.health > gp.player.maxHealth) {
                gp.player.health = gp.player.maxHealth;
            }
            use = true;
        }
        return use;
    }
}
