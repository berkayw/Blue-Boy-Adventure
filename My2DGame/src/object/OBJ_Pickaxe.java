package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Pickaxe extends Entity {
    public static final String objName = "Pickaxe";

    public OBJ_Pickaxe(GamePanel gp)
    {
        super(gp);

        type = type_pickaxe;
        name = objName;
        down1 = setup("/objects/pickaxe",gp.tileSize,gp.tileSize);
        attackValue = 1;
        attackArea.width = 26;
        attackArea.height= 26;
        description = "[" + name + "]\nYou will dig it!";
        price = 75;
        knockBackPower = 1;
        motion1_duration = 10;
        motion2_duration = 20;
    }

}
