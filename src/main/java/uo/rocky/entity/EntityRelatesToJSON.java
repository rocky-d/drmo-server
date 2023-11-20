package uo.rocky.entity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.time.format.DateTimeFormatter;

/**
 * TOWRITE
 * <p>
 * Extends {@link JSONString}.
 *
 * @author Rocky Haotian Du
 */
public interface EntityRelatesToJSON extends JSONString {

    DateTimeFormatter LOCALDATETIME_FORMATTER_T = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    static String escapeDoubleQuotes(String string) {
        return null == string ? "null" : "\"" + string.replace("\"", "\\\"") + "\"";
    }

    default JSONObject toJSONObject() throws JSONException {
        return new JSONObject(toJSONString());
    }
}