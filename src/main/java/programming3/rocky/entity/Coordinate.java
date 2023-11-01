package programming3.rocky.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.StringJoiner;

public class Coordinate {
    private long id;
    private double longitude;
    private double latitude;
    private String datetime;
    private Danger danger;
    private String description;
    private String usrName;

    public Coordinate(long id, double longitude, double latitude, String datetime, Danger danger, String usrName, String description) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.datetime = datetime;
        this.danger = danger;
        this.usrName = usrName;
        this.description = description;
    }

    public Coordinate(JSONObject jsonObject) throws JSONException {
        id = Instant.now().toEpochMilli();
        longitude = jsonObject.getDouble("longitude");
        latitude = jsonObject.getDouble("latitude");
        datetime = jsonObject.getString("sent");
        danger = Danger.valueOf(jsonObject.getString("dangertype").toUpperCase());
        usrName = jsonObject.getString("username");
        description = jsonObject.has("description") ? jsonObject.getString("description") : null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Danger getDanger() {
        return danger;
    }

    public void setDanger(Danger danger) {
        this.danger = danger;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Coordinate.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("longitude=" + longitude)
                .add("latitude=" + latitude)
                .add("datetime='" + datetime + "'")
                .add("danger=" + danger)
                .add("description='" + description + "'")
                .add("usrName='" + usrName + "'")
                .toString();
    }

    public enum Danger {DEER, REINDEER, MOOSE, OTHER}
}