package org.example;

import entity.Entity;
import interactiveTile.IN_Rock;
import interactiveTile.InteractiveTile;
import object.*;
import monster.*;

public class EntityGenerator {
    Gamepanel gp;

    public EntityGenerator(Gamepanel gp) {
        this.gp = gp;
    }

    public Entity getMonsterFromString(String monsterString) {
        Entity monster = null;
        switch (monsterString) {
            case MON_Green_Slime.objIDName:
                monster = new MON_Green_Slime(gp);
                break;
            case MON_Orc.objIDName:
                monster = new MON_Orc(gp);
                break;
        }
        return monster;
    }

    public Entity getObjectFromString(String itemName) {
        Entity obj = null;

        switch (itemName) {
            case OBJ_Sword.objIDName: obj = new OBJ_Sword(gp); break;
            case OBJ_Shield.objIDName: obj = new OBJ_Shield(gp); break;
            case OBJ_Coin.objIDName: obj = new OBJ_Coin(gp); break;
            case OBJ_Key.objIDName: obj = new OBJ_Key(gp); break;
            case OBJ_Mana_Potion.objIDName: obj = new OBJ_Mana_Potion(gp); break;
            case OBJ_Health_Potion.objIDName: obj = new OBJ_Health_Potion(gp); break;
            case OBJ_Lantern.objIDName: obj = new OBJ_Lantern(gp); break;
            case OBJ_Door.objIDName: obj = new OBJ_Door(gp); break;
            case OBJ_Chest.objIDName: obj = new OBJ_Chest(gp); break;
            case OBJ_Rock_Smasher.objIDName: obj = new OBJ_Rock_Smasher(gp); break;
            case OBJ_Sign.objIDName: obj = new OBJ_Sign(gp); break;
            case OBJ_Ability_focus_crystal.objIDName: obj = new OBJ_Ability_focus_crystal(gp); break;
            case OBJ_satchel.objIDName: obj = new OBJ_satchel(gp); break;
            case OBJ_Alchemy_Table.objIDName: obj = new OBJ_Alchemy_Table(gp); break;
            case OBJ_Fireball.objIDName: obj = new OBJ_Fireball(gp); break;
            case OBJ_Rune_Paper.objIDName: obj = new OBJ_Rune_Paper(gp); break;
        }

        return obj;
    }

    public InteractiveTile getInteractiveTileFromString(String name, int spawnWorldX, int spawnWorldY) {
        InteractiveTile tile = null;

        switch (name) {
            case "Rock_interactable": tile = new IN_Rock(gp, spawnWorldX/gp.tilesize, spawnWorldY/gp.tilesize); break;
        }

        return tile;
    }

}
