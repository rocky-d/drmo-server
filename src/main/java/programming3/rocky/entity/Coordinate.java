package programming3.rocky.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

public final class Coordinate implements RelateToJSON, RelateToSQLite {
    private long id;
    private double longitude;
    private double latitude;
    private String datetime;  // TODO: refactor datatype
    private Dangertype dangertype;
    private String description;
    private String usrName;

    public Coordinate(long id, double longitude, double latitude, String datetime, Dangertype dangertype, String usrName, String description) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.datetime = datetime;
        this.dangertype = dangertype;
        this.usrName = usrName;
        this.description = description;
    }

    public Coordinate(JSONObject jsonObject) throws JSONException {
        id = Instant.now().toEpochMilli();
        longitude = jsonObject.getDouble("longitude");
        latitude = jsonObject.getDouble("latitude");
        datetime = jsonObject.getString("sent");
        dangertype = Dangertype.valueOf(jsonObject.getString("dangertype").toUpperCase());
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

    public Dangertype getDangertype() {
        return dangertype;
    }

    public void setDangertype(Dangertype dangertype) {
        this.dangertype = dangertype;
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
                .add("dangertype=" + dangertype)
                .add("description='" + description + "'")
                .add("usrName='" + usrName + "'")
                .toString();
    }

    @Override
    public String toJSONString() {
        return new StringJoiner(",", "{", "}")
                .add("\"id\"=\"" + id + "\"")
                .add("\"longitude\"=\"" + longitude + "\"")
                .add("\"latitude\"=\"" + latitude + "\"")
                .add("\"datetime\"=\"" + datetime + "\"")
                .add("\"dangertype\"=\"" + dangertype + "\"")
                .add("\"description\"=\"" + description + "\"")
                .add("\"username\"=\"" + usrName + "\"")
                .toString();
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        return RelateToJSON.super.toJSONObject();
    }

    @Override
    public synchronized void insertWithSQLite() throws Exception {
        // TODO

        Class.forName("org.sqlite.JDBC");

        Connection connection = DriverManager.getConnection("jdbc:sqlite:coursework.sqlite.db");
        connection.setAutoCommit(false);
        System.out.println("jdbc:sqlite:coursework.sqlite.db opened");
        Statement statement = connection.createStatement();

        Instant instant = Instant.parse(datetime);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // 转换为SQLite数据库中datetime字段的格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDateTime = localDateTime.format(formatter);

        String query = String.format(
                "INSERT INTO coordinate (CDT_ID, CDT_LONGITUDE, CDT_LATITUDE, CDT_DATETIME, CDT_DANGERTYPE, CDT_DESCRIPTION, CDT_USR_NAME) "
                        + "VALUES (%s, %s, %s, '%s', '%s', %s, '%s')",
                id, longitude, latitude, formattedDateTime, dangertype, description, usrName);
        System.out.println(query);
        statement.executeUpdate(query);

        statement.close();
        connection.commit();
        connection.close();
    }

    @Override
    public void deleteWithSQLite() throws Exception {
        // TODO
    }

    @Override
    public void updateWithSQLite() throws Exception {
        // TODO
    }

    @Override
    public void selectWithSQLite() throws Exception {
        // TODO
    }

    public enum Dangertype {
        DEER, REINDEER, MOOSE, OTHER;
    }
}