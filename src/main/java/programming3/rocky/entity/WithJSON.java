package programming3.rocky.entity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

public interface WithJSON extends JSONString {
    default JSONObject toJSONObject() throws JSONException {
        return new JSONObject(toJSONString());
    }
}