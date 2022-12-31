package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

public class OBJ_Key extends Entity {

    GamePanel gp;
    public static final String objName = "Key";

    public OBJ_Key(GamePanel gp)
    {
        super(gp);
        this.gp = gp;
        type = type_consumable;
        name = objName;
        down1 = setup("/objects/key",gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nIt opens a door.";
        price = 350;
        stackable = true;

        setDialogue();
    }
    public void setDialogue()
    {
        dialogues[0][0] = "You use the " + name + " and open the door.";

        dialogues[1][0] = "What are you doing?";
    }
    public boolean use(Entity entity)
    {
        int objIndex = getDetected(entity, gp.obj, "Door"); //user, target, name
        if(objIndex != 999)
        {
            startDialogue(this,0);
            gp.playSE(3);
            gp.obj[gp.currentMap][objIndex] = null;
            return true;
        }
        else
        {
            startDialogue(this,1);
            return false;
        }
    }
}
