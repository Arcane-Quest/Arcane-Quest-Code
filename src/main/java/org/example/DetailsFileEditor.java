package org.example;

import data.MapDetailsDataStructure;
import entity.Entity;
import entity.EntitySerializableData;
import interactiveTile.InteractiveTile;
import object.OBJ_Chest;
import object.OBJ_Sword;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class DetailsFileEditor {
    public static void main(String args[]) {


        var mapName = "details";

        var objects = new Entity[10];
        objects = instantiateObjDetails(objects);
        var iTile = new InteractiveTile[50];
        iTile = instantiateiTileDetails(iTile);

        makeMapDatFile(objects, iTile, mapName);
        System.out.println("Finished making details file, saved at saves/maintenance/" + mapName + ".dat");

    }

    /// Change this based on the file being made
    private static Entity[] instantiateObjDetails(Entity[] objects) {
        var gp = new Gamepanel();
        var result = objects;
        // Change this to what you want in the maps content
        var i = 0;

        var input = new OBJ_Chest(gp);
        input.setContents(new OBJ_Sword(gp));
        input.worldX = gp.tilesize * 20;
        input.worldY = gp.tilesize * 20;
        result[i] = input;

        // end of content area
        return result;
    }

    /// Change this based on the file being made
    private static InteractiveTile[] instantiateiTileDetails(InteractiveTile[] interactiveTiles) {
        var result = interactiveTiles;
        return result;
    }

    public static void makeMapDatFile(Entity[] obj, InteractiveTile[] iTile, String filename) {
        var mapDetailsClass = new MapDetailsDataStructure();
        // instantiating class parameters
        mapDetailsClass.objLoadableIDNames = new String[10];
        mapDetailsClass.objCoordinates = new int[10][2];
        mapDetailsClass.serializableData = new EntitySerializableData[10];
        mapDetailsClass.iTileLoadableIDNames = new String[10];
        mapDetailsClass.iTileCoordinates = new int[10][2];

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] == null)  {
                continue;
            }
            mapDetailsClass.objLoadableIDNames[i] = obj[i].name;
            mapDetailsClass.objCoordinates[i][0] = obj[i].worldX;
            mapDetailsClass.objCoordinates[i][1] = obj[i].worldY;
            mapDetailsClass.serializableData[i] = obj[i].saveableData;
        }
        for (int i = 0; i < iTile.length; i++) {
            if (iTile[i] == null)  {
                continue;
            }
            mapDetailsClass.objLoadableIDNames[i] = iTile[i].name;
            mapDetailsClass.objCoordinates[i][0] = iTile[i].worldX;
            mapDetailsClass.objCoordinates[i][1] = iTile[i].worldY;
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("saves/maintenance/" + filename + ".dat")));
            oos.writeObject(mapDetailsClass);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
