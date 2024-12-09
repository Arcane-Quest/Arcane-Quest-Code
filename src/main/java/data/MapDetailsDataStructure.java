package data;

import entity.EntitySerializableData;

import java.io.Serializable;

public class MapDetailsDataStructure implements Serializable {
    public String[] objLoadableIDNames;

    /// 2d array for each item is \[x, y]
    public int[][] objCoordinates;
    public String[] iTileLoadableIDNames;
    public int[][] iTileCoordinates;

    /// objects serializable data
    public EntitySerializableData[] serializableData;
}
