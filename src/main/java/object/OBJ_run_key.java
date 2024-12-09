package object;

import entity.Entity;
import org.example.Gamepanel;


public class OBJ_run_key extends Entity {

    public static final String objIDName = "Run Key";

    public OBJ_run_key(Gamepanel gp) {
        super(gp);

        name = objIDName;
        down1 = setup("/objects/boss_key", gp.tilesize, gp.tilesize);
    }
}
