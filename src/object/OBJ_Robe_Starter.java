package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

//import static object.SuperObject.scaleImage;

public class OBJ_Robe_Starter extends Entity {

    public OBJ_Robe_Starter(GamePanel gp) {
        super(gp);
        type = type_robe;
        name = "Robe of New Light";
        try{
            down1 = ImageIO.read(getClass().getResourceAsStream("/objects/Robe_of_Frail_Light.png"));
            down1 = uTool.scaleImage(down1, gp.tileSize, gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
        defenseValue = 1;
        description = "[" + name + "]\nA robe given to those who \nenroll in the Mage Academy";
    }
}
