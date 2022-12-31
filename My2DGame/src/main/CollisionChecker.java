package main;

import entity.Entity;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp)
    {
        this.gp = gp;
    }
    public void checkTile(Entity entity)
    {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;                                // solidArea.x = 8
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;      // solidArea.y = 16
        int entityTopWorldY = entity.worldY + entity.solidArea.y;                                 // solidArea.width = 32
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;    // solidArea.height = 32

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;

        int tileNum1 , tileNum2;

        //Use a temporal direction when it's being knockbacked
        String direction = entity.direction;
        if(entity.knockBack == true)
        {
            direction = entity.knockBackDirection;
        }

        switch (direction)
        {
            case "up" :
                entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                tileNum1 =  gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];  //Check Left Hand
                tileNum2 =  gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow]; //Check Right Hand
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)
                {
                    entity.collisionOn = true;
                }
                break;

            case "down" :
                entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
                tileNum1 =  gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];  //Check Left Hand
                tileNum2 =  gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow]; //Check Right Hand
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)
                {
                    entity.collisionOn = true;
                }
                break;

            case "left" :
                entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                tileNum1 =  gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];  //Check Left Hand
                tileNum2 =  gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow]; //Check Right Hand
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)
                {
                    entity.collisionOn = true;
                }
                break;

            case "right" :
                entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                tileNum1 =  gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];  //Check Left Hand
                tileNum2 =  gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow]; //Check Right Hand
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)
                {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player)
    {
        int index = 999;

        //Use a temporal direction when it's being knockbacked
        String direction = entity.direction;
        if(entity.knockBack == true)
        {
            direction = entity.knockBackDirection;
        }

        for(int i = 0;i < gp.obj[1].length; i++)
        {
            if(gp.obj[gp.currentMap][i] != null)
            {
                // get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // get the object's solid area position
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x;       //entity's solid area and obj's solid area is different.
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y;

                switch (direction)
                {
                    case "up" :
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down" :
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left" :
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right" :
                        entity.solidArea.x += entity.speed;
                        break;
                }
                if(entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) //Checking if Entity rectangle and Object rectangle intersects.
                {
                    if(gp.obj[gp.currentMap][i].collision == true) //Collision (Player can't enter through a door.)
                    {
                        entity.collisionOn = true;
                    }
                    if(player == true) // Checking this because no one can receive items except the player.
                    {
                        index = i;   // Non-player characters cannot pickup objects.
                    }
                }
                entity.solidArea.x = entity.solidAreaDefaultX; // Reset
                entity.solidArea.y = entity.solidAreaDefaultY;

                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefaultX;     // Reset
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefaultY;
            }
        }
         return index;
    }

    //NPC OR MONSTER
    public int checkEntity(Entity entity, Entity[][] target)
    {
        int index = 999;   // no collision returns 999;
        //Use a temporal direction when it's being knockbacked
        String direction = entity.direction;
        if(entity.knockBack == true)
        {
            direction = entity.knockBackDirection;
        }

        for(int i = 0;i < target[1].length; i++)
        {
            if(target[gp.currentMap][i] != null)
            {
                // get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // get the object's solid area position
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;       //entity's solid area and obj's solid area is different.
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;

                switch (direction)
                {
                    case "up" :
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down" :
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left" :
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right" :
                        entity.solidArea.x += entity.speed;
                        break;
                }

                if(entity.solidArea.intersects(target[gp.currentMap][i].solidArea))
                {
                    if(target[gp.currentMap][i] != entity) // avoid entity includes itself as a collision target
                    {
                        entity.collisionOn = true;
                        index = i;   // Non-player characters cannot pickup objects.
                    }
                }
                entity.solidArea.x = entity.solidAreaDefaultX; //Reset
                entity.solidArea.y = entity.solidAreaDefaultY;

                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;     //Reset
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }
    public boolean checkPlayer(Entity entity)
    {
        boolean contactPlayer = false;
        // get entity's solid area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        // get the object's solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;       //entity's solid area and obj's solid area is different.
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch (entity.direction)
        {
            case "up" :
                entity.solidArea.y -= entity.speed;
                break;
            case "down" :
                entity.solidArea.y += entity.speed;
                break;
            case "left" :
                entity.solidArea.x -= entity.speed;
                break;
            case "right" :
                entity.solidArea.x += entity.speed;
                break;
        }
        if(entity.solidArea.intersects(gp.player.solidArea))
        {
            entity.collisionOn = true;
            contactPlayer = true;
        }
        entity.solidArea.x = entity.solidAreaDefaultX; ////Reset
        entity.solidArea.y = entity.solidAreaDefaultY;

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;     ////Reset
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;
    }
}
