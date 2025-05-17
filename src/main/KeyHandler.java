package main;

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
                // Debug escape from combat if needed
                // gp.gameState = gp.playState;
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
                performPlayerAttack();
                break;
            case 1: // Spells
                // To be implemented - open spell selection submenu
                gp.ui.addMessage("Spells not implemented yet!");
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

    // Attack implementation
    private void performPlayerAttack() {
        if (gp.currentCombatMonsterIndex != -1 &&
                gp.monster[gp.currentMap] != null &&
                gp.currentCombatMonsterIndex < gp.monster[gp.currentMap].length &&
                gp.monster[gp.currentMap][gp.currentCombatMonsterIndex] != null) {

            Entity monster = gp.monster[gp.currentMap][gp.currentCombatMonsterIndex];

            // Calculate damage
            int damage = gp.player.attack - monster.defense;
            if (damage < 0) {
                damage = 0;
            }

            // Apply damage to monster
            monster.life -= damage;

            // Show damage message
            gp.ui.addMessage("Player dealt " + damage + " damage!");

            // Check if monster is defeated
            if (monster.life <= 0) {

                // Return to play state
                gp.gameState = gp.playState;

                // Monster defeated
                gp.ui.addMessage("You defeated the " + monster.name + "!");

                // Add exp to player
                gp.player.exp += monster.exp;
                gp.ui.addMessage("You gained " + monster.exp + " exp!");

                // Check level up
                gp.player.checkLevelUp();

                // Set monster to null or respawn it as needed
                gp.monster[gp.currentMap][gp.currentCombatMonsterIndex] = null;
            } else {
                // Monster's turn
                monsterTurn();
            }
        }
    }

    // Defend implementation
    private void playerDefend() {
        // Increase player defense temporarily for this turn
        int originalDefense = gp.player.defense;
        gp.player.defense *= 2; // Double defense while defending
        gp.ui.addMessage("You take defensive stance!");

        // Monster's turn
        monsterTurn();

        // Reset defense after monster turn
        gp.player.defense = originalDefense; // Return to normal defense
    }

    // Escape implementation
    private void attemptEscape() {
        // Random chance to escape (e.g., 50%)
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


    // Trying to fix commit
    // Method to use selected item in combat
    private void useSelectedItem() {
        int itemIndex = gp.ui.getItemIndexOnSlot();

        if (itemIndex < gp.player.inventory.size()) {
            Entity selectedItem = gp.player.inventory.get(itemIndex);

            // Check item type and use accordingly
            if (selectedItem.type == selectedItem.type_consumable) {
                // For healing items (adjust item names according to your game)
                if (selectedItem.name.equals("Hotdog") || selectedItem.name.contains("Hotdog")) {
                    gp.player.life += 2; // Adjust healing amount
                    if (gp.player.life > gp.player.maxLife) {
                        gp.player.life = gp.player.maxLife;
                    }
                    gp.ui.addMessage("Restored 2 HP!");
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