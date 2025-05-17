package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity{

    // We use game panel and key handler
    KeyHandler keyH;
    public boolean attackCanceled = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int inventorySize = 20;


    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);

        type = 0;

        this.gp = gp;
        this.keyH = keyH;

        // The player's hitbox
        solidArea = new Rectangle();
        solidArea.x = 12; // 12
        solidArea.y = 16; // 16
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 34; // 24
        solidArea.height = 34; // 28

        // This is the size of the hitbox when the player attacks
        attackArea.width = 60;
        attackArea.height = 60;

        setDefaultValues();
        getPlayerImage();
        getPlayerATKImage();
        setItems();
    }

    // This is the default spawn of the player character
    public void setDefaultValues(){

        x = 15 * gp.tileSize; // Spawning x
        y = 15 * gp.tileSize; // spawning y
        speed = 10; // How fast a player can move
        direction = "down"; // How the player spawns when booting game up

        // Player stuff
        level = 1; // starting level
        strength = 1; // Damage does
        dexterity = 1; // Damage recieves
        exp = 0; // starting exp
        nextLevelExp = 5; // how much is needed for level 2
        coin = 0; // starting coin
        currentWeapon = new OBJ_Staff_Starter(gp); // starting weapon
        currentArmor = new OBJ_Robe_Starter(gp); // starting armor
        attack = getAttack(); // Strength and weapon
        defense = getDefense(); // Dexterity and armor
        maxLife = 6;
        life = maxLife;
    }

    public void setItems(){
        inventory.add(currentArmor);
        inventory.add(currentWeapon);
    }

    // These set the attack and defense of player
    public int getAttack(){
        return attack = strength * currentWeapon.attackValue;
    }
    public int getDefense(){
        return defense = dexterity * currentArmor.defenseValue;
    }

    // Player images for walking
    public void getPlayerImage(){
        up1 = setup("New_Mage_Back_1");
        up2 = setup("New_Mage_Back_2");
        down1 = setup("New_Mage_Front_1");
        down2 = setup("New_Mage_Front_2");
        left1 = setup("New_Mage_Left_1");
        left2 = setup("New_Mage_Left_2");
        right1 = setup("New_Mage_Right_1");
        right2 = setup("New_Mage_Right_2");
    }

    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();}
        return image;
    }

    // Player images for attacking
    public void getPlayerATKImage(){

        // So essentially the staffs do need to be different types if we do different sprites
        //if(currentWeapon.type == type_staff_DifferentType){ Pics in here}
        try{
            ATKup1 = ImageIO.read(getClass().getResourceAsStream("/player/New_Mage_Back_ATK_1.png"));
            ATKup2 = ImageIO.read(getClass().getResourceAsStream("/player/New_Mage_Back_ATK_2.png"));
            ATKdown1 = ImageIO.read(getClass().getResourceAsStream("/player/New_Mage_Front_ATK_1.png"));
            ATKdown2 = ImageIO.read(getClass().getResourceAsStream("/player/New_Mage_Front_ATK_2.png"));
            ATKleft1 = ImageIO.read(getClass().getResourceAsStream("/player/New_Mage_Left_ATK_1.png"));
            ATKleft2 = ImageIO.read(getClass().getResourceAsStream("/player/New_Mage_Left_ATK_2.png"));
            ATKright1 = ImageIO.read(getClass().getResourceAsStream("/player/New_Mage_Right_ATK_1.png"));
            ATKright2 = ImageIO.read(getClass().getResourceAsStream("/player/New_Mage_Right_ATK_2.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        //if(currentWeapon.type == type_staff_DifferentType){ Pics in here}
    }

    // During updates this makes it so we can actually move and assigns a direction to the key
    // we pressed so we can get the right sprite
    public void update(){

        // Check to see if attacking first and if so do attacking method if not do the rest of the movements
        if(attacking == true){
            attacking();
        }
        else if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed ==true){

            if (keyH.upPressed == true){
                direction = "up";
            }
            else if (keyH.downPressed == true){
                direction = "down";
            }
            else if (keyH.leftPressed == true){
                direction = "left";
            }
            else if (keyH.rightPressed == true){
                direction = "right";
            }

            // Check tile collision, if collision is false player can move.
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // Check object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // Check NPC collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // Check monster collision
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            // Check Event
            gp.eHandler.checkEvent();

            // This is movement
            if (collisionOn == false && keyH.enterPressed == false){
                switch(direction){
                    case "up":
                        y -= speed;
                        break;
                    case "down":
                        y += speed;
                        break;
                    case "left":
                        x -= speed;
                        break;
                    case "right":
                        x += speed;
                        break;
                }
            }
            if (keyH.enterPressed == true && attackCanceled == false){
                attacking = true;
                spriteCounter = 0;
            }
            attackCanceled = false;

            // Makes it so the player sprite changes every 12 frames only when a key is being pressed
            // Basically, the sprite counter is tied to update, which is updating 60 times a second
            // Once the counter reaches 13 frames (Above 12) it will change the spriteNum to 1 or 2
            // Then the counter will reset itself, and we do it again
            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                }
                else if (spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
        if(invincible == true){
            invincibleCounter++;
            if (invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    // Method for player attacking
    public void attacking() {
        spriteCounter++;

        // Displays sprite 1 for 5 frames and sprite 2 for the remainder up to a 25 frames attack cycle
        if(spriteCounter <= 5){
            spriteNum = 1;
        }
        if(spriteCounter > 5 && spriteCounter <= 25){
            spriteNum = 2;

            // Saving current x and y of player rectangle
            int currentX = x;
            int currentY = y;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Now we adjust that rectangle to match the weapon being used
            switch(direction){
                case"up":
                    y -= attackArea.height;
                    break;
                case"down":
                    y += attackArea.height;
                    break;
                case"left":
                    x -= attackArea.width;
                    break;
                case"right":
                    x += attackArea.width;
                    break;
            }

            // Attack area becomes solid area
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Checking monster collision with the updated rectangles
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            // Resetting the variables we changed
            x = currentX;
            y = currentY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if(spriteCounter > 25){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    // Method to pick up objects based on what they are
    public void pickUpObject(int i) {
        if (i != 999) {
            String text = "";
            if (gp.obj[gp.currentMap][i].name == "Key") {
                hasKey++;
                System.out.println("Has Key++");
            }
            if (inventory.size() != inventorySize) {
                if (gp.obj[gp.currentMap][i].name != "Chest" && gp.obj[gp.currentMap][i].name != "Door" && gp.obj[gp.currentMap][i].name != "Key") {
                    inventory.add(gp.obj[gp.currentMap][i]);
                    text = "Got a " + gp.obj[gp.currentMap][i].name + "!";
                }
            }
            else{
                text = "You can't carry any more!";
            }
            gp.ui.addMessage(text);
            if(hasKey >= 1 && gp.obj[gp.currentMap][i].name == "Chest") {
                hasKey--;
                gp.obj[gp.currentMap][i] = null;
            }
            else if (hasKey >= 1 && gp.obj[gp.currentMap][i].name == "Door") {
                hasKey--;
                gp.obj[gp.currentMap][i] = null;
            }
            else if (gp.obj[gp.currentMap][i].name != "Chest" && gp.obj[gp.currentMap][i].name != "Door"){
                gp.obj[gp.currentMap][i] = null;
            }
        }
    }

    // When enter is pressed, talks to an NPC while making sure the attack animation is not played
    public void interactNPC(int i){
        if(gp.keyH.enterPressed == true) {
            if (i != 999) {
                attackCanceled = true;
                gp.gameState = gp.dialogueState;
                gp.npc[gp.currentMap][i].speak();
            }
        }
    }

    // If the player is not invincible, the monster will do damage equal to their attack minus player defense
    // Damage below zero is set to zero and the life is subtracted and invincibility for the player granted
    public void contactMonster(int i) {
        if (i != 999) {
            if (invincible == false && gp.monster[gp.currentMap][i].dying == false) {

                int damage = gp.monster[gp.currentMap][i].attack - defense;
                if (damage < 0){
                    damage = 0;
                }
                life -= damage;
                invincible = true;
            }
        }
    }

    // Checks to the see if monster is invincible, if not it will do damage calcs (simple defense - attack and vice versa)
    // It decreased monster HP and makes them invincible and displays their reaction plus a message of your damage to the mob
    // Then if the monster is slained, it is set to dying and messages appear telling the player what was slain and how much exp given
    // Finally we check for a level up
    public void damageMonster(int i) {
        if (i != 999) {
            if(gp.monster[gp.currentMap][i].invincible == false){

                int damage = attack - gp.monster[gp.currentMap][i].defense;
                if (damage < 0){
                    damage = 0;
                }
                gp.monster[gp.currentMap][i].life -= damage;
                gp.ui.addMessage(damage + " damage!");
                gp.monster[gp.currentMap][i].invincible = true;
                gp.monster[gp.currentMap][i].damageReaction();

                if(gp.monster[gp.currentMap][i].life <= 0){
                    gp.monster[gp.currentMap][i].dying = true;
                    gp.ui.addMessage(gp.monster[gp.currentMap][i].name + " has been slain!");
                    gp.ui.addMessage( gp.monster[gp.currentMap][i].exp + " exp gained!");
                    exp += gp.monster[gp.currentMap][i].exp;
                    checkLevelUp();
                }
            }
        }
    }

    // This checks if the player gained a level
    // If they did this will increase stats, set the attack and defense to what they should be after the re-calc
    // This also makes the next level up two times it was before so there is progression and players don't perma kill slimes forever
    public void checkLevelUp(){
        if(exp >= nextLevelExp){
            level++;
            nextLevelExp = nextLevelExp * 2;
            exp = 0;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();

            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You are now level " + level + "!\n"
                    + "Max life increased!\n" + "Strength up!   Attack up!\n" + "Dexterity up!   Defense up!";
        }
    }

    public void selectItem(){

        int itemIndex = gp.ui.getItemIndexOnSlot();

        if(itemIndex < inventory.size()){
            Entity selectedItem = inventory.get(itemIndex);

            if(selectedItem.type == type_staff){ // || selectedItem.type == type_DifferentStaffType
                currentWeapon = selectedItem;
                attack = getAttack();
                // getPlayerATKImage();
            }
            if(selectedItem.type == type_robe){ // Same with robe if we gunna update players sprites
                currentArmor = selectedItem;
                defense = getDefense();
            }
                /*
                if(selectedItem.type == type_consumable){

                    // later
                }

                 */
        }
    }

    // Draws the graphics
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        int drawX = x;
        int drawY = y;
        int drawWidth = gp.tileSize;
        int drawHeight = gp.tileSize;

        switch(direction){
            case "up":
                if(attacking == false){
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                }
                if (attacking == true){
                    if (spriteNum == 1) {
                        image = ATKup1;
                        drawY = y - gp.tileSize; // Adjust Y to draw above
                        drawHeight = gp.tileSize * 2;
                    }
                    if (spriteNum == 2) {
                        image = ATKup2;
                        drawY = y - gp.tileSize; // Adjust Y to draw above
                        drawHeight = gp.tileSize * 2;
                    }
                }
                break;
            case "down":
                if(attacking == false){
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                }
                if (attacking == true){
                    if (spriteNum == 1) {
                        image = ATKdown1;
                        drawHeight = gp.tileSize * 2;
                    }
                    if (spriteNum == 2) {
                        image = ATKdown2;
                        drawHeight = gp.tileSize * 2;
                    }
                }
                break;
            case "left":
                if(attacking == false){
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                }
                if (attacking == true){
                    if (spriteNum == 1) {
                        image = ATKleft1;
                        drawX = x - gp.tileSize; // Adjust X to draw to the left
                        drawWidth = gp.tileSize * 2;
                    }
                    if (spriteNum == 2) {
                        image = ATKleft2;
                        drawX = x - gp.tileSize; // Adjust X to draw to the left
                        drawWidth = gp.tileSize * 2;
                    }
                }
                break;
            case "right":
                if(attacking == false){
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                }
                if (attacking == true){
                    if (spriteNum == 1) {
                        image = ATKright1;
                        drawWidth = gp.tileSize * 2;
                    }
                    if (spriteNum == 2) {
                        image = ATKright2;
                        drawWidth = gp.tileSize * 2;
                    }
                }
                break;
        }

        if (invincible == true){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        // Use the calculated drawX, drawY, drawWidth, and drawHeight
        g2.drawImage(image, drawX, drawY, drawWidth, drawHeight, null);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}