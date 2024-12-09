package object;

import entity.Entity;
import entity.EntitySerializableData;
import entity.Projectile;
import org.example.Gamepanel;

public class OBJ_Rune_Paper extends Entity {
    public static final String objIDName = "Rune paper";

    public int cooldownCounter = 0;
    public int cooldownTime = 2;

    Gamepanel gp;
    String projectilename;
    public OBJ_Rune_Paper(Gamepanel gp) {
        super(gp);
        this.name = objIDName;
        this.gp = gp;
        type = EntityType.SPELL;
        saveableData = new EntitySerializableData();
        down1 = setup("/tiles/debug_texture", gp.tilesize, gp.tilesize); // default
        description = "[" + name + "]\n" + "There was an\nissue loading";
    }

    public void entitySetup(int spellNum) {
        switch (spellNum) {
            case 1:
                down1 = setup("/objects/Rune_paper1", gp.tilesize, gp.tilesize);
                description = "[" + name + "]\n" + "Fire runes";
                projectilename = OBJ_Fireball.objIDName;
                saveableData.saveTrait("projectile", projectilename);
                saveableData.saveTrait("sellable", sellable);
                saveableData.saveTrait("description", description);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
        }
    }

    public boolean loadfromsaveabledata() {
        if(saveableData != null) {
            this.projectilename = (String) saveableData.loadTrait("projectile");
            this.description = (String) saveableData.loadTrait("description");
            this.sellable = (boolean) saveableData.loadTrait("sellable");
        }
        return true;
    }

    public Projectile shoot(Entity user) {
        if(projectilename != null) {
            Projectile spellObject = (Projectile) gp.entityGenerator.getObjectFromString(projectilename);
            if (projectilename != null && spellObject.hasResource(user)) {
                spellObject.set(user.worldX, user.worldY, user.direction, true, user);
                spellObject.completeResourceTransaction(user);

                // add it to the list
                for (int i = 0; i < gp.projectile[1].length; i++) {
                    if (gp.projectile[gp.currentMap][i] == null) {
                        gp.projectile[gp.currentMap][i] = spellObject;
                        break;
                    }
                }
                return spellObject;
            }
        }
        return null;
    }

    public boolean use(Entity entity) {
        for(int i = 0; i < gp.player.spells.length; i++) {
            if(gp.player.spells[i] == null) {
                gp.player.spells[i] = this;
                return true;
            }
        }

        System.out.println("Failed to find empty spell slot");
        return false;
    }
}
