package org.example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URI;
import java.awt.Desktop;
import java.util.Objects;

// KEYBINDS
// WASD for movement
// Enter and space for object interaction
//TODO:
// Enter for sword
// Space for shield


public class Keyhandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, spellKeyPressed, shieldKeyPressed;
    public int spellSlotNum = -1;
    Gamepanel gp;

    boolean debug = false;

    public Keyhandler(Gamepanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // ignore
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (gp.gameState) {
            case ACTIVE: active(code);       break;
            case PAUSED: paused(code);       break;
            case DIALOGUE: dialogue(code);   break;
            case TITLE: title(code);         break;
            case INVENTORY: inventory(code); break;
            case GAMEOVER: gameOver(code);   break;
            case TRADING: trade(code);       break;
            case OPTIONS: options(code);     break;
        }


    }

    public void options(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 2;
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 2) {
                gp.ui.commandNum = 0;
            }
        }
        if (code == KeyEvent.VK_ENTER) {
            switch (gp.ui.commandNum) {
                case 0:
                    // update
                    System.exit(0);
                    try {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            Desktop.getDesktop().browse(new URI("https://github.com/robot-penguin34/Untitled-mage-game/blob/master/HOWTOUPDATE.md"));
                            System.exit(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            Desktop.getDesktop().browse(new URI("https://github.com/robot-penguin34/Untitled-mage-game?tab=readme-ov-file#Resetting-Progress"));
                            System.exit(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    gp.gameState = Gamepanel.GameStates.TITLE;
                    break;
            }
        }
    }

    public void trade(int code) {
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(gp.ui.subState == 0) {
            if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
            }
            if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }
        } else if (gp.ui.subState == 1) {
            npcInventory(code);
            if(code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_BACK_SPACE) {
                gp.ui.subState = 0;
            }
        } else if (gp.ui.subState == 2) {
            playerInventory(code);
            if(code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_BACK_SPACE) {
                gp.ui.subState = 0;
            }
        }
    }

    private void gameOver(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
        } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
        }
        if (KeyEvent.VK_ENTER == code) {
            if(gp.ui.commandNum == 0) {
                gp.resetGame(false);
                gp.gameState = Gamepanel.GameStates.ACTIVE;
            } else if(gp.ui.commandNum == 1) {
                gp.gameState = Gamepanel.GameStates.TITLE;
                gp.resetGame(false);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            // Forward (North) /\
            upPressed = false;
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            // Backward (South) \/
            downPressed = false;

        }
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            // Left (West) <
            leftPressed = false;

        }
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            // Right (East) >
            rightPressed = false;
        }
        if(code == KeyEvent.VK_SHIFT) {
            shieldKeyPressed = false;
        }
        if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
            enterPressed = false;
        }
    }


    public void active(int code) {
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            // Forward (North) /\
            upPressed = true;
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            // Backward (South) \/
            downPressed = true;

        }
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            // Left (West) <
            leftPressed = true;

        }
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            // Right (East) >
            rightPressed = true;
        }
        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = Gamepanel.GameStates.PAUSED;
        }
        if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
            enterPressed = true;
        }
        if (code == KeyEvent.VK_B) {
            gp.gameState = Gamepanel.GameStates.INVENTORY;
        }
        if (code == KeyEvent.VK_F3) {
            debug = !debug;
        }
        if (code == KeyEvent.VK_SHIFT) {
            shieldKeyPressed = true;
        }


        spellKeyPressed = true;
        switch (code) {
            case KeyEvent.VK_1, KeyEvent.VK_NUMPAD1: spellSlotNum = 1;  break;
            case KeyEvent.VK_2, KeyEvent.VK_NUMPAD2: spellSlotNum = 2;  break;
            case KeyEvent.VK_3, KeyEvent.VK_NUMPAD3: spellSlotNum = 3;  break;
            case KeyEvent.VK_4, KeyEvent.VK_NUMPAD4: spellSlotNum = 4;  break;
            case KeyEvent.VK_5, KeyEvent.VK_NUMPAD5: spellSlotNum = 5;  break;
            case KeyEvent.VK_6, KeyEvent.VK_NUMPAD6: spellSlotNum = 6;  break;
            case KeyEvent.VK_7, KeyEvent.VK_NUMPAD7: spellSlotNum = 7;  break;
            case KeyEvent.VK_8, KeyEvent.VK_NUMPAD8: spellSlotNum = 8;  break;
            case KeyEvent.VK_9, KeyEvent.VK_NUMPAD9: spellSlotNum = 9;  break;
            case KeyEvent.VK_0, KeyEvent.VK_NUMPAD0: spellSlotNum = 0; break;
            default: spellKeyPressed = false; break;
        }

    }
    public void title(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 2;
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 2) {
                gp.ui.commandNum = 0;
            }
        }
        if (code == KeyEvent.VK_ENTER) {
            switch (gp.ui.commandNum) {
                case 0:
                    if(gp.saveDataExists) {
                        gp.saveLoad.load();
                    }
                    gp.gameState = Gamepanel.GameStates.ACTIVE;
                    //TODO: add play music
                    break;
                case 1:
                    gp.gameState = Gamepanel.GameStates.OPTIONS;
                    break;
                case 2:
                    System.exit(0);
                    break;
            }
        }
    }
    public void inventory(int code) {
        if (code == KeyEvent.VK_B || code == KeyEvent.VK_ESCAPE) {
            gp.gameState = Gamepanel.GameStates.ACTIVE;
        }

        if (code == KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
        playerInventory(code);

    }
    public void dialogue(int code) {
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = gp.ui.dialogueOptions.length - 1;
            }
        }
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if (gp.ui.commandNum > gp.ui.dialogueOptions.length - 1) {
                gp.ui.commandNum = 0;
            }
        }

    }
    public void paused(int code) {
        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = Gamepanel.GameStates.ACTIVE;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(code == KeyEvent.VK_Q) {
            gp.gameState = Gamepanel.GameStates.TITLE;
            gp.resetGame(false);
        }
    }

    public void playerInventory(int code) {
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            if(gp.ui.playerSlotRow > 0) {
                gp.ui.playerSlotRow--;
            }
        }
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            if(gp.ui.playerSlotRow < 3) {
                gp.ui.playerSlotRow++;
            }
        }
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            if(gp.ui.playerSlotCol > 0) {
                gp.ui.playerSlotCol--;
            }
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            if(gp.ui.playerSlotCol < 4) {
                gp.ui.playerSlotCol++;
            }
        }
    }

    public void npcInventory(int code) {
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            if(gp.ui.npcSlotRow > 0) {
                gp.ui.npcSlotRow--;
            }
        }
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            if(gp.ui.npcSlotRow < 3) {
                gp.ui.npcSlotRow++;
            }
        }
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            if(gp.ui.npcSlotCol > 0) {
                gp.ui.npcSlotCol--;
            }
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            if(gp.ui.npcSlotCol < 4) {
                gp.ui.npcSlotCol++;
            }
        }
    }
}
