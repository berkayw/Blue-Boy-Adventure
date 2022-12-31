package environment;

import main.GamePanel;

import java.awt.*;

public class EnvironmentManager {

    GamePanel gp;
    public Lighting lighting;

    public EnvironmentManager(GamePanel gp)
    {
        this.gp = gp;
    }
    public void setup()
    {
        lighting = new Lighting(gp);
    }
    public void update()
    {
        lighting.update();
    }
    public void draw(Graphics2D g2)
    {
        lighting.draw(g2);
    }
}
