package uo.rocky.entity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

/**
 * TODO
 *
 * @author Rocky Haotian Du
 */
public interface EntityRelatesToJSON extends JSONString {

    static String escapeDoubleQuotes(String string) {
        return null == string ? "null" : "\"" + string.replace("\"", "\\\"") + "\"";
    }

    default JSONObject toJSONObject() throws JSONException {
        return new JSONObject(toJSONString());
    }
}