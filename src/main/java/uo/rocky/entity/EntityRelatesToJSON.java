package uo.rocky.entity;

import org.json.JSONObject;
import org.json.JSONString;

public interface EntityRelatesToJSON extends JSONString {
    static String escapeDoubleQuotes(String string) {
        return null == string ? "null" : "\"" + string.replace("\"", "\\\"") + "\"";
    }

    default JSONObject toJSONObject() {
        return new JSONObject(toJSONString());
    }
}