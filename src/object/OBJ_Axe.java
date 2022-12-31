package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity {

    public static final String objName = "Woodcutter's Axe";
    public OBJ_Axe(GamePanel gp) {
        super(gp);

        type = type_axe;
        name = objName;
        down1 = setup("/objects/axe",gp.tileSize,gp.tileSize);
        attackValue = 2;
        attackArea.width = 26;
        attackArea.height= 26;
        description = "[" + name + "]\nA bit rusty but still \ncan cut some trees.";
        price = 75;
        knockBackPower = 5;
        motion1_duration = 20;
        motion2_duration = 40;
    }
}
