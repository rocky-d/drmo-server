package uo.rocky.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public final class Coordinate extends EntityBase implements EntityRelatesToJSON, EntityRelatesToSQLite {
    private static Connection connection = null;

    private long id;
    private double longitude;
    private double latitude;
    private String datetime;  // TODO: refactor datatype
    private Dangertype dangertype;
    private String description;
    private String usrName;

    public Coordinate(long id, double longitude, double latitude, String datetime, Dangertype dangertype, String description, String usrName) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.datetime = datetime;
        this.dangertype = dangertype;
        this.description = description;
        this.usrName = usrName;
    }

    public static Coordinate valueOf(JSONObject jsonObject) throws JSONException {
        return new Coordinate(
                Instant.now().toEpochMilli(),
                jsonObject.getDouble("longitude"),
                jsonObject.getDouble("latitude"),
                jsonObject.getString("sent"),
                Dangertype.valueOf(jsonObject.getString("dangertype").toUpperCase()),  // TODO: throws Exception
                jsonObject.has("description") ? jsonObject.getString("description") : null,
                jsonObject.getString("username")
        );
    }

    static Connection getConnection() {
        return connection;
    }

    static void setConnection(Connection connection) {
        Coordinate.connection = connection;
    }

    public static synchronized boolean insertCoordinate(Coordinate coordinate) throws Exception {
        return coordinate.insertSQL();
    }

    public static synchronized boolean deleteCoordinate() throws Exception {
        return false;
    }

    public static synchronized boolean updateCoordinate() throws Exception {
        return false;
    }

    public static synchronized List<Coordinate> selectCoordinate(Map<String, String> params) throws Exception {
        return EntitySQLiteConnection.selectCoordinate(params);
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
        return new StringJoiner(", ", Coordinate.class.getSimpleName() + "{", "}")
                .add("id=" + id)
                .add("longitude=" + longitude)
                .add("latitude=" + latitude)
                .add("datetime=" + (null == datetime ? "null" : "'" + datetime + "'"))
                .add("dangertype=" + (null == dangertype ? "null" : dangertype))
                .add("description=" + (null == description ? "null" : "'" + description + "'"))
                .add("usrName=" + (null == usrName ? "null" : "'" + usrName + "'"))
                .toString();
    }

    @Override
    public String toJSONString() {
        return new StringJoiner(",", "{", "}")
                .add("\"id\"=\"" + id + "\"")
                .add("\"longitude\"=\"" + longitude + "\"")
                .add("\"latitude\"=\"" + latitude + "\"")
                .add("\"sent\"=" + EntityRelatesToJSON.escapeDoubleQuotes(datetime))
                .add("\"dangertype\"=" + EntityRelatesToJSON.escapeDoubleQuotes(dangertype.name()))
                .add("\"description\"=" + EntityRelatesToJSON.escapeDoubleQuotes(description))
                .add("\"username\"=" + EntityRelatesToJSON.escapeDoubleQuotes(usrName))
                .toString();
    }

    @Override
    public synchronized boolean insertSQL() throws Exception {
        // TODO
//        Class.forName("org.sqlite.JDBC");

        Instant instant = Instant.parse(datetime);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDateTime = localDateTime.format(formatter);

        String query = String.format("INSERT INTO coordinate" +
                        " (CDT_ID,CDT_LONGITUDE,CDT_LATITUDE,CDT_DATETIME,CDT_DANGERTYPE,CDT_DESCRIPTION,CDT_USR_NAME)" +
                        " VALUES (%s,%s,%s,%s,%s,%s,%s);",
                id,
                longitude,
                latitude,
                EntityRelatesToSQLite.escapeSingleQuotes(formattedDateTime),
                EntityRelatesToSQLite.escapeSingleQuotes(dangertype.name()),
                EntityRelatesToSQLite.escapeSingleQuotes(description),
                EntityRelatesToSQLite.escapeSingleQuotes(usrName)
        );
        System.out.println(query);

        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
        connection.commit();

        return true;
    }

    @Override
    public synchronized boolean deleteSQL() throws Exception {
        // TODO
        return true;
    }

    @Override
    public synchronized boolean updateSQL() throws Exception {
        // TODO
        return true;
    }

    public enum Dangertype {
        DEER, REINDEER, MOOSE, OTHER;
    }
}