package programming3.rocky.entity;

import org.json.JSONObject;
import org.json.JSONString;

public interface WithJSON extends JSONString {
    default JSONObject toJSONObject() {
        return new JSONObject(toJSONString());
    }
}