package object;

import entity.Entity;
import entity.EntitySerializableData;
import org.example.Gamepanel;

public class OBJ_satchel extends Entity {
    Gamepanel gp;
    public Entity contents;
    public static final String objIDName = "Carry Sack";


    public OBJ_satchel(Gamepanel gp) {
        super(gp);
        this.gp = gp;

        type = EntityType.CONSUMABLE;
        name = objIDName;
        down1 = setup("/objects/satchel", gp.tilesize, gp.tilesize);
        description = "[" + name + "]\n" + "Holds items";
        price = 10;
        stackable = true;
        sellable = true;
        saveableData = new EntitySerializableData();
        objectUseKey = "";
        saveableData.saveTrait("objectUseKey", objectUseKey);
        saveableData.saveTrait("carrySackSellable", sellable);
        setDialogue();

    }



    public void setDialogue() {
        dialogues[0][0] = "Better keep this closed for now";
    }

    public void setContents(Entity contents, boolean sellable) {
        this.sellable = sellable;
        this.contents = contents;
        if(contents != null) {
            this.description = "[" + this.name + "]\n" + "Holding:\n" + contents.name;
            saveableData.saveTrait("carrySackContentsName", contents.name);
        } else {
            System.out.println("Error: contents is null for object: " + this);
        }
        saveableData.saveTrait("carrySackSellable", sellable);
        saveableData.saveTrait("carrySackDescription", description);
        saveableData.saveTrait("objectUseKey", objectUseKey);
    }

    public void setContents(Entity contents, boolean sellable,
                            String description, String objectUseKey) {
        this.sellable = sellable;
        this.contents = contents;
        this.description = "[" + this.name + "]\n" + description;
        if(contents != null) {
            saveableData.saveTrait("carrySackContentsName", contents.name);
        }
        saveableData.saveTrait("carrySackDescription", "[" + this.name + "]\n" + description);
        saveableData.saveTrait("carrySackSellable", this.sellable);

        this.objectUseKey = objectUseKey;
        saveableData.saveTrait("objectUseKey", objectUseKey);
    }

    public boolean loadfromsaveabledata() {
        if(saveableData != null) {
            this.contents = (Entity) saveableData.loadTrait("carrySackContentsName");
            this.description = (String) saveableData.loadTrait("carrySackDescription");
            this.objectUseKey = (String) saveableData.loadTrait("objectUseKey");
            this.sellable = (boolean) saveableData.loadTrait("carrySackSellable");
            return true;
        }
        System.out.println("Issue");
        return false;
    }

    @Override
    public boolean use(Entity target) {
        if (contents != null) {
            return contents.use(target);
        }
        startDialogue(this, 0);
        return false;
    }
}
