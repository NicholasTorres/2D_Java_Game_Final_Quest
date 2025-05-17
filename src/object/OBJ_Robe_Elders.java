package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

//import static object.SuperObject.scaleImage;

public class OBJ_Robe_Elders extends Entity {

    public OBJ_Robe_Elders(GamePanel gp) {
        super(gp);
        type = type_robe;
        name = "Robe of the Elders";
        try{
            down1 = ImageIO.read(getClass().getResourceAsStream("/objects/Robe_of_the _Elders.png"));
            down1 = uTool.scaleImage(down1, gp.tileSize, gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
        defenseValue = 2;
        description = "[" + name + "]\nOnly worn by the most \nwise and powerful of mages";
    }
}