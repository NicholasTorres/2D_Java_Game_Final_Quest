package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // Play State keys
        if(gp.gameState == gp.playState){
            if(code == KeyEvent.VK_W){
                upPressed = true;
            }
            if(code == KeyEvent.VK_S){
                downPressed = true;
            }
            if(code == KeyEvent.VK_A){
                leftPressed = true;
            }
            if(code == KeyEvent.VK_D){
                rightPressed = true;
            }
            if(code == KeyEvent.VK_P){
                gp.gameState = gp.pauseState;
            }
            if(code == KeyEvent.VK_C){
                gp.gameState = gp.characterState;
            }
            if(code == KeyEvent.VK_ENTER){
                enterPressed = true;
            }
        }

        // Pause State
        else if(gp.gameState == gp.pauseState){
            if(code == KeyEvent.VK_P){
                gp.gameState = gp.playState;
            }
        }

        // Dialogue State
        else if (gp.gameState ==  gp.dialogueState){
            if (code == KeyEvent.VK_ENTER){
                gp.gameState = gp.playState;
            }
        }

        // Character state
        else if (gp.gameState == gp.characterState){
            if (code == KeyEvent.VK_C){
                gp.gameState = gp.playState;
            }
            if (code == KeyEvent.VK_W){
                if(gp.ui.slotRow != 0) {
                    gp.ui.slotRow--;
                }
            }
            if (code == KeyEvent.VK_A) {
                if (gp.ui.slotCol != 0) {
                    gp.ui.slotCol--;
                }
            }
            if (code == KeyEvent.VK_S) {
                if (gp.ui.slotRow != 3) {
                    gp.ui.slotRow++;
                }
            }
            if (code == KeyEvent.VK_D){
                if(gp.ui.slotCol != 4) {
                    gp.ui.slotCol++;
                }
            }
            if(code == KeyEvent.VK_ENTER){
                gp.player.selectItem();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = false;
        }
    }
}
