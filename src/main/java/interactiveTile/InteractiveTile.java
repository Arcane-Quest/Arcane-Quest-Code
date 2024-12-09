package interactiveTile;

import entity.Entity;
import org.example.Gamepanel;

public class InteractiveTile extends Entity {
    Gamepanel gp;
    public boolean saveLoadable = true;
    public boolean destructible = false;

    public InteractiveTile(Gamepanel gp, int col, int row) {
        super(gp);
    }

    public boolean checkItemRequirement(Entity entity) {
        boolean isCorrectItem = false;
        return isCorrectItem;

    }
    public void update() {

    }
}
