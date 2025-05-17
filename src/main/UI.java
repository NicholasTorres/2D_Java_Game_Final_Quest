package main;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_Key;
//import object.SuperObject;

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

    public UI(GamePanel gp){
        this.gp = gp;

        timesNewRoman_40 = new Font("Arial", Font.PLAIN, 30);
        //OBJ_Key key =new OBJ_Key();
        //keyImage = key.image;

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

        //g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
        //g2.drawString("x " + gp.player.hasKey, 74, 65);
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