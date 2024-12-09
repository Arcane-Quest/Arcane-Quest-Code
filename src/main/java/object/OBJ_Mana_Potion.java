package object;

import entity.Entity;
import org.example.Gamepanel;

public class OBJ_Mana_Potion extends Entity {

    Gamepanel gp;
    public static final String objIDName = "Mana Potion";

    public OBJ_Mana_Potion(Gamepanel gp) {
        super(gp);
        this.gp = gp;

        type = Entity.EntityType.CONSUMABLE;
        name = objIDName;
        down1 = setup("/objects/Blue_potion_bottle", gp.tilesize, gp.tilesize);
        description = "[" + name + "]\n" + "replenish your \nmagical abilities\nby " + 6;
        value = 8;
        price = value/2;
        stackable = true;
        setDialogues();
    }

    public void setDialogues() {
        dialogues[0][0] = "You drink the potion...\nand feel a sense of power\nsurge through you.";
    }

    @Override
    public boolean use(Entity target) {

            startDialogue(this, 0);
            target.mana += value;
            if (gp.player.mana > gp.player.maxMana) {
                gp.player.mana = gp.player.maxMana;
            }
            return true;
        }
    }

