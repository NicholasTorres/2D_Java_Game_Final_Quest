package main;

import entity.Entity;
import monster.MON_GreenSlime;
import object.OBJ_Heart;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

// Uncomment all non comments to display key counter
public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font timesNewRoman_40;
    public String currentDialogue = "";
    BufferedImage keyImage, heartFull, heartHalf, heartEmpty;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public int slotCol = 0;
    public int slotRow = 0;
    public int combatOptionRow = 0;
    public final String[] combatOptions = {"Attack", "Spells", "Items", "Defend", "Escape"};
    public boolean combatOptionsVisible = false;
    public static int combatCommandNum = 0;
    public static int combatCommandSpellNum = 0;
    public boolean combatSpellOptionsVisible = false;
    public final String[] combatSpellOptions = {"Fire", "Blizzard", "Thunder", "Wind", "Earth"};
    private int frameCounter = 0;
    private int monsterFrameCounter = 0;

    public UI(GamePanel gp){
        this.gp = gp;

        timesNewRoman_40 = new Font("Arial", Font.PLAIN, 30);

        // Create HUD
        Entity heart = new OBJ_Heart(gp);
        heartFull = heart.image;
        heartHalf = heart.image2;
        heartEmpty = heart.image3;
    }

    public void addMessage(String text){

        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2){

        this.g2 = g2;

        g2.setFont(timesNewRoman_40);
        g2.setColor(Color.WHITE);

        // Play State
        if(gp.gameState == gp.playState){
            // Playstate stuff
            resetCombatState();
            drawPlayerlife();
            drawMessage();
        }
        //Pause State
        if(gp.gameState == gp.pauseState){
            drawPlayerlife();
            drawPauseScreen();
        }
        // Dialogue State
        if(gp.gameState == gp.dialogueState){
            drawPlayerlife();
            drawDialogueScreen();
        }

        // Character State
        if(gp.gameState == gp.characterState){
            drawCharacterScreen();
            drawInventory();
        }

        // Combat State
        if (gp.gameState == gp.combatState){
            drawCombatScreen();
            drawMessage();
        }

        // Combat Ready State has combat menu
        if (gp.gameState == gp.combatReadyState){
            drawCombatScreen();
            drawMessage();
            drawCombatMenu();
        }

        // Combat Inventory
        if (gp.gameState == gp.inventoryCombatState){
            drawCombatScreen();
            drawMessage();
            drawInventory();
        }
        // Combat Animations
        if (gp.gameState == gp.combatAnimationState){
            drawCombatScreen();
            drawMessage();
            if(combatCommandNum == 0 && gp.gameState != gp.combatSpellState) {
                drawBasicAttack();
            }
            if(combatCommandSpellNum == 0 && combatCommandNum != 0){
                drawFire();
            }
            else if (combatCommandSpellNum == 1 && combatCommandNum != 0){
                drawFire();
            }
        }
        // Combat Spell
        if (gp.gameState == gp.combatSpellState){
            drawCombatScreen();
            drawMessage();
            drawSpellMenu();
        }
    }

    // Not implemented yet
    public void drawFire(){
        // Starting position
        int startX = gp.tileSize + (gp.screenWidth / 3);
        int startY = gp.tileSize + (gp.screenHeight / 5 + 40);

        // Target position (end point where projectile should end)
        int targetX = gp.tileSize + (gp.screenWidth / 3) + 300; // 300 pixels to the right of starting position

        int animationSpeed = 5; // Animation frame speed
        int projectileSpeed = 6; // How many pixels the projectile moves each frame

        if (gp.gameState == gp.combatAnimationState) {
            BufferedImage animationSprite = null;

            if (gp.player.animationCounter < 12 * animationSpeed) { // Controls how long the animation runs
                int frameIndex = gp.player.animationCounter / animationSpeed + 1;

                // Set the current animation frame based on frameIndex
                if (frameIndex == 1) {
                    animationSprite = gp.player.fire1;
                } else if (frameIndex == 2) {
                    animationSprite = gp.player.fire2;
                } else if (frameIndex == 3) {
                    animationSprite = gp.player.fire3;
                } else if (frameIndex == 4) {
                    animationSprite = gp.player.fire4;
                } else if (frameIndex == 5) {
                    animationSprite = gp.player.fire5;
                } else if (frameIndex == 6) {
                    animationSprite = gp.player.fire6;
                } else if (frameIndex == 7) {
                    animationSprite = gp.player.fire7;
                } else if (frameIndex == 8) {
                    animationSprite = gp.player.fire8;
                } else if (frameIndex == 9) {
                    animationSprite = gp.player.fire9;
                } else if (frameIndex == 10) {
                    animationSprite = gp.player.fire10;
                } else if (frameIndex == 11) {
                    animationSprite = gp.player.fire11;
                } else if (frameIndex == 12) {
                    animationSprite = gp.player.fire12;
                }

                if (animationSprite != null) {
                    // Calculate current X position based on animation progress
                    // This creates a smooth movement from startX to targetX
                    int currentDistance = (gp.player.animationCounter * projectileSpeed);
                    int currentX = startX + currentDistance;

                    // Draw the projectile at the calculated position
                    g2.drawImage(animationSprite, currentX, startY, gp.tileSize * 2, gp.tileSize * 2, null);
                }

                gp.player.animationCounter++; // Increment the counter
            } else {
                // Animation finished, go back to combat state and trigger monster turn
                gp.gameState = gp.combatState;
                gp.player.animationCounter = 0; // Reset the counter for next time
            }
        }
    }

    public void drawBasicAttack() {
        int animationX = gp.tileSize + (gp.screenWidth / 3) + 200; // This x and y places the animation right on top of the enemy
        int AnimationY = gp.tileSize + (gp.screenHeight / 5 + 40);
        int animationSpeed = 5; // Animation speed wow no way

        if (gp.gameState == gp.combatAnimationState) {
            BufferedImage animationSprite = null;

            if (gp.player.animationCounter < 12 * animationSpeed) { // Controls how long each frame is shown (literally its just 60 FPS)
                int frameIndex = gp.player.animationCounter / animationSpeed + 1; // animation counter starts at 0 so the + 1 is for 0 cases
                if (frameIndex == 1) {
                    animationSprite = gp.player.atk1;
                } else if (frameIndex == 2) {
                    animationSprite = gp.player.atk2;
                } else if (frameIndex == 3) {
                    animationSprite = gp.player.atk3;
                } else if (frameIndex == 4) {
                    animationSprite = gp.player.atk4;
                } else if (frameIndex == 5) {
                    animationSprite = gp.player.atk5;
                } else if (frameIndex == 6) {
                    animationSprite = gp.player.atk6;
                } else if (frameIndex == 7) {
                    animationSprite = gp.player.atk7;
                } else if (frameIndex == 8) {
                    animationSprite = gp.player.atk8;
                } else if (frameIndex == 9) {
                    animationSprite = gp.player.atk9;
                } else if (frameIndex == 10) {
                    animationSprite = gp.player.atk10;
                } else if (frameIndex == 11) {
                    animationSprite = gp.player.atk11;
                } else if (frameIndex == 12) {
                    animationSprite = gp.player.atk12;
                }

                if (animationSprite != null) {
                    g2.drawImage(animationSprite, animationX, AnimationY, gp.tileSize * 2, gp.tileSize * 2, null);
                }

                gp.player.animationCounter++; // Increment the counter
            } else {
                // Animation finished, go back to combat state and trigger monster turn
                gp.gameState = gp.combatState;
                gp.player.animationCounter = 0; // Reset the counter for next time
            }
        }
    }

    public void drawCombatScreen() {
        //Window
        int x = gp.tileSize + (gp.screenWidth / 6);
        int y = gp.tileSize + (gp.screenHeight / 7);
        int width = gp.screenWidth - (gp.tileSize * 15);
        int height = gp.screenHeight - (gp.tileSize * 10);
        drawCombatWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        x += gp.tileSize;
        y += gp.tileSize;

        // Drawing Sprites

        // Player drawing position (adjust as needed)
        int playerX = gp.tileSize + (gp.screenWidth / 3) + 200;
        int playerY = gp.tileSize + (gp.screenHeight / 2);

        // Player Speed Icon
        int playerSpeedIconX = gp.player.playerSpeedIconX;
        int playerSpeedIconY = gp.player.playerSpeedIconY;
        int originalSpeedIconY = gp.player.playerSpeedIconY;
        int MonsterSpeedIconX = gp.player.playerSpeedIconX;
        int monsterSpeedIconY = gp.player.playerSpeedIconY;
        int originalMonsterSpeedIconY = gp.player.playerSpeedIconY;
        int currentY = playerSpeedIconY;
        int monsterCurrentY = playerSpeedIconY;
        int projectileSpeed = 1;

        // Enemy (Slime) drawing position (adjust as needed)
        int slimeX = gp.tileSize + (gp.screenWidth / 3) + 200;
        int slimeY = gp.tileSize + (gp.screenHeight / 5 + 40);

        // Get player sprite
        BufferedImage playerSprite = null;
        BufferedImage speedIconPlayer = null;
        if (gp.gameState == gp.combatState || gp.gameState == gp.inventoryCombatState) {
            if (gp.player.combatSpriteNum == 1) {
                speedIconPlayer = gp.player.speedIcon;
                playerSprite = gp.player.up1;
            } else if (gp.player.combatSpriteNum == 2) {
                speedIconPlayer = gp.player.speedIcon;
                playerSprite = gp.player.up2;
            }
        }

        // Get slime sprite
        if (gp.currentCombatMonsterIndex != -1 &&
                gp.monster[gp.currentMap] != null &&
                gp.currentCombatMonsterIndex < gp.monster[gp.currentMap].length &&
                gp.monster[gp.currentMap][gp.currentCombatMonsterIndex] != null &&
                gp.monster[gp.currentMap][gp.currentCombatMonsterIndex].type == gp.monster[gp.currentMap][gp.currentCombatMonsterIndex].type_monster) {

            MON_GreenSlime slime = (MON_GreenSlime) gp.monster[gp.currentMap][gp.currentCombatMonsterIndex];
            BufferedImage slimeSprite = null;
            BufferedImage speedIconMonster = gp.monster[gp.currentMap][gp.currentCombatMonsterIndex].speedIcon;
            frameCounter++;

            switch (slime.direction) {
                case "up":
                case "right":
                case "left":
                case "down":
                    if (slime.spriteNum == 1) slimeSprite = slime.one;
                    else if (slime.spriteNum == 2) slimeSprite = slime.two;
                    else if (slime.spriteNum == 3) slimeSprite = slime.three;
                    else if (slime.spriteNum == 4) slimeSprite = slime.four;
                    else if (slime.spriteNum == 5) slimeSprite = slime.five;
                    else if (slime.spriteNum == 6) slimeSprite = slime.six;
                    else if (slime.spriteNum == 7) slimeSprite = slime.seven;
                    else if (slime.spriteNum == 8) slimeSprite = slime.eight;
                    else if (slime.spriteNum == 9) slimeSprite = slime.nine;
                    else if (slime.spriteNum == 10) slimeSprite = slime.ten;
                    else if (slime.spriteNum == 11) slimeSprite = slime.eleven;
                    else if (slime.spriteNum == 12) slimeSprite = slime.twelve;
                    break;
            }

            if (gp.player.combatSpriteNum == 1) {
                speedIconPlayer = gp.player.speedIcon;
                playerSprite = gp.player.up1;
            } else if (gp.player.combatSpriteNum == 2) {
                speedIconPlayer = gp.player.speedIcon;
                playerSprite = gp.player.up2;
            }

            // Draw the player sprite
            if (playerSprite != null) {
                g2.drawImage(playerSprite, playerX, playerY, gp.tileSize * 2, gp.tileSize * 2, null);
            }

            // Speed icon logic - FIXED VERSION
            if (speedIconPlayer != null) {
                // If the player hasn't taken their turn yet and animation isn't done, move the icon down
                if (gp.combatStartAnimationDone == false && gp.player.playerTurnNow == false && gp.player.playerTookTurn == false) {
                    int currentDistance = (frameCounter * gp.player.battleSpeed);
                    currentY = playerSpeedIconY + currentDistance;

                    g2.drawImage(speedIconPlayer, gp.player.playerSpeedIconX, currentY, gp.tileSize * 2, gp.tileSize * 2, null);

                    // Check if icon reached the bottom
                    if (currentY >= (gp.tileSize * 13)) {
                        gp.gameState = gp.combatReadyState;
                        gp.combatStartAnimationDone = true;
                        frameCounter = 0;
                        gp.player.playerTurnNow = true;
                    }
                }
                // If player took their turn, reset for next cycle
                else if (gp.player.playerTookTurn == true) {
                    // Reset the animation variables for next turn
                    gp.combatStartAnimationDone = false;
                    gp.player.playerTookTurn = false;
                    gp.player.playerTurnNow = false;
                    frameCounter = 0;

                    // Draw icon back at starting position
                    g2.drawImage(speedIconPlayer, gp.player.playerSpeedIconX, gp.player.playerSpeedIconY, gp.tileSize * 2, gp.tileSize * 2, null);
                }
                // If player's turn is ready (at bottom), keep icon at bottom
                else if (gp.player.playerTurnNow == true) {
                    int finalY = (gp.tileSize * 13);
                    g2.drawImage(speedIconPlayer, gp.player.playerSpeedIconX, finalY, gp.tileSize * 2, gp.tileSize * 2, null);
                }
                // Default case - draw at starting position
                else {
                    g2.drawImage(speedIconPlayer, gp.player.playerSpeedIconX, gp.player.playerSpeedIconY, gp.tileSize * 2, gp.tileSize * 2, null);
                }
            }

            // Draw the slime sprite
            if (slimeSprite != null) {
                g2.drawImage(slimeSprite, slimeX, slimeY, gp.tileSize * 2, gp.tileSize * 2, null);
            }

            // Slime speed icon logic - FIXED VERSION
            if (speedIconMonster != null) {
                // If the monster hasn't taken their turn yet, move the icon down
                if (gp.monster[gp.currentMap][gp.currentCombatMonsterIndex].monsterTurnNow == false &&
                        gp.monster[gp.currentMap][gp.currentCombatMonsterIndex].monsterTookTurn == false) {

                    monsterFrameCounter++; // Independent counter
                    int monsterCurrentDistance = (monsterFrameCounter * projectileSpeed);
                    monsterCurrentY = monsterSpeedIconY + monsterCurrentDistance;

                    g2.drawImage(speedIconMonster, gp.player.playerSpeedIconX + 10, monsterCurrentY, gp.tileSize * 2, gp.tileSize * 2, null);

                    // Check if monster icon reached the bottom (independent of player)
                    if (monsterCurrentY >= (gp.tileSize * 13)) {
                        gp.monster[gp.currentMap][gp.currentCombatMonsterIndex].monsterTurnNow = true;
                        // DON'T reset monsterFrameCounter here - let it keep the position
                    }
                }
                // If monster took their turn, reset for next cycle
                else if (gp.monster[gp.currentMap][gp.currentCombatMonsterIndex].monsterTookTurn == true) {
                    // Reset the monster's animation variables for next turn
                    gp.monster[gp.currentMap][gp.currentCombatMonsterIndex].monsterTookTurn = false;
                    gp.monster[gp.currentMap][gp.currentCombatMonsterIndex].monsterTurnNow = false;
                    monsterFrameCounter = 0;

                    // Draw icon back at starting position
                    g2.drawImage(speedIconMonster, gp.player.playerSpeedIconX + 10, originalMonsterSpeedIconY, gp.tileSize * 2, gp.tileSize * 2, null);
                }
                // If monster's turn is ready (at bottom), keep icon at bottom
                else if (gp.monster[gp.currentMap][gp.currentCombatMonsterIndex].monsterTurnNow == true) {
                    int finalY = (gp.tileSize * 13);
                    g2.drawImage(speedIconMonster, gp.player.playerSpeedIconX + 10, finalY, gp.tileSize * 2, gp.tileSize * 2, null);
                }
                // Default case - draw at starting position
                else {
                    g2.drawImage(speedIconMonster, gp.player.playerSpeedIconX + 10, originalMonsterSpeedIconY, gp.tileSize * 2, gp.tileSize * 2, null);
                }

                // Temporary fix for monster taking its turn for now
                if (gp.monster[gp.currentMap][gp.currentCombatMonsterIndex] != null &&
                        gp.monster[gp.currentMap][gp.currentCombatMonsterIndex].monsterTurnNow == true &&
                        gp.monster[gp.currentMap][gp.currentCombatMonsterIndex].monsterTookTurn == false) {

                    // Monster attack is here (Still going to try to make it work from the monster's code)
                    Entity monster = gp.monster[gp.currentMap][gp.currentCombatMonsterIndex];
                    int damage = monster.attack - gp.player.defense;
                    if (damage < 0) damage = 0;
                    gp.player.life -= damage;
                    monster.monsterTookTurn = true;

                    addMessage("The monster attacked for " + damage + " damage! Player HP is now: " + gp.player.life);
                }
            }
        }

        // Speed Bar
        g2.setColor(new Color(255, 255, 255));
        g2.fillRect(gp.tileSize + 1300, gp.tileSize + (gp.screenWidth / 7), 10, gp.tileSize * 8);

        // Speed bar border
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(gp.tileSize + 1300, gp.tileSize + (gp.screenWidth / 7), 10, gp.tileSize * 8);
    }

    public void resetCombatState() {
        // Reset player combat state
        gp.player.playerTurnNow = false;
        gp.player.playerTookTurn = false;
        gp.combatStartAnimationDone = false;

        // Reset monster combat state
        if (gp.currentCombatMonsterIndex != -1 &&
                gp.monster[gp.currentMap] != null &&
                gp.currentCombatMonsterIndex < gp.monster[gp.currentMap].length &&
                gp.monster[gp.currentMap][gp.currentCombatMonsterIndex] != null) {

            gp.monster[gp.currentMap][gp.currentCombatMonsterIndex].monsterTurnNow = false;
            gp.monster[gp.currentMap][gp.currentCombatMonsterIndex].monsterTookTurn = false;
        }

        // Reset frame counters
        frameCounter = 0;
        monsterFrameCounter = 0;
    }


    public void drawCombatMenu() {
        // Combat options menu frame
        int frameX = gp.tileSize + (gp.screenWidth / 6);
        int frameY = gp.tileSize * 15;
        int frameWidth = gp.tileSize * 18;
        int frameHeight = gp.tileSize * 4;

        drawCombatWindow(frameX,frameY, frameWidth, frameHeight);

        // Draw options
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));

        int textX;
        int textY;

        // Draw combat options in a horizontal row
        for (int i = 0; i < combatOptions.length; i++) {
            textX = frameX + gp.tileSize + (i * (frameWidth / combatOptions.length));
            textY = frameY + gp.tileSize * 2;

            // Draw cursor
            if (combatCommandNum == i) {
                g2.setColor(Color.YELLOW);
                // Draw triangle cursor
                int[] xPoints = {textX - 20, textX - 10, textX - 20};
                int[] yPoints = {textY - 15, textY - 5, textY + 5};
                g2.fillPolygon(xPoints, yPoints, 3);
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.WHITE);
            }

            g2.drawString(combatOptions[i], textX, textY);
        }

        // Description at the bottom
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 24F));
        textX = frameX + 20;
        textY = frameY + frameHeight - 20;

        String description = "";
        switch(combatCommandNum) {
            case 0:
                description = "Basic attack with equipped weapon"; break;
            case 1:
                description = "Cast a spell using MP"; break;
            case 2:
                description = "Use an item from your inventory"; break;
            case 3:
                description = "Take defensive stance to reduce damage"; break;
            case 4:
                description = "Try to escape from battle"; break;
        }

        g2.drawString(description, textX, textY);
    }

    public void drawSpellMenu() {
        // Combat options menu frame
        int frameX = gp.tileSize + (gp.screenWidth / 6);
        int frameY = gp.tileSize * 15;
        int frameWidth = gp.tileSize * 18;
        int frameHeight = gp.tileSize * 4;

        drawCombatWindow(frameX,frameY, frameWidth, frameHeight);

        // Draw options
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));

        int textX;
        int textY;

        // Draw combat options in a horizontal row
        for (int i = 0; i < combatSpellOptions.length; i++) {
            textX = frameX + gp.tileSize + (i * (frameWidth / combatSpellOptions.length));
            textY = frameY + gp.tileSize * 2;

            // Draw cursor
            if (combatCommandSpellNum == i) {
                g2.setColor(Color.YELLOW);
                // Draw triangle cursor
                int[] xPoints = {textX - 20, textX - 10, textX - 20};
                int[] yPoints = {textY - 15, textY - 5, textY + 5};
                g2.fillPolygon(xPoints, yPoints, 3);
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.WHITE);
            }

            g2.drawString(combatSpellOptions[i], textX, textY);
        }

        // Description at the bottom
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 24F));
        g2.setColor(Color.WHITE);
        textX = frameX + 20;
        textY = frameY + frameHeight - 20;

        String description = "";
        switch(combatCommandSpellNum) {
            case 0:
                description = "A small flame: deals weak fire damage"; break;
            case 1:
                description = "A cold marble of ice: deals weak ice damage"; break;
            case 2:
                description = "A tiny jolt of electricity: deals weak lightning damage"; break;
            case 3:
                description = "A soothing breeze: heals 4 HP"; break;
            case 4:
                description = "A collection of rocky plates: defense up by 2"; break;
        }

        g2.drawString(description, textX, textY);
    }

    public void drawPlayerlife(){

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        // Draw Max Life
        while(i < gp.player.maxLife/2){
            g2.drawImage(heartEmpty, x, y, null);
            i++;
            x += (gp.tileSize + 5);
        }

        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        // Draw Current Life
        while(i < gp.player.life){
            g2.drawImage(heartHalf, x, y, null);
            i++;
            if(i < gp.player.life){
                g2.drawImage(heartFull, x, y, null);
            }
            i++;
            x += (gp.tileSize + 5);
        }
    }

    public void drawMessage(){

        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 18;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24F));

        for(int i = 0; i < message.size(); i++){

            if(message.get(i) != null){

                g2.setColor(Color.BLACK);
                g2.drawString(message.get(i), messageX, messageY);
                g2.setColor(Color.WHITE);
                g2.drawString(message.get(i), messageX + 1, messageY + 1);

                int counter = messageCounter.get(i) + 1; // messageCounter++
                messageCounter.set(i, counter); // Set the counter to the array
                messageY += 30;

                if(messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    public void drawPauseScreen(){
        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x ,y);
    }

    public void drawDialogueScreen(){
        //Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 25);
        int height = gp.screenHeight - (gp.tileSize * 18);
        drawSubWindow(x,y,width,height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F));
        x += gp.tileSize;
        y += gp.tileSize;

        for(String line : currentDialogue.split("\n")){
            g2.drawString(line,x,y); // 115, 80
            y += 40;
        }
    }

    public void drawCharacterScreen(){

        // Window
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 6;
        final int frameHeight = gp.tileSize * 12;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Text
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 50;

        // Names
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Coin", textX, textY);
        textY += lineHeight;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight;
        g2.drawString("Armor", textX, textY);

        // Values
        int tailX = (frameX + frameWidth) - 30;
        // Reset textY
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight - 30;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY, null);
        textY += gp.tileSize;
        g2.drawImage(gp.player.currentArmor.down1, tailX - gp.tileSize, textY, null);

    }

    public void drawInventory(){

        // Inventory Frame
        int frameX = gp.tileSize * 9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Slots
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;

        // Draw Players items
        for (int i = 0; i < gp.player.inventory.size(); i++){

            // Equip Cursor
            if(gp.player.inventory.get(i) == gp.player.currentWeapon || gp.player.inventory.get(i) == gp.player.currentArmor){
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }

            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);

            slotX += gp.tileSize;

            if (i == 4 || i == 9 || i == 14){
                slotX = slotXstart;
                slotY += gp.tileSize;
            }
        }

        // Cursor
        int cursorX = slotXstart + (gp.tileSize * slotCol);
        int cursorY = slotYstart + (gp.tileSize * slotRow);;
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        // Drawing cursor
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX,cursorY,cursorWidth,cursorHeight, 10, 10);

        // Description frame
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize * 3;

        // Draw Description text
        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(20F));

        int itemIndex = getItemIndexOnSlot();

        if(itemIndex < gp.player.inventory.size()) {
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);

            for (String line : gp.player.inventory.get(itemIndex).description.split("\n")) {
                g2.drawString(line, textX, textY);
                textY += 32;
            }
        }
    }

    public int getItemIndexOnSlot(){
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
    }

    public void drawSubWindow(int x, int y, int width, int height){

        Color c = new Color(0,0,0,215);
        g2.setColor(c);
        g2.fillRoundRect(x,y,width,height,35,35);

        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5,y+5,width-10,height-10,25,25);
    }

    public void drawCombatWindow(int x, int y, int width, int height){

        Color c = new Color(0,0,0,255);
        g2.setColor(c);
        g2.fillRoundRect(x,y,width,height,35,35);

        c = new Color(255,255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5,y+5,width-10,height-10,25,25);
    }

    public int getXForCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }

    public int getXForAlignToRight(String text, int tailX){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }

}