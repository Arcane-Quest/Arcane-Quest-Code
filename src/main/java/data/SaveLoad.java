package data;

import entity.EntitySerializableData;
import interactiveTile.IN_Rock;
import interactiveTile.InteractiveTile;
import object.*;
import org.example.Gamepanel;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.awt.Desktop;
import java.util.HashMap;
import java.util.Objects;

import entity.Entity;

public class SaveLoad {
    Gamepanel gp;

    public SaveLoad(Gamepanel gp) {
        this.gp = gp;
    }

    public void save(){
        try {
            System.out.println(gp.player.currentWeapon);

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("saves/main_save/save_sigils.dat")));
            DataStorage ds = new DataStorage();
            ds.level = gp.player.level;
            ds.maxHealth = gp.player.maxHealth;
            ds.health = gp.player.health;
            ds.maxMana = gp.player.maxMana;
            ds.coin = gp.player.coin;
            ds.worldIndex = gp.currentMap;
            ds.continueWorldX = gp.player.worldX;
            ds.continueWorldY = gp.player.worldY;
            ds.xp = gp.player.xp;
            ds.nextLevelXp = gp.player.nextLevelExp;
            ds.dexterity = gp.player.dexterity;
            ds.strength = gp.player.strength;
//            ds.spells = gp.player.spells;
            ds.mana = (int) gp.player.mana;


            for (int i = 0; i < gp.player.inventory.size(); i++) {
                if (gp.player.inventory.get(i) != null) {
                    ds.itemNames.add(gp.player.inventory.get(i).name);
                    ds.itemAmounts.add(gp.player.inventory.get(i).amount);
                    ds.itemData.add(gp.player.inventory.get(i).saveableData);
                }
            }
            if(gp.player.currentWeapon != null) {
                ds.currentWeaponSlot = gp.player.getCurrentWeaponSlot();
                ds.hasCurrentWeapon = true;
            } else { ds.hasCurrentWeapon = false; }

            if(gp.player.currentShield != null) {
                ds.currentShieldSlot = gp.player.getCurrentShieldSlot();
                ds.hasCurrentShield = true;
            } else { ds.hasCurrentShield = false; }

            if (gp.player.currentAmulet != null) {
                ds.currentAmuletSlot = gp.player.getCurrentAmuletSlot();
                ds.hasCurrentAmulet = true;
            } else { ds.hasCurrentAmulet = false; }

            if(gp.player.currentRing != null) {
                ds.currentRingSlot = gp.player.getCurrentRingSlot();
                ds.hasCurrentRing = true;
            } else { ds.hasCurrentRing = false; }


            // Map
            ds.mapObjectNames = new String[gp.maxMap][gp.obj[1].length];
            ds.mapObjectWorldX = new int[gp.maxMap][gp.obj[1].length];
            ds.mapObjectWorldY = new int[gp.maxMap][gp.obj[1].length];
            ds.mapObjectLootNames = new String[gp.maxMap][gp.obj[1].length];
            ds.mapObjectOpened = new boolean[gp.maxMap][gp.obj[1].length];

            for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                for(int i = 0; i < gp.obj[1].length; i++) {
                    if(gp.obj[mapNum][i] == null) {
                        ds.mapObjectNames[mapNum][i] = "NA";
                    } else if (Objects.equals(gp.obj[mapNum][i].name, OBJ_Sign.objIDName)) {
                        ds.mapObjectNames[mapNum][i] = "IGN";
                    }
                    else {
                        ds.mapObjectNames[mapNum][i] = gp.obj[mapNum][i].name;
                        ds.mapObjectWorldX[mapNum][i] = gp.obj[mapNum][i].worldX;
                        ds.mapObjectWorldY[mapNum][i] = gp.obj[mapNum][i].worldY;
                        if(gp.obj[mapNum][i].contents != null) {
                            ds.mapObjectLootNames[mapNum][i] = gp.obj[mapNum][i].contents.name;
                        }
                        ds.mapObjectOpened[mapNum][i] = gp.obj[mapNum][i].opened;
                    }
                }
            }

            // Tiles
            ds.interactiveTileNames = new String[gp.maxMap][gp.interactiveTile[1].length];
            ds.mapITileWorldY = new int[gp.maxMap][gp.interactiveTile[1].length];
            ds.mapITileWorldX = new int[gp.maxMap][gp.interactiveTile[1].length];

            for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                for(int i = 0; i < gp.interactiveTile[1].length; i++) {
                    if(gp.interactiveTile[mapNum][i] == null) {
                        ds.interactiveTileNames[mapNum][i] = "NA";
                    } else {
                        ds.interactiveTileNames[mapNum][i] = gp.interactiveTile[mapNum][i].name;
                        ds.mapITileWorldX[mapNum][i] = gp.interactiveTile[mapNum][i].worldX;
                        ds.mapITileWorldY[mapNum][i] = gp.interactiveTile[mapNum][i].worldY;
                    }
                }
            }

//            ds.entitySavedData = new EntitySerializableData[gp.maxMap][gp.npc[1].length];
//
//            for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
//                for(int i = 0; i < gp.npc[1].length; i++) {
//                    if(gp.npc[mapNum][i] != null) {
//                        if (gp.npc[mapNum][i].saveableData != null) {
//                            ds.entitySavedData[mapNum][i] = gp.npc[mapNum][i].saveableData;
//                            System.out.println("Saving NPC savable data: " + gp.npc[mapNum][i].name);
//                        }
//                    }
//                }
//            }


            //Save the data
            oos.writeObject(ds);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("saves/main_save/save_sigils.dat")));
            DataStorage ds = (DataStorage) ois.readObject();

            gp.player.level = ds.level;
            gp.player.maxHealth = ds.maxHealth;
            gp.player.health = ds.health;
            gp.player.maxMana = ds.maxMana;
            gp.player.coin = ds.coin;
            gp.currentMap = ds.worldIndex;
            gp.player.worldX = ds.continueWorldX;
            gp.player.worldY = ds.continueWorldY;
            gp.player.xp = ds.xp;
            gp.player.nextLevelExp = ds.nextLevelXp;
            gp.player.dexterity = ds.dexterity;
            gp.player.strength = ds.strength;
            gp.player.mana = ds.mana;

            gp.player.inventory.clear();
            for (int i = 0; i < ds.itemNames.size(); i++) {
                if(ds.itemNames.get(i) != null) {
                    gp.player.tryAddInventory(gp.entityGenerator.getObjectFromString(ds.itemNames.get(i)));
                    if(ds.itemAmounts.get(i) == null) {
                    System.out.println("item: " + ds.itemNames.get(i) + " added to inventory");
                    gp.player.inventory.get(i).amount = ds.itemAmounts.get(i);
                    }
                    if(ds.itemData.size() > i) {
                        gp.player.inventory.get(i).saveableData = ds.itemData.get(i);
                        if(ds.itemData.get(i) != null) {
                            gp.player.inventory.get(i).loadfromsaveabledata();
                        }
                    }
                }
            }

            if(ds.hasCurrentWeapon) {
                gp.player.currentWeapon = gp.player.inventory.get(ds.currentWeaponSlot);
            }
            if(ds.hasCurrentShield) {
                gp.player.currentShield = gp.player.inventory.get(ds.currentShieldSlot);
            }
            if(ds.hasCurrentRing) {
                gp.player.currentRing = gp.player.inventory.get(ds.currentRingSlot);
            }
            if(ds.hasCurrentAmulet) {
                gp.player.currentAmulet = gp.player.inventory.get(ds.currentAmuletSlot);
            }
            
            gp.tileM.loadMap(0, "wizard_hut");
            ois.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Exception when loading save data",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        gp.player.getPlayerImage();
        gp.player.getAttack();
        gp.player.getDefense();
    }

}
