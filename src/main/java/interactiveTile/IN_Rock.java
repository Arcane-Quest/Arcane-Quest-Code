package interactiveTile;

import entity.Entity;
import org.example.Gamepanel;

import java.awt.*;

public class IN_Rock extends InteractiveTile {
    public static final String nameIdentifier = "InteractiveRock";
    Gamepanel gp;

    public IN_Rock(Gamepanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;
        this.worldX = gp.tilesize * col;
        this.worldY = gp.tilesize * row;

        down1 = setup("/tiles/rock", gp.tilesize, gp.tilesize);
        destructible = true;
        name = "Rock_interactable";
    }

    public boolean checkItemRequirement(Entity entity) {
        boolean isCorrectItem = false;

        if (entity.currentWeapon.canBreakRock) {
            return true;
        }
        return false;
    }

    public Color getParticleColor() {
        return new Color(154, 154, 166);
    }
    public int getParticleSize() {
        return 6;
    }

    public int getParticleSpeed() {
        return 1;
    }

    public int getParticleLifetime() {
        return 20;
    }
}
