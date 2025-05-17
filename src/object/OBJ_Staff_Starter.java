package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

//import static object.SuperObject.scaleImage;

public class OBJ_Staff_Starter extends Entity {

    public OBJ_Staff_Starter(GamePanel gp) {
        super(gp);

        type = type_staff;
        name = "Apprentice Staff";
        try{
            down1 = ImageIO.read(getClass().getResourceAsStream("/objects/Staff_of_Apprentices.png"));
            down1 = uTool.scaleImage(down1, gp.tileSize, gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
        attackValue = 1;
        description = "[" + name + "]\nA staff crudely fashioned \nyears ago";
    }
}
