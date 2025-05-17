package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

//import static object.SuperObject.scaleImage;

public class OBJ_Key_Entity extends Entity {

    public OBJ_Key_Entity(GamePanel gp) {
        super(gp);

        name = "Key2";
        try{
            down1 = ImageIO.read(getClass().getResourceAsStream("/objects/Key.png"));
            down1 = uTool.scaleImage(down1, gp.tileSize, gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
        description = "[" + name + "]\nOpens a door or chest";
    }
}
