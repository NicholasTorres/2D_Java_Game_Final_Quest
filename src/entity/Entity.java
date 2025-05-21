package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    public GamePanel gp;
    public UtilityTool uTool = new UtilityTool();
    public int x, y;
    public int defaultSpeed;
    public BufferedImage image, image2, image3;
    public boolean collision = false;

    // Types
    public int type;// 0 = player, 1 = npc, 2 = monster, 3 weapon
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_staff = 3;
    public final int type_robe = 4;
    public final int type_consumable = 5;

    // Character Stats
    public int speed;
    public int combatSpeed = 0;
    public int originalSpeed;
    public String name = "default";
    public int maxLife;
    public int life;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentArmor;
    public static int hasKey = 0;
    public static int hasHotdog = 0;
    public String description = "";

    // Item stuff
    public int attackValue;
    public int defenseValue;

    // Counters
    public int FrameCounter = 0;
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibleCounter;
    public int hpBarCounter = 0;
    public int dyingCounter = 0;

    // Booleans
    public boolean collisionOn = false;
    boolean attacking = false;
    public boolean choseAttack = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;
    public boolean invincible = false;

    // Images
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, thirteen;
    public BufferedImage ATKup1, ATKup2, ATKdown1, ATKdown2, ATKleft1, ATKleft2, ATKright1, ATKright2, atk1, atk2, atk3, atk4, atk5, atk6, atk7, atk8, atk9, atk10, atk11, atk12;
    public BufferedImage  fire1, fire2, fire3, fire4, fire5, fire6, fire7, fire8, fire9, fire10, fire11, fire12;
    public String direction = "down";
    public int spriteNum = 1;

    // Rectangles
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public int solidAreaDefaultX, solidAreaDefaultY;

    // Dialogue
    String dialogues[] = new String[20];
    int dialogueIndex = 0;

    public Entity(GamePanel gp) {
        this.gp = gp;

        // AI Added TESTING
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void setAction() {
    }

    public void damageReaction(){
    }

    public void update() {

        setAction();
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        //gp.cChecker.checkEntity(this, new Entity[][]{gp.player});
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if(this.type == type_monster && contactPlayer == true){
            if(gp.player.invincible == false){

                int damage = attack - gp.player.defense;
                if (damage < 0){
                    damage = 0;
                }
                gp.player.life -= damage;
                gp.player.invincible = true;
            }
        }


        if (collisionOn == false) {
            switch (direction) {
                case "up": y -= speed; break;
                case "down": y += speed; break;
                case "left": x -= speed; break;
                case "right": x += speed; break;
            }
        }
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if(invincible == true){
            invincibleCounter++;
            if (invincibleCounter > 30){
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void speak() {
    }

    public void draw(Graphics2D g2, int width, int height) { // Modified draw method

        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }

        // Monster HP bar
        if(type == 2 && hpBarOn == true) {

            double oneScale = (double)gp.tileSize/maxLife;
            double hpBarValue = oneScale*life;
            if (hpBarValue < 0){
                hpBarValue = 0;
            }

            g2.setColor(new Color(35, 35, 35));
            g2.fillRect(x - 1, y - 16, gp.tileSize + 2, 12);

            g2.setColor(new Color(255, 0, 30));
            g2.fillRect(x, y - 15, (int)hpBarValue, 10);

            hpBarCounter++;

            if (hpBarCounter > 300){
                hpBarCounter = 0;
                hpBarOn = false;
            }
        }

        if (invincible == true){
            hpBarOn = true;
            hpBarCounter = 0;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        if (dying == true){
            dyingCounter++;
        }

        g2.drawImage(image, x, y, width, height, null);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void draw(Graphics2D g2) {
        draw(g2, gp.tileSize, gp.tileSize); // Call the modified draw method with default tileSize
    }

    public void dyingAnimation(Graphics2D g2){

        dyingCounter++;

        // For death animations switch the change alpha to the desired sprite
        if (dyingCounter <= 5){
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > 5 && dyingCounter <= 10){
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > 10 && dyingCounter <= 15){
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > 15 && dyingCounter <= 20){
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > 20 && dyingCounter <= 25){
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > 25 && dyingCounter <= 30){
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > 30 && dyingCounter <= 35){
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > 35 && dyingCounter <= 40){
            changeAlpha(g2, 1f);
        }
        if(dyingCounter > 40){
            alive = false;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

     public BufferedImage setup(String imageName) {
            UtilityTool uTool = new UtilityTool();
            BufferedImage image = null;
            try{
                image = ImageIO.read(getClass().getResourceAsStream(imageName + ".png"));
                image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
            }catch(IOException e){
                e.printStackTrace();}
            return image;
        }
}


