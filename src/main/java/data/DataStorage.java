package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import entity.Entity;
import entity.EntitySerializableData;

public class DataStorage implements Serializable {
    // Player stats
    int level;
    int maxHealth;
    int health;
    int maxMana;
    int mana;
    int strength;
    int dexterity;
    int xp;
    int nextLevelXp;
    int coin;
    int worldIndex;
    int continueWorldX;
    int continueWorldY;

    //Inventory
    ArrayList<String> itemNames = new ArrayList<>();
    ArrayList<Integer> itemAmounts = new ArrayList<>();
    ArrayList<EntitySerializableData> itemData = new ArrayList<>();
    int currentWeaponSlot;
    boolean hasCurrentWeapon;
    int currentShieldSlot;
    boolean hasCurrentShield;
    int currentRingSlot;
    boolean hasCurrentRing;
    int currentAmuletSlot;
    boolean hasCurrentAmulet;

    //Map
    String mapObjectNames[][];
    int mapObjectWorldX[][];
    int mapObjectWorldY[][];
    String mapObjectLootNames[][];
    boolean mapObjectOpened[][];
    // tiles
    String interactiveTileNames[][];
    int mapITileWorldX[][];
    int mapITileWorldY[][];

}
