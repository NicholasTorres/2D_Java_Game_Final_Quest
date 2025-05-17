package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

//import static object.SuperObject.scaleImage;

public class OBJ_Staff_Trees extends Entity {


    public OBJ_Staff_Trees(GamePanel gp) {
        super(gp);

        type = type_staff;
        name = "Staff of the Elder Tree";
        try{
            down1 = ImageIO.read(getClass().getResourceAsStream("/objects/Staff_of_Trees.png"));
            down1 = uTool.scaleImage(down1, gp.tileSize, gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
        attackValue = 2;
        description = "[" + name + "]\nOld ass staff lmao";
    }
}
