package org.example;

import entity.Entity;
import object.OBJ_Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {

    Gamepanel gp;
    Graphics2D g2;
    Font pressStart2P;
    Color fontColor;
    public boolean messageShowing = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    BufferedImage heart_full, heart_half, heart_blank;
    int counter = 0;
    public int subState;
    String currentToolTip = "";
    public int removeToolTipDelayCounter = 0;


    // inventory
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotRow = 0;
    public int npcSlotCol = 0;

    public Entity npc;
    int charIndex = 0;
    String combinedText = "";
    public String dialogueOptions[] = new String[5];
    public boolean dialogueWithOptionsFlag = false;

    public UI(Gamepanel gp) {
        this.gp = gp;

        try {
            InputStream is = getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            pressStart2P = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }



        // HUD
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(pressStart2P);
        g2.setColor(Color.WHITE);

        switch (gp.gameState) {
            case ACTIVE: drawPlayerLife(); drawMessage(); drawTooltips();   break;
            case PAUSED: drawPlayerLife(); drawPauseScreen();               break;
            case DIALOGUE:
                drawPlayerLife();
                if(!dialogueWithOptionsFlag) {
                    drawDialogueScreen();
                } else {
                    drawDialogueScreenWithOptions();
                }
                break;
            case TITLE: drawTitleScreen();                                  break;
            case INVENTORY: drawCharacterDetailsScreen(); drawInventory(gp.player, true);  break;
            case GAMEOVER: gameOver();                                      break;
            case TELEPORT: teleport();                                      break;
            case TRADING: drawTradeScreen(); break;
            case OPTIONS: drawOptionsScreen(); break;
        }
    }

    public void setToolTip(String toolTip) {
        removeToolTipDelayCounter = 20;
        currentToolTip = toolTip;
    }

    public void setToolTip(String toolTip, int delay) {
        removeToolTipDelayCounter = delay;
        currentToolTip = toolTip;
    }

    private void drawTooltips() {
        g2.getFont().deriveFont(23f);

        int toolTipX = getXforCenteredText(currentToolTip);
        int toolTipY = 11*gp.tilesize;

        g2.setColor(Color.black);
        g2.drawString(currentToolTip, toolTipX+2, toolTipY+2);

        g2.setColor(Color.white);
        g2.drawString(currentToolTip, toolTipX, toolTipY);
    }

    public void drawOptionsScreen() {
        g2.setFont(g2.getFont().deriveFont( 80F));
        String text = "Options Menu";
        int x = getXforCenteredText(text);
        int y = gp.tilesize * 3;
        g2.setColor(Color.darkGray);
        g2.drawString(text, x+5, y+5); // shadow
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        g2.setColor(Color.white);

        x = gp.screenWidth/2 - (gp.tilesize*2)/2;
        y += gp.tilesize * 2;

        g2.drawImage(gp.player.down1, x, y, gp.tilesize*2, gp.tilesize*2, null);

        // menu


        g2.setFont(g2.getFont().deriveFont(48F));
        text = "Update";
        x = getXforCenteredText(text);
        y += gp.tilesize*3.5;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x-gp.tilesize, y);
        }

        text = "RESET PROGRESS";
        x = getXforCenteredText(text);
        y += gp.tilesize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x-gp.tilesize, y);
        }

        g2.setColor(Color.RED);
        text = "Back";
        x = getXforCenteredText(text);
        y += gp.tilesize;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x-gp.tilesize, y);
        }

    }

    public void drawTradeScreen() {
        switch (subState) {
            case 0: trade_select(); break;
            case 1: trade_buy();    break;
            case 2: trade_sell();   break;
        }
        gp.keyH.enterPressed = false;
    }
    public void trade_select() {
        npc.dialogueSet = 0;
        drawDialogueScreen();

        // dialogue options
        int x = gp.tilesize * 10;
        int y = gp.tilesize * 4;
        int width = gp.tilesize * 5;
        int height = (int)(gp.tilesize * 3.5);
        drawSubWindow(x, y, width, height);

        g2.setColor(Color.WHITE);

        x += gp.tilesize;
        y += gp.tilesize-10;
        g2.drawString("Buy", x, y);
        if(commandNum == 0) {
            g2.drawString(">", x-24, y);
            if(gp.keyH.enterPressed) {
                subState = 1;
                gp.keyH.enterPressed = false;
            }
        }
        y += gp.tilesize-10;
        g2.drawString("Sell", x, y);
        if(commandNum == 1) {
            g2.drawString(">", x-24, y);
            if(gp.keyH.enterPressed) {
                subState = 2;
                gp.keyH.enterPressed = false;
            }
        }
        y += gp.tilesize-10;
        g2.drawString("Leave", x, y);
        if(commandNum == 2) {
            g2.drawString(">", x-24, y);
            if(gp.keyH.enterPressed) {
                commandNum = 0; // reset command num
                npc.startDialogue(npc, 1);
                gp.keyH.enterPressed = false;
            }
        }

    }
    public void trade_buy() {
        drawInventory(gp.player, false);
        drawInventory(npc, true);

        // Escape key hint
        int x = gp.tilesize * 2;
        int y = gp.tilesize * 9;
        int width = gp.tilesize * 6;
        int height = gp.tilesize * 2;
        drawSubWindow(x, y, width, height);
        g2.setColor(Color.WHITE);
        g2.drawString("[ESC] Back", x+24, y+60);

        // draw player gold
        x = gp.tilesize * 9;
        y = gp.tilesize * 9;
        width = gp.tilesize * 6;
        height = gp.tilesize * 2;
        drawSubWindow(x, y, width, height);
        g2.setColor(new Color(234, 190, 46));
        g2.drawString("Gold: " + gp.player.coin, x+24, y+60);

        // draw price window
        int itemIndex = getItemIndexForSlot(npcSlotCol, npcSlotRow);
        if(itemIndex < npc.inventory.size()) {
            x = (int)(gp.tilesize*5.5);
            y = (int)(gp.tilesize*6);
            width = (int)(gp.tilesize*3.5);
            height = (int)(gp.tilesize*1.5);
            drawSubWindow(x, y, width, height);

            int price = npc.inventory.get(itemIndex).price;
            if (price <= gp.player.coin) {
                g2.setColor(Color.GREEN);
            } else {
                g2.setColor(Color.RED);
            }
            g2.drawString(price + " Gold", x+40, y+50);

            if(gp.keyH.enterPressed) {
                if(npc.inventory.get(itemIndex) != null &&
                        npc.inventory.get(itemIndex).price > gp.player.coin) {
                    subState = 0;
                    npc.startDialogue(npc, 4);
                } else if(!gp.player.tryAddInventory(npc.inventory.get(itemIndex))) {
                    subState = 0;
                    npc.startDialogue(npc, 3);
                } else {
                    gp.player.coin -= npc.inventory.get(itemIndex).price;
                }
                gp.keyH.enterPressed = false;
            }
        }
    }
    public void trade_sell() {
        drawInventory(gp.player, true);
        int x = gp.tilesize;
        int y = gp.tilesize * 9;
        int width = gp.tilesize * 6;
        int height = gp.tilesize * 2;

        // Escape key hint

        drawSubWindow(x, y, width, height);
        g2.setColor(Color.WHITE);
        g2.drawString("[ESC] Back", x + 24, y + 60);

        // draw player gold
        x = gp.tilesize * 9;
        y = gp.tilesize * 9;
        width = gp.tilesize * 6;
        height = gp.tilesize * 2;
        drawSubWindow(x, y, width, height);
        g2.setColor(new Color(234, 190, 46));
        g2.drawString("Gold: " + gp.player.coin, x + 24, y + 60);

        // draw price window
        int itemIndex = getItemIndexForSlot(playerSlotCol, playerSlotRow);
        if (itemIndex < gp.player.inventory.size()) {
            x = (int) (gp.tilesize);
            y = (int) (gp.tilesize * 6);
            width = (int) (gp.tilesize * 3.5);
            height = (int) (gp.tilesize * 1.5);
            drawSubWindow(x, y, width, height);

            int price = (gp.player.inventory.get(itemIndex).price -
                    gp.player.inventory.get(itemIndex).price/3);
            g2.setColor(new Color(79, 230, 16));
            g2.drawString("+" + price + " Gold", x + 50, y + 50);

            if (gp.keyH.enterPressed) {
                if(gp.player.inventory.get(itemIndex).sellable && gp.player.inventory.get(itemIndex)
                        != gp.player.currentWeapon && gp.player.inventory.get(itemIndex) != gp.player.currentShield) {
                    gp.player.coin += (gp.player.inventory.get(itemIndex).price -
                            gp.player.inventory.get(itemIndex).price/3);
                    if(gp.player.inventory.get(itemIndex).amount > 1) {
                        gp.player.inventory.get(itemIndex).amount--;
                    } else {
                        gp.player.inventory.remove(itemIndex);
                    }
                } else {
                    commandNum = 0;
                    subState = 0;
                    npc.startDialogue(npc, 2);
                }
                gp.keyH.enterPressed = false;
            }
        }
    }

    public void teleport() {
        counter++;
        g2.setColor(new Color(0, 0, 0, counter*5));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        if (counter*5 >= 220) {
            counter = 0;
            gp.gameState = Gamepanel.GameStates.ACTIVE;
        }
    }

    public void gameOver() {
        g2.setColor(new Color(0, 0, 0, 235));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        int cursorX;
        String text;
        g2.setFont(g2.getFont().deriveFont(70f));
        g2.setColor(Color.WHITE);
        text = "Game Over";
        x = getXforCenteredText("Game Over")+4;
        y = gp.tilesize*4-4;
        g2.drawString(text, x, y);

        g2.setColor(new Color(191, 30, 30));
        x = getXforCenteredText("Game Over");
        y = gp.tilesize*4;
        g2.drawString(text, x, y);

        g2.setFont(g2.getFont().deriveFont(35f));
        text = "Retry";
        x = getXforCenteredText("Retry");
        cursorX = x - gp.tilesize*3;
        y += gp.tilesize*4;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">",cursorX, y);
        }

        text = "Quit to title";
        x = getXforCenteredText("Quit to title");
        y += 55;
        g2.drawString(text, x, y);

        if (commandNum == 1) {
            g2.drawString(">",cursorX, y);
        }
    }

    public void drawInventory(Entity entity, boolean cursorActive) {
        int frameX;
        int frameY;
        int frameWidth;
        int frameHeight;
        int padding;
        int slotCol;
        int slotRow;

        if(entity == gp.player) {
            frameX = gp.tilesize*9;
            frameY = gp.tilesize;
            frameWidth = gp.tilesize*6;
            frameHeight = gp.tilesize*5;
            padding = 20;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        } else {
            frameX = gp.tilesize*2;
            frameY = gp.tilesize;
            frameWidth = gp.tilesize*6;
            frameHeight = gp.tilesize*5;
            padding = 20;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }

        // window


        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // slots

        final int slotXstart = frameX + padding;
        final int slotYstart = frameY + padding;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tilesize+3;

        // load player's inventory
        for (int i = 0; i < entity.inventory.size(); i++) {
            if (entity.inventory.get(i) == entity.currentWeapon ||
                    entity.inventory.get(i) == entity.currentShield ||
                    entity.inventory.get(i) == entity.currentRing ||
                    entity.inventory.get(i) == entity.currentAmulet) {
                g2.setColor(new Color(195, 177, 61));
                g2.fillRoundRect(slotX, slotY, gp.tilesize, gp.tilesize, 10, 10);
            }

            g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);

            if(entity.inventory.get(i).amount > 1) {
                g2.setFont(g2.getFont().deriveFont(12f));
                int amountX;
                int amountY;

                String s = "" + entity.inventory.get(i).amount;
                amountX = getXforAlignToRightText(s, slotX + 44);
                amountY = slotY + gp.tilesize;

                g2.setColor(Color.black);
                g2.drawString(s, amountX, amountY);
                g2.setColor(Color.white);
                g2.drawString(s, amountX - 3, amountY - 3);
            }

            slotX += slotSize;
            if (i == 4 || i == 9 || i == 14) {
                //TODO: make this dynamic
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        // cursor
        if(cursorActive) {
            int cursorX = slotXstart + (slotSize * slotCol);
            int cursorY = slotYstart + (slotSize * slotRow);
            int cursorWidth = gp.tilesize;
            int cursorHeight = gp.tilesize;

            g2.setColor(Color.WHITE);
            g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

            // description
            int dFrameX = frameX;
            int dFrameY = frameY + frameHeight;
            int dFrameWidth = frameWidth;
            int dFrameHeight = gp.tilesize * 3;


            int itemIndex = getItemIndexForSlot(slotCol, slotRow);

            if (itemIndex < entity.inventory.size()) {
                drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);

                int textX = dFrameX + padding;
                int textY = dFrameY + gp.tilesize;
                g2.setFont(g2.getFont().deriveFont(17f));
                g2.setColor(Color.white);

                for (String line : entity.inventory.get(itemIndex).description.split("\n")) {
                    g2.drawString(line, textX, textY);
                    textY += padding + 15;
                }
            }
        }

    }

    public int getItemIndexForSlot(int slotCol, int slotRow) {
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
    }

    public void drawPauseScreen() {
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,30));

        int frameX = gp.tilesize*4;
        int frameY = gp.tilesize;
        int frameWidth = gp.tilesize*8;
        int frameHeight = gp.tilesize*10;
        int padding = 20;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);


        g2.setColor(Color.WHITE);

        options_top(frameX, frameY);

    }

    public void options_top(int frameX, int frameY) {
        int textX = getXforCenteredText("PAUSED");
        int textY = gp.tilesize*3;
        g2.drawString("PAUSED", textX, textY);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,23));
        textX = getXforCenteredText("Keybinds:");
        textY += gp.tilesize-25;
        g2.drawString("Keybinds:", textX, textY);

        textY += gp.tilesize-25;
        textX = frameX + gp.tilesize-50;
        g2.drawString("Movement: WASD", textX, textY);

        textY += gp.tilesize-25;
        g2.drawString("Attack & interact:", textX, textY);
        textY += gp.tilesize-25;
        g2.drawString("Enter", textX, textY);

        textY += gp.tilesize-25;
        g2.drawString("Cast spell:", textX, textY);
        textY += gp.tilesize-25;
        g2.drawString("number keys", textX, textY);

        textY += gp.tilesize-25;
        g2.drawString("Shield: space", textX, textY);

        textY += gp.tilesize-25;
        g2.drawString("Run boots: Shift", textX, textY);

        g2.setColor(new Color(59, 155, 185));
        textX = getXforCenteredText("Press Q to quit game");
        textY += gp.tilesize-25;
        g2.drawString("Press Q to quit game", textX, textY);



    }

    public void drawDialogueScreenWithOptions() {

        int x = gp.tilesize * 2;
        int y = gp.screenHeight - (gp.tilesize*5);
        int width = gp.screenWidth - (gp.tilesize * 4);
        int height = gp.tilesize*4;

        drawSubWindow(x, y, width, height);

        x += gp.tilesize;
        y += gp.tilesize;

        if(npc.dialogues[npc.dialogueSet][npc.dialogueIndex] != null) {

            char dialogueCharachters[] = npc.dialogues[npc.dialogueSet][npc.dialogueIndex].toCharArray();
            if(charIndex < dialogueCharachters.length) {
                String s = String.valueOf(dialogueCharachters[charIndex]);
                combinedText = combinedText + s;
                currentDialogue = combinedText;

                charIndex++;
            }
        } else {
            npc.dialogueIndex = 0;
            if (gp.gameState == Gamepanel.GameStates.DIALOGUE) {
                gp.gameState = Gamepanel.GameStates.ACTIVE;
            }
        }

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.setColor(Color.WHITE);

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }


        int largestDialogueOptionLengthX = 0;
        int currentDialogueOptionLengthX = 0;

        for (int i = 0; i < dialogueOptions.length; i++) {
            if (dialogueOptions[i] != null) {
                for (char dialoguesCharacter : dialogueOptions[i].toCharArray()) {
                    currentDialogueOptionLengthX += g2.getFontMetrics().charWidth(dialoguesCharacter);
                }
                if (currentDialogueOptionLengthX > largestDialogueOptionLengthX) {
                    largestDialogueOptionLengthX = currentDialogueOptionLengthX;
                }
            }
        }

        x = gp.tilesize * 10;
        y = gp.tilesize * 4;
        width = (int) (largestDialogueOptionLengthX + 50);
        height = (int) ((gp.tilesize) * dialogueOptions.length);
        drawSubWindow(x, y, width, height);

        g2.setColor(Color.WHITE);

        x += 35;

        for (int i = 0; i < dialogueOptions.length; i++) {
            if (dialogueOptions[i] != null) {
                y += gp.tilesize - 10;
                g2.drawString(dialogueOptions[i], x, y);
                if (commandNum == i) {
                    g2.drawString(">", x - 24, y);
                    if (gp.keyH.enterPressed) {
                        charIndex = 0;
                        combinedText = "";
                        npc.handleDialogueChosenEvent(dialogueOptions[i]);
                        gp.keyH.enterPressed = false;
                    }
                }
            }
        }
    }

    public void drawDialogueScreen() {
        int x = gp.tilesize * 2;
        int y = gp.screenHeight - (gp.tilesize*5);
        int width = gp.screenWidth - (gp.tilesize * 4);
        int height = gp.tilesize*4;

        drawSubWindow(x, y, width, height);

        x += gp.tilesize;
        y += gp.tilesize;

        if(npc.dialogues[npc.dialogueSet][npc.dialogueIndex] != null) {

            char dialogueCharachters[] = npc.dialogues[npc.dialogueSet][npc.dialogueIndex].toCharArray();
            if(charIndex < dialogueCharachters.length) {
                String s = String.valueOf(dialogueCharachters[charIndex]);
                combinedText = combinedText + s;
                currentDialogue = combinedText;

                charIndex++;
            }

            if(gp.keyH.enterPressed) {
                charIndex = 0;
                combinedText = "";
                if (gp.gameState == Gamepanel.GameStates.DIALOGUE) {
                    npc.dialogueIndex++;
                    gp.keyH.enterPressed = false;
                }
            }
        } else {
            npc.dialogueIndex = 0;
            if (gp.gameState == Gamepanel.GameStates.DIALOGUE) {
                gp.gameState = Gamepanel.GameStates.ACTIVE;
            }
        }

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.setColor(Color.WHITE);

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }

    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 225);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(140, 139, 139);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(6));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }

    public void drawTitleScreen() {
        g2.setFont(g2.getFont().deriveFont( 80F));
        String text = "Arcane Quest";
        int x = getXforCenteredText(text);
        int y = gp.tilesize * 3;
        Color c = new Color(37, 89, 140);
        g2.setColor(c);
        g2.drawString(text, x+5, y+5); // shadow


        g2.setColor(Color.cyan);
        g2.drawString(text, x, y);
        g2.setColor(Color.white);

        x = gp.screenWidth/2 - (gp.tilesize*2)/2;
        y += gp.tilesize * 2;

        g2.drawImage(gp.player.down1, x, y, gp.tilesize*2, gp.tilesize*2, null);

        // menu


        g2.setFont(g2.getFont().deriveFont(48F));
        text = "Play";
        x = getXforCenteredText(text);
        y += gp.tilesize*3.5;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x-gp.tilesize, y);
        }

        text = "Options";
        x = getXforCenteredText(text);
        y += gp.tilesize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x-gp.tilesize, y);
        }


        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tilesize;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x-gp.tilesize, y);
        }

    }

    public void drawPlayerLife() {
        int x = gp.tilesize/2;
        int y = gp.tilesize/2;

        for (int i = 0; i < gp.player.maxHealth/2; i++) {
            g2.drawImage(heart_blank, x, y, gp.tilesize, gp.tilesize,null);
            x += gp.tilesize;
        }

        x = gp.tilesize/2;
        y = gp.tilesize/2;
        int i = 0;

        while (i < gp.player.health) {
            g2.drawImage(heart_half, x, y, gp.tilesize, gp.tilesize, null);
            i++;
            if(i < gp.player.health) {
                g2.drawImage(heart_full, x, y, gp.tilesize, gp.tilesize, null);
            }
            i++;
            x += gp.tilesize;
        }

        // draw mana bar
        x = gp.tilesize/2;
        y += gp.tilesize;

        double oneScale = (double)gp.tilesize*3/gp.player.maxMana;
        double levelRelLength = oneScale + (double) (gp.player.maxMana - gp.player.startingMana) * gp.tilesize/10;
        double hpBarValue = levelRelLength * gp.player.mana;

        g2.setColor(Color.BLACK);
        g2.fillRoundRect(x-5, y-5, (int)levelRelLength * gp.player.maxMana + 10, gp.tilesize/2 + 10, 25, 25);
        g2.setColor(new Color(65, 181, 205));
        g2.fillRoundRect(x, y, (int)hpBarValue, gp.tilesize/2, 20, 20);

    }


    public void drawMessage() {
        int messageX = gp.tilesize;
        int messageY = gp.tilesize*4;
        g2.setFont(g2.getFont().deriveFont( 23f));

        for (int i = 0; i < message.size(); i++) {
            if (message.get(i) == null) {
                continue;
            }

            g2.setColor(Color.black);
            g2.drawString(message.get(i), messageX+2, messageY+2);

            g2.setColor(Color.white);
            g2.drawString(message.get(i), messageX, messageY);

            int counter = messageCounter.get(i) + 1;
            messageCounter.set(i, counter);
            messageY += 50;

            if (messageCounter.get(i) > 180) {
                message.remove(i);
                messageCounter.remove(i);
            }
        }
    }

    public void drawCharacterDetailsScreen() {
        final int frameX = gp.tilesize;
        final int frameY = gp.tilesize;
        final int frameWidth = gp.tilesize*5;
        final int frameHeight = gp.tilesize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(20f));

        int textX = frameX + 20;
        int textY = frameY + gp.tilesize;
        final int lineHeight = 35;

        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("XP", textX, textY);
        textY += lineHeight;
        g2.drawString("Health", textX, textY);
        textY += lineHeight;
        g2.drawString("Gold", textX, textY);
        textY += lineHeight;
        g2.drawString("Mana", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Next level xp:", textX, textY);
        textY += lineHeight;

        int tailX = (frameX + frameWidth) - 30;
        textY = frameY + gp.tilesize;
        String value;

        value = String.valueOf(gp.player.level);
        textY = drawMenuValue(tailX, textY, value, lineHeight);

        value = String.valueOf(gp.player.xp);
        textY = drawMenuValue(tailX, textY, value, lineHeight);


        value = String.valueOf(gp.player.health + "/" + gp.player.maxHealth);
        textY = drawMenuValue(tailX, textY, value, lineHeight);

        value = String.valueOf(gp.player.coin);
        textY = drawMenuValue(tailX, textY, value, lineHeight);

        value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
        textY = drawMenuValue(tailX, textY, value, lineHeight);

        value = String.valueOf(gp.player.attack);
        textY = drawMenuValue(tailX, textY, value, lineHeight);

        value = String.valueOf(gp.player.defense);
        textY = drawMenuValue(tailX, textY, value, lineHeight) + lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textY = drawMenuValue(tailX, textY, value, lineHeight);
    }

    public int drawMenuValue(int x, int y, String value, int distance) {
        x = getXforAlignToRightText(value, x);
        g2.drawString(value, x, y);
        y += distance;
        return y;
    }

    public int getXforAlignToRightText(String text, int tailX) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
}