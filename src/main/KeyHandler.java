package main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import entity.Entity;

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
        // Combat state
        else if (gp.gameState == gp.combatState){
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){
                gp.ui.combatCommandNum--;
                if (gp.ui.combatCommandNum < 0) {
                    gp.ui.combatCommandNum = gp.ui.combatOptions.length - 1;
                }
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
                gp.ui.combatCommandNum++;
                if (gp.ui.combatCommandNum >= gp.ui.combatOptions.length) {
                    gp.ui.combatCommandNum = 0;
                }
            }
            if (code == KeyEvent.VK_SPACE){
                executeCombatCommand();
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.combatState;
            }
        }
        // Combat Spell State
        else if (gp.gameState == gp.combatSpellState){
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){
                gp.ui.combatCommandSpellNum--;
                if (gp.ui.combatCommandSpellNum < 0) {
                    gp.ui.combatCommandSpellNum = gp.ui.combatOptions.length - 1;
                }
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
                gp.ui.combatCommandSpellNum++;
                if (gp.ui.combatCommandSpellNum >= gp.ui.combatOptions.length) {
                    gp.ui.combatCommandSpellNum = 0;
                }
            }
            if (code == KeyEvent.VK_SPACE){
                executeCombatSpellCommand();
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.combatState;
            }
        }
        // Inventory in combat state
        else if (gp.gameState == gp.inventoryCombatState) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                if (gp.ui.slotRow != 0) {
                    gp.ui.slotRow--;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                if (gp.ui.slotRow < 3) { // Assuming 4 rows in inventory
                    gp.ui.slotRow++;
                }
            }
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                if (gp.ui.slotCol != 0) {
                    gp.ui.slotCol--;
                }
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                if (gp.ui.slotCol < 4) { // Assuming 5 columns
                    gp.ui.slotCol++;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                useSelectedItem();
            }
            if (code == KeyEvent.VK_ESCAPE) {
                // Return to combat menu
                gp.gameState = gp.combatState;
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

    // Execute combat command based on selected option
    public void executeCombatCommand() {
        switch (gp.ui.combatCommandNum) {
            case 0: // Attack
                gp.gameState = gp.combatAnimationState;
                performPlayerAttack();
                break;
            case 1: // Spells
                gp.gameState = gp.combatSpellState;
                break;
            case 2: // Items
                // Open inventory in combat context
                gp.gameState = gp.inventoryCombatState;
                break;
            case 3: // Defend
                playerDefend();
                break;
            case 4: // Escape
                attemptEscape();
                break;
        }
    }

    // Execute combat spell command based on selected option
    public void executeCombatSpellCommand() {
        switch (gp.ui.combatCommandSpellNum) {
            case 0: // Fire
                gp.gameState = gp.combatAnimationState;
                performPlayerAttack();
                break;
            case 1: // Ice
                gp.gameState = gp.combatAnimationState;
                spellIce();
                break;
            case 2: // Thunder
                gp.gameState = gp.combatAnimationState;
                spellThunder();
                break;
            case 3: // Wind
                gp.gameState = gp.combatAnimationState;
                spellWind();
                break;
            case 4: // Earth
                gp.gameState = gp.combatAnimationState;
                spellEarth();
                break;
        }
    }

    private void spellFire() {

    }

    private void spellIce() {
        gp.ui.addMessage("Not yet");
    }

    private void spellThunder() {
        gp.ui.addMessage("Not yet");
    }

    private void spellWind() {
        gp.ui.addMessage("Not yet");
    }

    private void spellEarth() {
        gp.ui.addMessage("Not yet");
    }

    // Attack implementation
    private void performPlayerAttack() {
        if (gp.currentCombatMonsterIndex != -1 &&
                gp.monster[gp.currentMap] != null &&
                gp.currentCombatMonsterIndex < gp.monster[gp.currentMap].length &&
                gp.monster[gp.currentMap][gp.currentCombatMonsterIndex] != null) {

            Entity monster = gp.monster[gp.currentMap][gp.currentCombatMonsterIndex];

            gp.gameState = gp.combatAnimationState;
            gp.player.animationCounter = 0;

            // Calculate damage
            int damage = gp.player.attack - monster.defense;
            if (damage < 0) {
                damage = 0;
            }

            // Apply damage to monster
            monster.life -= damage;
            gp.ui.addMessage("Player dealt " + damage + " damage!");

            // Check if monster is defeated
            if (monster.life <= 0 && !monster.dying) {
                monster.dying = true;
                monster.dyingCounter = 0; // Start the death animation counter
                gp.ui.addMessage("The " + monster.name + " is defeated!");
            } else {
                // Monster's turn if not defeated
                monsterTurn();
            }
        }
    }

    // Defend implementation
    private void playerDefend() {
        // Increase player defense temporarily for this turn
        int originalDefense = gp.player.defense;
        gp.player.defense += 1; // Plus 1 defense when defending
        gp.ui.addMessage("You take a defensive stance!");

        // Monster's turn
        monsterTurn();

        // Reset defense after monster turn
        gp.player.defense = originalDefense; // Return to normal defense
    }

    // Escape implementation
    private void attemptEscape() {
        // 50% chance to escape
        boolean escaped = new java.util.Random().nextBoolean();

        if (escaped) {
            gp.ui.addMessage("Escaped successfully!");
            gp.gameState = gp.playState;
        } else {
            gp.ui.addMessage("Failed to escape!");
            // Monster's turn
            monsterTurn();
        }
    }

    // Monster turn implementation
    private void monsterTurn() {
        if (gp.currentCombatMonsterIndex != -1 &&
                gp.monster[gp.currentMap] != null &&
                gp.currentCombatMonsterIndex < gp.monster[gp.currentMap].length &&
                gp.monster[gp.currentMap][gp.currentCombatMonsterIndex] != null) {

            Entity monster = gp.monster[gp.currentMap][gp.currentCombatMonsterIndex];

            // Calculate damage
            int damage = monster.attack - gp.player.defense;
            if (damage < 0) {
                damage = 0;
            }

            // Apply damage to player
            gp.player.life -= damage;

            // Show damage message
            gp.ui.addMessage(monster.name + " dealt " + damage + " damage!");

            // Check if player is defeated
            if (gp.player.life <= 0) {
                gp.ui.addMessage("You were defeated!");
                // Reset player life to 1 or implement game over
                gp.player.life = 1;
                gp.gameState = gp.playState;
            }
        }
    }

    // Method to use selected item in combat
    private void useSelectedItem() {
        int itemIndex = gp.ui.getItemIndexOnSlot();

        if (itemIndex < gp.player.inventory.size()) {
            Entity selectedItem = gp.player.inventory.get(itemIndex);

            // Check item type and use accordingly
            if (selectedItem.type == selectedItem.type_consumable) {
                // For healing items (adjust item names according to your game)
                if (selectedItem.name.equals("Hotdog") || selectedItem.name.contains("Hotdog")) {
                    gp.player.life += 5; // Adjust healing amount
                    if (gp.player.life > gp.player.maxLife) {
                        gp.player.life = gp.player.maxLife;
                    }
                    gp.ui.addMessage("Restored 5 HP!");
                    gp.player.inventory.remove(itemIndex);

                    // After using item, monster's turn
                    monsterTurn();

                    // Return to combat state
                    gp.gameState = gp.combatState;
                } else {
                    gp.ui.addMessage("Can't use this item in battle!");
                }
            } else {
                gp.ui.addMessage("You can't use this in battle!");
            }
        }
    }
}