package entity;

import main.GamePanel;
import main.KeyHandler;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Player extends Entity{

    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;
    public boolean lightUpdated = false;

    public Player(GamePanel gp, KeyHandler keyH)
    {
        super(gp); // calling constructor of super class(from entity class)
        this.keyH=keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2- (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = 8;
        solidAreaDefaultY = 16;

//      attackArea.width = 36;  //For test sword
//      attackArea.height = 36;

        setDefaultValues(); // when u create Player object, initialize with default values
    }

    public void setDefaultValues()
    {
        //Default Starting Positions
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        gp.currentMap = 0;
        gp.currentArea = gp.outside;

        //Blue Gem Start Position, mapNum = 3;
//         worldX = gp.tileSize *25;
//        worldY = gp.tileSize * 9;
//        gp.currentMap = 3;

        defaultSpeed = 4;
        speed = defaultSpeed;
        direction = "down";

        //PLAYER STATUS
        level = 1;
        maxLife = 10;
        life = maxLife;
        maxMana = 8;
        mana = maxMana;
        ammo = 10;
        strength = 1;           // The more strenght he has, the more damage he gives.
        dexterity = 1;          // The more dexterity he has, the less damage he receives.
        exp = 0;
        nextLevelExp = 4;
        coin = 40;
        invincible = false;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        currentLight = null;
        projectile = new OBJ_Fireball(gp);
        //projectile = new OBJ_Rock(gp);
        attack = getAttack();   // The total attack value is decided by strength and weapon
        defense = getDefense(); // The total defense value is decided by dexterity and shield

        getImage();
        getAttackImage();
        getGuardImage();
        setItems();
        //setDialogue();
    }
    public void setDefaultPositions()
    {
        gp.currentMap = 0;
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        direction = "down";
    }
    public void setDialogue()
    {
        dialogues[0][0] = "You are level " + level + " now!\n" + "You feel stronger!";
    }
    public void restoreStatus()
    {
        life = maxLife;
        mana = maxMana;
        speed = defaultSpeed;
        invincible = false;
        transparent = false;
        attacking = false;
        guarding = false;
        knockBack = false;
        lightUpdated = true;
    }

    public void setItems()
    {
        inventory.clear(); //cuz if game restarts inventory must be cleared first
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        /*inventory.add(new OBJ_Potion_Red(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Key(gp));

        inventory.add(new OBJ_Lantern(gp));
        inventory.add(new OBJ_Axe(gp));
        inventory.add(new OBJ_Pickaxe(gp));*/

    }

    public int getAttack()
    {
        attackArea = currentWeapon.attackArea;
        motion1_duration = currentWeapon.motion1_duration;
        motion2_duration = currentWeapon.motion2_duration;
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense()
    {
        return defense = dexterity * currentShield.defenseValue;
    }
    public int getCurrentWeaponSlot()
    {
        int currentWeaponSlot = 0;
        for(int i = 0; i < inventory.size(); i++)
        {
            if(inventory.get(i) == currentWeapon)
            {
                currentWeaponSlot = i;
            }
        }
        return currentWeaponSlot;
    }
    public int getCurrentShieldSlot()
    {
        int currentShieldSlot = 0;
        for(int i = 0; i < inventory.size(); i++)
        {
            if(inventory.get(i) == currentShield)
            {
                currentShieldSlot = i;
            }
        }
        return currentShieldSlot;
    }

    public void getImage()
    {
            up1 = setup("/player/boy_up_1",gp.tileSize,gp.tileSize);
            up2 = setup("/player/boy_up_2",gp.tileSize,gp.tileSize);
            down1 = setup("/player/boy_down_1",gp.tileSize,gp.tileSize);
            down2 = setup("/player/boy_down_2",gp.tileSize,gp.tileSize);
            left1 = setup("/player/boy_left_1",gp.tileSize,gp.tileSize);
            left2 = setup("/player/boy_left_2",gp.tileSize,gp.tileSize);
            right1 = setup("/player/boy_right_1",gp.tileSize,gp.tileSize);
            right2 = setup("/player/boy_right_2",gp.tileSize,gp.tileSize);
    }
    public void getSleepingImage(BufferedImage image)
    {
        up1 = image;
        up2 = image;
        down1 = image;
        down2 = image;
        left1 = image;
        left2 = image;
        right1 = image;
        right2 = image;
    }
    public void getAttackImage()
    {
        if(currentWeapon.type == type_sword)
        {
            attackUp1 = setup("/player/boy_attack_up_1",gp.tileSize, gp.tileSize * 2);         // 16x32 px
            attackUp2 = setup("/player/boy_attack_up_2",gp.tileSize, gp.tileSize * 2);         // 16x32 px
            attackDown1 = setup("/player/boy_attack_down_1",gp.tileSize, gp.tileSize * 2);     // 16x32 px
            attackDown2 = setup("/player/boy_attack_down_2",gp.tileSize, gp.tileSize * 2);     // 16x32 px
            attackLeft1 = setup("/player/boy_attack_left_1",gp.tileSize * 2, gp.tileSize);      // 32x16 px
            attackLeft2 = setup("/player/boy_attack_left_2",gp.tileSize * 2, gp.tileSize);      // 32x16 px
            attackRight1 = setup("/player/boy_attack_right_1",gp.tileSize * 2, gp.tileSize);    // 32x16 px
            attackRight2 = setup("/player/boy_attack_right_2",gp.tileSize * 2, gp.tileSize);    // 32x16 px
        }
        else if(currentWeapon.type == type_axe)
        {
            attackUp1 = setup("/player/boy_axe_up_1",gp.tileSize, gp.tileSize * 2);         // 16x32 px
            attackUp2 = setup("/player/boy_axe_up_2",gp.tileSize, gp.tileSize * 2);         // 16x32 px
            attackDown1 = setup("/player/boy_axe_down_1",gp.tileSize, gp.tileSize * 2);     // 16x32 px
            attackDown2 = setup("/player/boy_axe_down_2",gp.tileSize, gp.tileSize * 2);     // 16x32 px
            attackLeft1 = setup("/player/boy_axe_left_1",gp.tileSize * 2, gp.tileSize);      // 32x16 px
            attackLeft2 = setup("/player/boy_axe_left_2",gp.tileSize * 2, gp.tileSize);      // 32x16 px
            attackRight1 = setup("/player/boy_axe_right_1",gp.tileSize * 2, gp.tileSize);    // 32x16 px
            attackRight2 = setup("/player/boy_axe_right_2",gp.tileSize * 2, gp.tileSize);    // 32x16 px
        }
        else if(currentWeapon.type == type_pickaxe)
        {
            attackUp1 = setup("/player/boy_pick_up_1",gp.tileSize, gp.tileSize * 2);         // 16x32 px
            attackUp2 = setup("/player/boy_pick_up_2",gp.tileSize, gp.tileSize * 2);         // 16x32 px
            attackDown1 = setup("/player/boy_pick_down_1",gp.tileSize, gp.tileSize * 2);     // 16x32 px
            attackDown2 = setup("/player/boy_pick_down_2",gp.tileSize, gp.tileSize * 2);     // 16x32 px
            attackLeft1 = setup("/player/boy_pick_left_1",gp.tileSize * 2, gp.tileSize);      // 32x16 px
            attackLeft2 = setup("/player/boy_pick_left_2",gp.tileSize * 2, gp.tileSize);      // 32x16 px
            attackRight1 = setup("/player/boy_pick_right_1",gp.tileSize * 2, gp.tileSize);    // 32x16 px
            attackRight2 = setup("/player/boy_pick_right_2",gp.tileSize * 2, gp.tileSize);    // 32x16 px
        }

    }
    public void getGuardImage()
    {
        guardUp = setup("/player/boy_guard_up",gp.tileSize,gp.tileSize);
        guardDown = setup("/player/boy_guard_down",gp.tileSize,gp.tileSize);
        guardLeft = setup("/player/boy_guard_left",gp.tileSize,gp.tileSize);
        guardRight = setup("/player/boy_guard_right",gp.tileSize,gp.tileSize);
    }
    public void update() // Runs 60 times every seconds.
    {
        if(knockBack == true)
        {

            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkObject(this,true);
            gp.cChecker.checkEntity(this, gp.npc);
            gp.cChecker.checkEntity(this, gp.monster);
            gp.cChecker.checkEntity(this, gp.iTile);

            if(collisionOn == true)
            {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
            else if(collisionOn == false)
            {
                switch (knockBackDirection)
                {
                    case "up" :
                        worldY -= speed;
                        break;

                    case "down" :
                        worldY += speed;
                        break;

                    case "left" :
                        worldX -= speed;
                        break;

                    case "right" :
                        worldX += speed;
                        break;
                }
            }
            knockBackCounter++;
            if(knockBackCounter == 10)
            {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
        }
        else if(attacking == true) //Need to adjust attack to "up" and "left" cuz player goes back a tile. Need to adjust drawing start point(x,y)
        {
            attacking();
        }
        else if(keyH.spacePressed == true)
        {
            guarding = true;
            guardCounter++;
        }
        else if(keyH.upPressed == true || keyH.downPressed == true ||
                keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true)
        {
            if(keyH.upPressed == true)
            {
                direction = "up";
            }
            else if(keyH.downPressed == true)
            {                                                                 // You can go up and down while you pressing left or right.
                direction = "down";                                           // But if you going up or down you cannot press left or right
            }                                                                 // The reason is here the if statements order.
            else if(keyH.leftPressed == true)                                 // For example when "keyH.upPressed == true", the else if blocks are not working. And you cannot go anyway when you press up.
            {
                direction = "left";
            }
            else if(keyH.rightPressed == true)
            {
                direction = "right";
            }
            //CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this,true);
            pickUpObject(objIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);   // npc array. checks any of npc collision
            interactNPC(npcIndex);

            //CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);   // npc array. checks any of npc collision
            contactMonster(monsterIndex);

            //CHECK INTERACTIVE COLLISION
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);

            //CHECK EVENT
            gp.eHandler.checkEvent();

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if(collisionOn == false && keyH.enterPressed == false)   //Without this, player moves when you press ENTER
            {
                switch (direction)
                {
                    case "up" :
                        worldY -= speed;
                        break;

                    case "down" :
                        worldY += speed;
                        break;

                    case "left" :
                        worldX -= speed;
                        break;

                    case "right" :
                        worldX += speed;
                        break;
                }
            }

            if(keyH.enterPressed == true && attackCanceled == false)
            {
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }

            attackCanceled = false;
            gp.keyH.enterPressed = false;
            guarding = false;
            guardCounter = 0;

                spriteCounter++;
                if (spriteCounter > 12) {
                    if (spriteNum == 1)                  //spriteNum changes every 12 frames
                    {
                        spriteNum = 2;
                    } else if (spriteNum == 2) {
                        spriteNum = 1;
                    }
                    spriteCounter = 0;                  // spriteCounter reset
                }
        }
        else        // This is for: If you release the key when you walking, change sprite num to 1 and use player's not-walking sprite.
        {
            standCounter++;
            if(standCounter == 20)                       // After you release the key player stands 20 frames last position then spriteNum will be 1(default)
            {
                spriteNum = 1;
                standCounter = 0;                        // standCounter reset
            }
            guarding = false;
            guardCounter = 0;

        }

        //PROJECTILE SHOOTING
        if(gp.keyH.shotKeyPressed == true && projectile.alive == false && shotAvailableCounter == 30 && projectile.haveResource(this) == true)   //2nd Condition : You can shoot it only one at a time
        {                                                                                               //3rd Condition : If you close shot monster, projectile.alive will be false. So if you still pressing F key, immediately shoot another fireball.
            // SET DEFAULT COORDINATES, DIRECTION AND USER
            projectile.set(worldX,worldY,direction,true,this);

            // SUBTRACT THE COST(MANA,AMMO ETC.)
            projectile.subtractResource(this);

            // ADD IT TO THE LIST
            //gp.projectileList.add(projectile);

            //CHECK VACANCY
            for(int i = 0; i < gp.projectile[1].length; i++)
            {
                if(gp.projectile[gp.currentMap][i] == null)
                {
                    gp.projectile[gp.currentMap][i] = projectile;
                    break;
                }
            }

            shotAvailableCounter = 0; //reset

            gp.playSE(10);
        }

        //This needs to be outside of key if statement! // If player receive damage from monster, player's gonna be invincible for a second
        if(invincible == true)
        {
            invincibleCounter++;
            if(invincibleCounter > 60)
            {
                invincible = false;
                transparent = false;
                invincibleCounter = 0;
            }
        }

        if(shotAvailableCounter < 30)
        {
            shotAvailableCounter++;
        }
        if(life > maxLife) //for using potion, heal etc.
        {
            life = maxLife;
        }
        if(mana > maxMana) //for using potion, heal etc.
        {
            mana = maxMana;
        }
        if(keyH.godModeOn == false)
        {
            if(life <= 0)
            {
                gp.gameState = gp.gameOverState;
                gp.ui.commandNum =- 1; //for if you die while pressing enter
                gp.stopMusic();
                gp.playSE(12);
            }
        }
    }


    public void pickUpObject(int i)
    {
        if(i != 999)
        {
            // PICKUP ONLY ITEMS
            if(gp.obj[gp.currentMap][i].type == type_pickupOnly)
            {
                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;
            }
            //OBSTACLE
            else if(gp.obj[gp.currentMap][i].type == type_obstacle)
            {
                if(keyH.enterPressed == true)
                {
                    attackCanceled = true;
                    gp.obj[gp.currentMap][i].interact();
                }
            }
            // INVENTORY ITEMS
            else
            {
                String text;
                if(canObtainItem(gp.obj[gp.currentMap][i]) == true) //if inventory is not full can pick up object
                {
                    //inventory.add(gp.obj[gp.currentMap][i]); //canObtainItem() already adds item
                    gp.playSE(1);
                    text = "Got a " + gp.obj[gp.currentMap][i].name + "!";
                }
                else
                {
                    text = "You cannot carry any more";
                }
                gp.ui.addMessage(text);
                gp.obj[gp.currentMap][i] = null;
            }
        }
    }
    public void interactNPC(int i)
    {
        if(i != 999)
        {
            if(gp.keyH.enterPressed == true)
            {
                attackCanceled = true;
                gp.npc[gp.currentMap][i].speak();
            }

            gp.npc[gp.currentMap][i].move(direction);
        }
    }
    public void contactMonster(int i) // CollisionChecker Method Implement //checkPlayer() : Checks who touches to player //checkEntity() : Checks if player touches to an entity;
    {
        if(i != 999)
        {
            if(invincible == false && gp.monster[gp.currentMap][i].dying == false)
            {
                gp.playSE(6);  //receivedamage.wav

                int damage = gp.monster[gp.currentMap][i].attack - defense;
                if(damage < 1)
                {
                    damage = 1;
                }
                life -= damage;
                invincible = true;
                transparent = true;
            }
        }
    }
    public void damageMonster(int i, Entity attacker, int attack, int knockBackPower)
    {
        if(i != 999)
        {
            if(gp.monster[gp.currentMap][i].invincible == false)
            {
                gp.playSE(5);   //hitmonster.wav

                if(knockBackPower > 0)
                {
                    setKnockBack(gp.monster[gp.currentMap][i], attacker, knockBackPower);
                }
                if(gp.monster[gp.currentMap][i].offBalance == true)
                {
                    attack *= 2;
                }
                int damage = attack - gp.monster[gp.currentMap][i].defense;
                if(damage <= 0 )
                {
                    damage = 1;
                }
                gp.monster[gp.currentMap][i].life -= damage;
                gp.ui.addMessage(damage + " damage!");
                gp.monster[gp.currentMap][i].invincible = true;
                gp.monster[gp.currentMap][i].damageReaction();  //run away from player

                if(gp.monster[gp.currentMap][i].life <= 0)
                {
                    gp.monster[gp.currentMap][i].dying = true;
                    gp.ui.addMessage("Killed the " + gp.monster[gp.currentMap][i].name + "!");
                    gp.ui.addMessage("Exp +" + gp.monster[gp.currentMap][i].exp + "!");
                    exp += gp.monster[gp.currentMap][i].exp;
                    checkLevelUp();
                }
            }
        }
    }
    public void damageInteractiveTile(int i)
    {
        if(i != 999 && gp.iTile[gp.currentMap][i].destructible == true && gp.iTile[gp.currentMap][i].isCorrectItem(this) == true && gp.iTile[gp.currentMap][i].invincible == false)
        {
            gp.iTile[gp.currentMap][i].playSE();
            gp.iTile[gp.currentMap][i].life--;
            gp.iTile[gp.currentMap][i].invincible = true;

            //Generate Particle
            generateParticle(gp.iTile[gp.currentMap][i], gp.iTile[gp.currentMap][i]);

            if(gp.iTile[gp.currentMap][i].life == 0)
            {
                //gp.iTile[gp.currentMap][i].checkDrop();
                gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm();
            }
        }
    }
    public void damageProjectile(int i)
    {
        if(i != 999)
        {
            Entity projectile = gp.projectile[gp.currentMap][i];
            projectile.alive = false;
            generateParticle(projectile,projectile);
        }
    }
    public void checkLevelUp()
    {
         while(exp >= nextLevelExp)
         {
             level++;
             exp = exp - nextLevelExp;          //Example: Your exp is 4 and nextLevelExp is 5. You killed a monster and receive 2exp. So, your exp is now 6. Your 1 extra xp will be recovered for the next level.
             if(level <= 4)
             {
                 nextLevelExp = nextLevelExp + 4;   //Level 2 to 6: 4xp- 8xp- 12xp- 16xp- 20xp
             }
             else
             {
                 nextLevelExp = nextLevelExp + 8;  //After Level 6: 28xp- 36xp- 44xp- 52xp- 60xp
             }
             maxLife += 2;
             strength++;
             dexterity++;
             attack = getAttack();
             defense = getDefense();
             gp.playSE(8); //levelup.wav

             dialogues[0][0] = "You are level " + level + " now!\n" + "You feel stronger!";
             setDialogue();
             startDialogue(this,0);
         }
    }
    public void selectItem()
    {
        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);
        if(itemIndex < inventory.size())
        {
            Entity selectedItem = inventory.get(itemIndex);

            if(selectedItem.type == type_sword ||
                    selectedItem.type == type_axe || selectedItem.type == type_pickaxe)
            {
                currentWeapon = selectedItem;
                attack = getAttack();   //update player attack
                getAttackImage(); //update player attack image (sword/axe)
            }
            if(selectedItem.type == type_shield)
            {
                currentShield = selectedItem;
                defense = getDefense(); //update player defense
            }
            if(selectedItem.type == type_light)
            {
                if(currentLight == selectedItem)
                {
                    currentLight = null;
                }
                else
                {
                    currentLight = selectedItem;
                }
                lightUpdated = true;
            }
            if(selectedItem.type == type_consumable)
            {
                if(selectedItem.use(this) == true)
                {
                    if(selectedItem.amount > 1)
                    {
                        selectedItem.amount--;
                    }
                    else
                    {
                        inventory.remove(itemIndex);
                    }
                }
            }

        }
    }
    public int searchItemInInventory(String itemName)
    {
        int itemIndex = 999;
        for(int i = 0; i < inventory.size(); i++)
        {
            if(inventory.get(i).name.equals(itemName))
            {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }
    public boolean canObtainItem(Entity item)
    {
        boolean canObtain = false;

        Entity newItem = gp.eGenerator.getObject(item.name);

        //CHECK IF STACKABLE
        if(newItem.stackable == true)
        {
            int index = searchItemInInventory(newItem.name);

            if(index != 999)
            {
                inventory.get(index).amount++;
                canObtain = true;
            }
            else
            {
                //New item, so need to check vacancy
                if(inventory.size() != maxInventorySize)
                {
                    inventory.add(newItem);
                    canObtain = true;
                }
            }
        }
        //NOT STACKABLE so check vacancy
        else
        {
            if(inventory.size() != maxInventorySize)
            {
                inventory.add(newItem);
                canObtain = true;
            }
        }
        return  canObtain;
    }


    public void draw(Graphics2D g2)
    {
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;


        switch (direction)
        {
            case "up" :
                if(attacking == false) //Normal walking sprites
                {
                    if(spriteNum == 1){image = up1;}
                    if(spriteNum == 2) {image = up2;}
                }
                if(attacking == true)  //Attacking sprites
                {
                    tempScreenY = screenY - gp.tileSize;    //Adjusted the player's position one tile to up. Explained why I did it at where I call attacking() in update().
                    if(spriteNum == 1) {image = attackUp1;}
                    if(spriteNum == 2) {image = attackUp2;}
                }
                if(guarding == true)
                {
                    image = guardUp;
                }
                break;

            case "down" :
                if(attacking == false) //Normal walking sprites
                {
                    if(spriteNum == 1){image = down1;}
                    if(spriteNum == 2){image = down2;}
                }
                if(attacking == true)  //Attacking sprites
                {
                    if(spriteNum == 1){image = attackDown1;}
                    if(spriteNum == 2){image = attackDown2;}
                }
                if(guarding == true)
                {
                    image = guardDown;
                }
                break;

            case "left" :
                if(attacking == false) //Normal walking sprites
                {
                    if(spriteNum == 1) {image = left1;}
                    if(spriteNum == 2) {image = left2;}
                }
                if(attacking == true)  //Attacking sprites
                {
                    tempScreenX = screenX - gp.tileSize;    //Adjusted the player's position one tile left. Explained why I did it at where I call attacking() in update().
                    if(spriteNum == 1) {image = attackLeft1;}
                    if(spriteNum == 2) {image = attackLeft2;}
                }
                if(guarding == true)
                {
                    image = guardLeft;
                }
                break;

            case "right" :
                if(attacking == false) //Normal walking sprites
                {
                    if(spriteNum == 1) {image = right1;}
                    if(spriteNum == 2) {image = right2;}
                }
                if(attacking == true)  //Attacking sprites
                {
                    if(spriteNum == 1) {image = attackRight1;}
                    if(spriteNum == 2) {image = attackRight2;}
                }
                if(guarding == true)
                {
                    image = guardRight;
                }
                break;
        }

        //Make player half-transparent (%40) when invincible
        if(transparent == true)
        {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.4f));
        }
        if(drawing == true) //for boss cutscene making player invisible to move camera.(Cuz camera movement based on player). Only draw the PlayerDummy
        {
            g2.drawImage(image,tempScreenX,tempScreenY, null);
        }


        //Reset graphics opacity / alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));

        //DEBUG
        /*g2.setFont(new Font("Arial",Font.PLAIN, 26));
        g2.setColor(Color.white);
        g2.drawString("Invincible:" + invincibleCounter, 10,400);

        g2.setColor(Color.RED);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height); //PLAYER COLLISION CHECKER.RED RECTANGLE.

        tempScreenX = screenX + solidArea.x;
        tempScreenY = screenY + solidArea.y;
        switch(direction) {
            case "up": tempScreenY = screenY - attackArea.height; break;
            case "down": tempScreenY = screenY + gp.tileSize; break;
            case "left": tempScreenX = screenX - attackArea.width; break;
            case "right": tempScreenX = screenX + gp.tileSize; break;
        }
        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(tempScreenX, tempScreenY, attackArea.width, attackArea.height);*/
    }
}
