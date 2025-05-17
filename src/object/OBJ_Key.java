package object;

import entity.Entity;
import main.GamePanel;

import java.io.IOException;

public class OBJ_Key extends Entity {

    public OBJ_Key(GamePanel gp){

        super(gp);
        name = "Key";
        down1 = setup("/objects/Key");
        description = "[" + name + "]\nOpens a door or chest";
    }
}
