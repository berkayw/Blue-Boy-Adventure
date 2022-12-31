package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

import java.awt.*;

public class OBJ_Rock extends Projectile {

    GamePanel gp;
    public static final String objName = "Rock";

    public OBJ_Rock(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = objName;
        speed = 5;
        maxLife = 60;   //after 80 frames, projectile disappears
        life = maxLife;
        attack = 2;
        useCost = 1; //spend 1 mana
        alive = false;
        getImage();
        price = 25;
        knockBackPower = 1;
    }
    public void getImage()
    {
        up1 = setup("/projectile/rock_down_1", gp.tileSize,gp.tileSize);
        up2 = setup("/projectile/rock_down_1", gp.tileSize,gp.tileSize);
        down1 = setup("/projectile/rock_down_1", gp.tileSize,gp.tileSize);
        down2 = setup("/projectile/rock_down_1", gp.tileSize,gp.tileSize);
        left1 = setup("/projectile/rock_down_1", gp.tileSize,gp.tileSize);
        left2 = setup("/projectile/rock_down_1", gp.tileSize,gp.tileSize);
        right1 = setup("/projectile/rock_down_1", gp.tileSize,gp.tileSize);
        right2 = setup("/projectile/rock_down_1", gp.tileSize,gp.tileSize);
    }
    public boolean haveResource(Entity user)
    {
        boolean haveResource = false;
        if(user.ammo >= useCost)
        {
            haveResource = true;
        }
        return haveResource;
    }
    public void subtractResource(Entity user)
    {
        user.ammo -= useCost;
    }
    public Color getParticleColor()
    {
        Color color = new Color(40,50,0);
        return color;
    }
    public int getParticleSize()
    {
        int size = 10; //pixels
        return size;
    }
    public int getParticleSpeed()
    {
        int speed = 1;
        return speed;
    }
    public int getParticleMaxLife()
    {
        int maxLife = 20;
        return maxLife;
    }
}
