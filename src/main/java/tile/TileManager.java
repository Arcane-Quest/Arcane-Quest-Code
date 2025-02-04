package tile;

import data.MapDetailsDataStructure;
import entity.Entity;
import entity.EntitySerializableData;
import org.example.Gamepanel;
import org.example.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;

public class TileManager {
    Gamepanel gp;
    public Tile[] tile;
    public int mapTileNum[][][];
    public String currentMap;

    boolean drawPath = false; // developer debug only, can only show 1 at a time

    public TileManager(Gamepanel gp) {
        this.gp = gp;

        tile = new Tile[50];
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap(0, "wizard_hut");
        //loadMap("/maps/world01", 1);
        //loadMap("/maps/testMap2", 2);
        //loadMap("/maps/Starting_village", 3);

    }

    public void getTileImage() {
            setup(0, "grass1", false);
            setup(1, "brick_wall1", true);
            setup(2, "water1", true);
            setup(3, "path", false);
            setup(4, "dungeon_floor", false);
            setup(5, "mountain", true);
            setup(6, "tree", true);
            setup(7, "black_tile", true);
            setup(9, "debug_texture", false);

            setup(11, "grass2", false);
            setup(12, "grass3", false);
            setup(13, "grass4", false);
            setup(14, "brick_wall2", true);
            setup(15, "path2", false);
            setup(16, "path3", false);
            setup(17, "path4", false);
            setup(18, "straw_roof1", true);
            setup(19, "straw_roof_left_edge", false);
            setup(20, "straw_roof_right_edge", false);
            setup(21, "straw_roof1", false);

            // More info (and fixes) in the main/player.java class
    }

    public void setup(int index, String imagePath, boolean collision) {
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imagePath + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tilesize, gp.tilesize);
            tile[index].collision = collision;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadNPCs(String path, int mapIndex) throws IOException {

        // clear all previous npcs
        for (int i = 0; i < gp.npc[1].length; i++) {
            gp.npc[gp.currentMap][i] = null;
        }

        // instantiate an input stream
        InputStream is = getClass().getResourceAsStream(path + "/npcs.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        var line = br.readLine();
        while (br.ready() && line != null) {
            // when the line is not null
            // split the data in each row
            String rawNPC[] = line.split(", ");

            // generate an npc from the expected string in the first argument
            var npc = gp.entityGenerator.getMonsterFromString(rawNPC[0]);
            if(npc != null) {
                // if the npc isn't null, set it's x, y, and saveable data
                npc.worldX = Integer.parseInt(rawNPC[1]);
                npc.worldY = Integer.parseInt(rawNPC[2]);
                npc.loadfromsaveabledata();

                for (int ii = 0; ii < gp.npc[1].length; ii++) {
                    gp.npc[gp.currentMap][ii] = npc;
                }

            } else {
                throw new RuntimeException("[Error]: Could not find name npc: " + rawNPC[0]);
            }

            line = br.readLine();
        }

    }

    /// Loads a map from the /maps/* directory
    /// Usage:
    /// ```
    /// loadMap("foo");
    /// ```
    /// to use the current map:
    /// ```
    /// loadMap(gp.tileM.currentMap);
    /// ```
    ///
    /// A map folder expects the following:
    /// 1. a `locations.txt` file for tiles
    /// 2. a `details.dat` file for objects and other things (follows the `MapDetailsDataStructure` class)
    ///     I. the `DetailsFileEditor` class can be used to make these
    /// 3. an `npcs.txt` file
    /// 4. an `events.txt` file
    ///
    ///
    /// Due to the black magic of resource folders and the file class,
    /// (currently) you must have all of these files, or the program will crash
    public void loadMap(int mapIndex, String mapName) {
        var path = "/maps/" + mapName;
        currentMap = mapName;
        try {
            loadTiles(path, mapIndex);
            loadMapMonsters(path);
            loadNPCs(path, mapIndex);
            loadMapDetails(path, mapName);
            gp.eHandler.loadEventsFromPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void loadTiles(String path, int mapIndex) throws IOException {
        // create a new buffered reader
        InputStream is = getClass().getResourceAsStream(path + "/locations.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        int col = 0;
        int row = 0;
        // scan each tile and pack it into an array
        while (col < gp.maxWorldCol && row < gp.maxWorldRow
                && br.ready()) {
            String line = br.readLine();

            while (col < gp.maxWorldCol) {
                String numbers[] = line.split(" ");

                int num = Integer.parseInt(numbers[col]);
                mapTileNum[mapIndex][col][row] = num;
                col++;
            }
            if (col == gp.maxWorldRow) {
                col = 0;
                row++;
            }
        }
        br.close();
    }

    private void loadMapDetails(String path, String name) {

        MapDetailsDataStructure mapDetails = null;
        var found = false;
        try {
            var ois = new ObjectInputStream(new FileInputStream(new File("saves/main_save/map_data/" + name + "/details.dat")));
            mapDetails = (MapDetailsDataStructure) ois.readObject();
        } catch (FileNotFoundException e) {
            found = false;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        // First attempt to get


        if (!found) {
            try {
                var ois = new ObjectInputStream(getClass().getResourceAsStream("/maps/" + name + "/details.dat"));
                mapDetails = (MapDetailsDataStructure) ois.readObject();
            } catch (Exception e) {
                System.out.println("No map Details file found for map: " + name);
                e.printStackTrace();
            }
        }


//        if (new File("saves/main_save/map_data/" + name + "/details.dat" ).exists()) {
//            ois = new ObjectInputStream(new FileInputStream(new File("saves/main_save/map_data/" + name + "/details.dat" )));
//        }
//


        // clear map objects
        for (int i = 0; i < gp.obj[1].length; i++) {
            gp.obj[gp.currentMap][i] = null;
        }

        if (mapDetails == null) {
            System.out.println("No map details found for map: " + name);
        }

        // Load map objects into the array
        for (int i = 0; i < mapDetails.objLoadableIDNames.length; i++) {
            if (mapDetails.objLoadableIDNames[i] == null || mapDetails.objLoadableIDNames[i].equals("")) {
                continue;
            }
            // if the file's array is longer than the available slots, crash.
            if (i > gp.obj.length) {
                throw new RuntimeException("Error: mapDetails.objLoadableIDNames too long for file: " + path);
            }

            // instantiate the specified object and assign it's world x, y, and saveAble data.
            var evaluatedReturn = gp.entityGenerator.getObjectFromString(mapDetails.objLoadableIDNames[i]);
            evaluatedReturn.saveableData = mapDetails.serializableData[i];
            evaluatedReturn.worldX = mapDetails.objCoordinates[i][0];
            evaluatedReturn.worldY = mapDetails.objCoordinates[i][1];

            // assign the object to the objects array
            gp.obj[gp.currentMap][i] = evaluatedReturn;
        }

        // Clear interactive tiles
        for (int i = 0; i < gp.interactiveTile[1].length; i++) {
            gp.interactiveTile[gp.currentMap][i] = null;
        }

        // load interactive tiles
        for (int i = 0; i < mapDetails.iTileLoadableIDNames.length; i++) {
            if (mapDetails.iTileLoadableIDNames[i] == null || mapDetails.iTileLoadableIDNames[i].equals("")) {
                continue;
            }
            // if the array is too long, throw an error
            if (i > gp.interactiveTile[1].length) {
                throw new RuntimeException("Error: mapDetails.objLoadableIDNames too long for file: " + path);
            }

            // generate a new tile and save it to the interactive Tile array
            gp.interactiveTile[gp.currentMap][i] = gp.entityGenerator.getInteractiveTileFromString(
                    mapDetails.iTileLoadableIDNames[i],
                    mapDetails.iTileCoordinates[i][0],
                    mapDetails.iTileCoordinates[i][1]
            );
        }

    }

    public void saveMapDetails(String mapNameID) {
        try {

            // make a file writer
            var file = new File("saves/main_save/map_data/" + mapNameID + ".dat");
            if (!file.exists()) {
                file.createNewFile();
            }
            var oos = new ObjectOutputStream(new FileOutputStream(file));

            var mapDetails = new MapDetailsDataStructure();
            // create a temporary array to store the objects in
            var tmpNames = new String[gp.obj[1].length];
            var tmpObjCoords = new int[gp.obj[1].length][gp.obj[1].length];
            var tmpiTileCoords = new int[gp.obj[1].length][gp.obj[1].length];
            var tmpItileArray = new String[gp.obj[1].length];
            var tmpData = new EntitySerializableData[gp.obj[1].length];

            // for every object in the objects array, save it's name, x, y, and savable data
            for (int i = 0; i < gp.obj[1].length; i++) {
                if (gp.obj[gp.currentMap][i] == null) {
                    continue;
                }
                tmpNames[i] = gp.obj[gp.currentMap][i].name;
                tmpObjCoords[i][0] = gp.obj[gp.currentMap][i].worldX;
                tmpObjCoords[i][1] = gp.obj[gp.currentMap][i].worldY;
                tmpData[i] = gp.obj[gp.currentMap][i].saveableData;
            }
            // Save the arrays to the data structure
            mapDetails.objLoadableIDNames = tmpNames;
            mapDetails.serializableData = tmpData;
            mapDetails.objCoordinates = tmpObjCoords;


            // for every interactive tile, save it's name, worldX, and WorldY
            for (int i = 0; i < gp.interactiveTile[1].length; i++) {
                if (gp.interactiveTile[gp.currentMap][i] == null) {
                    continue;
                }
                tmpItileArray[i] = gp.interactiveTile[gp.currentMap][i].name;
                tmpiTileCoords[i][0] = gp.interactiveTile[gp.currentMap][i].worldX;
                tmpiTileCoords[i][1] = gp.interactiveTile[gp.currentMap][i].worldY;
            }
            mapDetails.iTileLoadableIDNames = tmpItileArray;
            mapDetails.iTileCoordinates = tmpiTileCoords;

            // Write and close
            oos.writeObject(mapDetails);
            oos.close();

            // finally, save all npc's savable data:
            for (int i = 0; i < gp.npc[1].length; i++) {
                if (gp.npc[gp.currentMap][i] == null) {
                    continue;
                }
                gp.npc[gp.currentMap][i].saveData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMapMonsters(String path) throws IOException {
            // Load Monsters for this map
            // clear all previous monsters
            for (int i = 0; i < gp.monster[1].length; i++) {
                gp.monster[gp.currentMap][i] = null;
            }

            InputStream is = getClass().getResourceAsStream(path + "/monsters.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            var line = br.readLine();
            while (line != null) {
                if (line.strip().equals("")) {
                    return;
                }
                String rawMonster[] = line.split(", ");

                Entity monster = gp.entityGenerator.getMonsterFromString(rawMonster[0]);
                if(monster != null) {
                    monster.worldX = Integer.parseInt(rawMonster[1]);
                    monster.worldY = Integer.parseInt(rawMonster[2]);

                    for (int ii = 0; ii < gp.monster[1].length; ii++) {
                        if (gp.monster[gp.currentMap][ii] == null) {
                            gp.monster[gp.currentMap][ii] = monster;
                            break;
                        }
                    }
                } else {
                    System.out.println("[Error]: Could not find name monster: " + rawMonster[0]);
                }

                line = br.readLine();
            }
    }

    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;


        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];

            int worldX = worldCol * gp.tilesize;
            int worldY = worldRow * gp.tilesize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if(gp.player.screenX > gp.player.worldX) {
                screenX = worldX;
            }
            if(gp.player.screenY > gp.player.worldY) {
                screenY = worldY;
            }
            int rightOffset = gp.screenWidth - gp.player.screenX;
            if(rightOffset > gp.worldWidth - gp.player.worldX) {
                screenX = gp.screenWidth - (gp.worldWidth - worldX);
            }
            int bottomOffset = gp.screenHeight - gp.player.screenY;
            if(bottomOffset > gp.worldHeight - gp.player.worldY) {
                screenY = gp.screenHeight - (gp.worldHeight - worldY);
            }

            if(worldX + gp.tilesize > gp.player.worldX - gp.player.screenX
                    && worldX - gp.tilesize < gp.player.worldX + gp.player.screenX
                    && worldY + gp.tilesize > gp.player.worldY - gp.player.screenY
                    && worldY - gp.tilesize < gp.player.worldY + gp.player.screenY && tile.length >= tileNum
                    && tile[tileNum] != null) {

                g2.drawImage(tile[tileNum].image, screenX, screenY,null);
            } else if (gp.player.screenX > gp.player.worldX || gp.player.screenY > gp.player.worldY
                    || rightOffset > gp.worldWidth - gp.player.worldX ||
                    bottomOffset > gp.worldHeight - gp.player.worldY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY,null);
            }


            worldCol++;


            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

        if (drawPath) {
            g2.setColor(new Color(255, 0, 0, 129));
            
            for(int i = 0; i < gp.pFinder.pathList.size(); i++) {
                int worldX = gp.pFinder.pathList.get(i).col * gp.tilesize;
                int worldY = gp.pFinder.pathList.get(i).row * gp.tilesize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                g2.fillRect(screenX, screenY, gp.tilesize, gp.tilesize);
            }
        }
    }
}
