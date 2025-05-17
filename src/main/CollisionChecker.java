package main;

import entity.Entity;

import java.awt.*;

public class CollisionChecker {

    GamePanel gp;
    private Rectangle entityRect = new Rectangle();
    private Rectangle targetRect = new Rectangle();

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;

    }

    public void checkTile(Entity entity) {

        int entityLeftX = entity.x + entity.solidArea.x;
        int entityRightX = entity.x + entity.solidArea.x + entity.solidArea.width;
        int entityTopY = entity.y + entity.solidArea.y;
        int entityBottomY = entity.y + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftX / gp.tileSize;
        int entityRightCol = entityRightX / gp.tileSize;
        int entityTopRow = entityTopY / gp.tileSize;
        int entityBottomRow = entityBottomY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)
                    entity.collisionOn = true;
                break;
            case "down":
                entityBottomRow = (entityBottomY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)
                    entity.collisionOn = true;
                break;
            case "left":
                entityLeftCol = (entityLeftX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)
                    entity.collisionOn = true;
                break;
            case "right":
                entityRightCol = (entityRightX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)
                    entity.collisionOn = true;
                break;
        }

    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        entityRect.x = entity.x + entity.solidArea.x;
        entityRect.y = entity.y + entity.solidArea.y;
        entityRect.width = entity.solidArea.width;
        entityRect.height = entity.solidArea.height;

        for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            if (gp.obj[gp.currentMap][i] != null) {
                targetRect.x = gp.obj[gp.currentMap][i].x + gp.obj[gp.currentMap][i].solidArea.x;
                targetRect.y = gp.obj[gp.currentMap][i].y + gp.obj[gp.currentMap][i].solidArea.y;
                targetRect.width = gp.obj[gp.currentMap][i].solidArea.width;
                targetRect.height = gp.obj[gp.currentMap][i].solidArea.height;

                Rectangle nextEntityRect = new Rectangle(entityRect); // For checking movement

                switch (entity.direction) {
                    case "up":
                        nextEntityRect.y -= entity.speed;
                        break;
                    case "down":
                        nextEntityRect.y += entity.speed;
                        break;
                    case "left":
                        nextEntityRect.x -= entity.speed;
                        break;
                    case "right":
                        nextEntityRect.x += entity.speed;
                        break;
                }

                if (nextEntityRect.intersects(targetRect)) {
                    if (gp.obj[gp.currentMap][i].collision == true) {
                        entity.collisionOn = true;
                    }
                    if (player == true) {
                        index = i;
                    }
                }
            }
        }
        return index;
    }

    // NPC and Player collision
    // It kept checking the entity against itself where it was trying to move
    // SO NOW AFTER 6 HOURS
    // It does not check against itself in the new position relative to the old and then get collision detection
    public int checkEntity(Entity entity, Entity[][] target) {

        int index = 999;

        for (int i = 0; i < target.length; i++) {
            if (target[gp.currentMap][i] != null && target[gp.currentMap][i] != entity) {
                // Get entity's next possible solid area position
                Rectangle entityNextArea = new Rectangle(entity.x + entity.solidAreaDefaultX, entity.y + entity.solidAreaDefaultY, entity.solidArea.width, entity.solidArea.height);
                switch (entity.direction) {
                    case "up":
                        entityNextArea.y -= entity.speed;
                        break;
                    case "down":
                        entityNextArea.y += entity.speed;
                        break;
                    case "left":
                        entityNextArea.x -= entity.speed;
                        break;
                    case "right":
                        entityNextArea.x += entity.speed;
                        break;
                }

                // Get target entity's current solid area position
                Rectangle targetArea = new Rectangle(target[gp.currentMap][i].x + target[gp.currentMap][i].solidAreaDefaultX, target[gp.currentMap][i].y + target[gp.currentMap][i].solidAreaDefaultY, target[gp.currentMap][i].solidArea.width, target[gp.currentMap][i].solidArea.height);

                if (entityNextArea.intersects(targetArea)) {
                    entity.collisionOn = true;
                    index = i;
                }
            }
        }
        return index;
    }

    public boolean checkPlayer(Entity entity) {

        boolean contactPlayer = false;

        // Get entity's solid area position for this check
        Rectangle entityArea = new Rectangle(entity.x + entity.solidAreaDefaultX, entity.y + entity.solidAreaDefaultY, entity.solidArea.width, entity.solidArea.height);

        // Get player's solid area position for this check
        Rectangle playerArea = new Rectangle(gp.player.x + gp.player.solidAreaDefaultX, gp.player.y + gp.player.solidAreaDefaultY, gp.player.solidArea.width, gp.player.solidArea.height);

        // Create a temporary rectangle representing the entity's potential next position
        Rectangle entityNextArea = new Rectangle(entityArea); // Start with the current area

        switch (entity.direction) {
            case "up":
                entityNextArea.y -= entity.speed;
                break;
            case "down":
                entityNextArea.y += entity.speed;
                break;
            case "left":
                entityNextArea.x -= entity.speed;
                break;
            case "right":
                entityNextArea.x += entity.speed;
                break;
        }

        if (entityNextArea.intersects(playerArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }

        return contactPlayer;
    }
}


