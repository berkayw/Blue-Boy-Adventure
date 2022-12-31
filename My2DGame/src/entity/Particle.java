package entity;

import main.GamePanel;

import java.awt.*;

public class Particle extends Entity{

    Entity generator;
    Color color;
    int size;
    int xd;
    int yd;

    public Particle(GamePanel gp, Entity generator, Color color,int size, int speed, int maxLife, int xd,int yd) {
        super(gp);

        this.generator = generator;
        this.color = color;
        this.size = size;
        this.speed = speed;
        this.maxLife = maxLife;
        this.xd = xd;
        this.yd = yd;

        life = maxLife;
        int offset = (gp.tileSize/2) - size/2; //for center of generator
        worldX = generator.worldX + offset;
        worldY = generator.worldY + offset;
    }

    public void update()
    {
        life--;

        // If particle's life 1/3 of its maxLife or less, it adds 1 to yd, this yd value gets greater every loop. so, particle will go down.
        if(life < maxLife/3)
        {
            yd++;
            size--;
        }

        worldX += xd * speed;
        worldY += yd * speed;

        if(life == 0)
        {
            alive = false;
        }
    }

    public void draw(Graphics2D g2)
    {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.setColor(color);
        g2.fillRect(screenX,screenY,size,size);
    }
}
