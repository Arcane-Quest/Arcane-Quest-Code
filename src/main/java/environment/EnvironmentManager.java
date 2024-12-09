package environment;

import org.example.Gamepanel;

import java.awt.*;

public class EnvironmentManager {
    Gamepanel gp;
    public Lighting lighting;
    public EnvironmentManager(Gamepanel gp) {
        this.gp = gp;
    }
    public void setup() {
        lighting = new Lighting(gp);
    }
    public void update() {
        lighting.update();
    }
    public void draw(Graphics2D g2) {
        lighting.draw(g2);
    }
}
