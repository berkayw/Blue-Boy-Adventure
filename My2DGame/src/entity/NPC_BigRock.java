package entity;

import main.GamePanel;
import object.OBJ_Door;
import object.OBJ_Door_Iron;
import tile_interactive.IT_MetalPlate;
import tile_interactive.InteractiveTile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class NPC_BigRock extends Entity{


    public static final String npcName = "Big Rock";

    public NPC_BigRock(GamePanel gp)
    {
        super(gp);

        name = npcName;
        direction = "down";
        speed = 4;

        solidArea = new Rectangle();
        solidArea.x = 2;
        solidArea.y = 6;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 44;
        solidArea.height = 40;


        dialogueSet = -1; //For first dialogueSet(= 0)

        getImage();
        setDialogue();
    }
    public void getImage()
    {
        up1 = setup("/npc/bigrock",gp.tileSize,gp.tileSize);
        up2 = setup("/npc/bigrock",gp.tileSize,gp.tileSize);
        down1 = setup("/npc/bigrock",gp.tileSize,gp.tileSize);
        down2 = setup("/npc/bigrock",gp.tileSize,gp.tileSize);
        left1 = setup("/npc/bigrock",gp.tileSize,gp.tileSize);
        left2 = setup("/npc/bigrock",gp.tileSize,gp.tileSize);
        right1 = setup("/npc/bigrock",gp.tileSize,gp.tileSize);
        right2 = setup("/npc/bigrock",gp.tileSize,gp.tileSize);
    }
    public void setDialogue()
    {
        dialogues[0][0] = "It's a giant rock.";
    }
    public void setAction()
    {

    }
    public void update()
    {

    }
    public void speak()
    {
        facePlayer();
        startDialogue(this,dialogueSet);

        dialogueSet++;
        if(dialogues[dialogueSet][0] == null)
        {
            //dialogueSet = 0;
            dialogueSet--; //displays last set
        }
    }
    public void move(String d)
    {
        this.direction = d;

        checkCollision();

        if(collisionOn == false)
        {
            switch(direction)
            {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        detectPlate();
    }
    public void detectPlate()
    {
        ArrayList<InteractiveTile> plateList = new ArrayList<>();
        ArrayList<Entity> rockList = new ArrayList<>();

        //Create a plate list
        for(int i = 0; i < gp.iTile[1].length; i++)
        {
            if(gp.iTile[gp.currentMap][i] != null && gp.iTile[gp.currentMap][i].name != null && gp.iTile[gp.currentMap][i].name.equals(IT_MetalPlate.itName))
            {
                plateList.add(gp.iTile[gp.currentMap][i]);
            }
        }

        //Create a rock list
        for(int i = 0; i < gp.npc[1].length; i++)
        {
            if(gp.npc[gp.currentMap][i] != null && gp.npc[gp.currentMap][i].name.equals(NPC_BigRock.npcName))
            {
                rockList.add(gp.npc[gp.currentMap][i]);
            }
        }

        int count = 0;

        //Scan the plate list
        for(int i = 0; i < plateList.size(); i++)
        {
            int xDistance = Math.abs(worldX - plateList.get(i).worldX);
            int yDistance = Math.abs(worldY - plateList.get(i).worldY);
            int distance = Math.max(xDistance, yDistance);

            if(distance < 8)
            {
                if(linkedEntity == null)
                {
                    linkedEntity = plateList.get(i);
                    gp.playSE(3);
                }
            }
            else
            {
                if(linkedEntity == plateList.get(i)) //checking if linked before then u move rock again somewhere else
                {
                    linkedEntity = null;
                }
            }
        }

        //Scan the rock list
        for(int i = 0; i < rockList.size(); i++)
        {
            //Count the rock on the plate
            if(rockList.get(i).linkedEntity != null)
            {
                count++;
            }
        }

        //If all the rocks are on the plates, the iron door opens
        if(count == rockList.size())
        {
            for(int i = 0; i < gp.obj[1].length; i++)
            {
                if(gp.obj[gp.currentMap][i] != null && gp.obj[gp.currentMap][i].name.equals(OBJ_Door_Iron.objName))
                {
                    gp.obj[gp.currentMap][i] = null;
                    gp.playSE(21);
                }
            }
        }
    }
}
