package entity;

import java.io.Serializable;
import java.util.HashMap;

public class EntitySerializableData implements Serializable {
    public HashMap<String, Object> savedValues = new HashMap<>();

    public EntitySerializableData() {}

    public void saveTrait(String key, Object value) {
        savedValues.put(key, value);
    }
    public Object loadTrait(String key) {
        return savedValues.get(key);
    }
}
