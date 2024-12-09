package org.example;

import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;

import entity.Entity;

public class EventHandler {
    Gamepanel gp;
    int previousEventX, previousEventY;
    boolean canTouchEvent = true;
    int tempCol, tempRow;
    Entity eventMaster;

    /// An array of triggerable events for determineTriggerPlayerEvent() to iterate over
    Runnable[][] events;

    /// Search the Runnable[][] events array based on the player's position
    /// and run the event for the tile the player is on if there is one
    public void determineTriggerPlayerEvent() {
        if ((gp.player.getCol() == previousEventX / gp.tilesize) && (gp.player.getRow() == previousEventY / gp.tilesize)) {
            return;
        }

        // Return solid area to defaults
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        // Update solid area to player's current position
        gp.player.solidArea.x += gp.player.worldX;
        gp.player.solidArea.y += gp.player.worldY;

        // round the player's world x and world y down into columns and rows
        var col = gp.player.worldX / gp.tilesize;
        var row = gp.player.worldY / gp.tilesize;



        // The events array is equal to the amount of columns and rows in the world
        // therefore, hop to the column in the array and see if there is an executable function
        // if there is, run it.
        if (col > gp.maxWorldCol || row > gp.maxWorldRow) {
            gp.ui.setToolTip("Out of bounds");
            return;
        }

        if (events[col][row] != null) {
            events[col][row].run();
            previousEventX = gp.player.worldX;
            previousEventY = gp.player.worldY;
        }

        // Return solid area back to defaults
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
    }

    public void loadEventsFromPath(String path) throws IOException {
//        if (!(new File(path + "/events.txt").exists())) {
//            System.out.println("No events found for: " + path);
//            return;
//        }

        for (int i = 0; i < events[0].length; i++) {
            Arrays.fill(events[i], null);
        }



        InputStream is = getClass().getResourceAsStream(path + "/events.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        var line = br.readLine();
        while (line != null) {
            String rawEventDetails[] = line.split(", ");
            Runnable triggerableEvent = null;
            System.out.println("Loading Event: " + line);
            switch (rawEventDetails[0]) {
                case "teleport":
                    triggerableEvent = new Runnable() {
                        @Override
                        public void run() {
                            teleport(
                                    rawEventDetails[3],
                                    Integer.parseInt(rawEventDetails[4]),
                                    Integer.parseInt(rawEventDetails[5]),
                                    Boolean.parseBoolean(rawEventDetails[6])
                            );
                        }
                    };
                    break;
                case "restPoint":
                    triggerableEvent = new Runnable() {
                        @Override
                        public void run() {
                            restPoint(Gamepanel.GameStates.DIALOGUE);
                        }
                    };
                    break;
                case "poison_grass":
                    triggerableEvent = new Runnable() {
                        @Override
                        public void run() {
                            poisonGrass(Gamepanel.GameStates.DIALOGUE);
                        }
                    };
                    break;
                default:
                    System.out.println(
                            "[Error]: Unknown Event: \"" + line + "\", loading from \"" + path + "/events.txt\""
                    );
                    break;
            }

            events[Integer.parseInt(rawEventDetails[1])][Integer.parseInt(rawEventDetails[2])] =
                    triggerableEvent;

            line = br.readLine();
        }
    }

    public EventHandler(Gamepanel gp) {
        this.gp = gp;
        this.events = new Runnable[gp.maxWorldCol][gp.maxWorldRow];

        eventMaster = new Entity(gp);
        setDialogue();
    }

    /// On every update: if the player meets conditions to trigger events
    /// Trigger the event in the players tile (if it exists)
    public void checkEvent() {
        // Player has to move more than a tile away from the previous event
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > gp.tilesize + 3) {
            canTouchEvent = true;
        }

        if(canTouchEvent) {
            determineTriggerPlayerEvent();
        }
    }



    public void setDialogue() {
        eventMaster.dialogues[0][0] = "You stepped into poison grass.";
        eventMaster.dialogues[0][1]= "EMOTIONAL DAMAGE.";

        eventMaster.dialogues[1][0] = "You rest and recover from\nyour adventures";
        eventMaster.dialogues[1][1] = "Your game has been saved.";
    }


    // events

    private void teleport(String map, int col, int row, boolean save) {
        gp.gameState = Gamepanel.GameStates.TELEPORT;
        // Prevent any events from triggering on the teleported to tile
        // (temporarily)
        previousEventX = gp.player.worldX;
        previousEventY = gp.player.worldY;
        canTouchEvent = false;

        gp.tileM.saveMapDetails(gp.tileM.currentMap);
        gp.tileM.loadMap(0, map);

        if(save) {
            gp.saveLoad.save();
        }

        gp.player.worldX = col * gp.tilesize;
        gp.player.worldY = row * gp.tilesize;
    }

    public void poisonGrass(Gamepanel.GameStates gameState) {
        eventMaster.startDialogue(eventMaster, 0);
        gp.player.health -= 1;

        canTouchEvent = false;
    }

    public void restPoint(Gamepanel.GameStates gameState) {
        gp.ui.setToolTip("Press Enter to rest");
        if(gp.keyH.enterPressed) {
            eventMaster.startDialogue(eventMaster, 1);
            gp.player.health = gp.player.maxHealth;
            gp.player.mana = gp.player.maxMana;
            gp.tileM.saveMapDetails(gp.tileM.currentMap);
            gp.tileM.loadMap(0, gp.tileM.currentMap);
            gp.saveLoad.save();
        }
    }
}
