package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Hotdog extends Entity {

        public OBJ_Hotdog(GamePanel gp){
            super(gp);
            name = "Hotdog";
            down1 = setup("/objects/Hotdog");
            description = "[" + name + "]\nNothing beats a good dog";
        }
}
