package uo.rocky.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Coordinate extends EntityBase {
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

    public static Coordinate valueOf(JSONObject jsonObject) throws JSONException, IllegalArgumentException {
        return new Coordinate(
                Instant.now().toEpochMilli(),
                jsonObject.getDouble("longitude"),
                jsonObject.getDouble("latitude"),
                jsonObject.getString("sent"),
                Dangertype.valueOf(jsonObject.getString("dangertype").toUpperCase()),
                jsonObject.has("description") ? jsonObject.getString("description") : null,
                jsonObject.getString("username")
        );
    }

    public static Coordinate valueOf(ResultSet resultSet) throws SQLException, IllegalArgumentException {
        return new Coordinate(
                resultSet.getLong("CDT_ID"),
                resultSet.getDouble("CDT_LONGITUDE"),
                resultSet.getDouble("CDT_LATITUDE"),
                resultSet.getString("CDT_DATETIME"),
                Dangertype.valueOf(resultSet.getString("CDT_DANGERTYPE")),
                resultSet.getString("CDT_DESCRIPTION"),
                resultSet.getString("CDT_USR_NAME")
        );
    }

    static Connection getConnection() {
        return connection;
    }

    static void setConnection(Connection connection) {
        Coordinate.connection = connection;
    }

    public static synchronized boolean insertCoordinate(Coordinate coordinate) throws SQLException {
        return coordinate.insertSQL();
    }

    public static synchronized boolean deleteCoordinate() throws SQLException {
        return false;
    }

    public static synchronized boolean updateCoordinate() throws SQLException {
        return false;
    }

    public static synchronized List<Coordinate> selectCoordinateList(Map<String, String> params) throws SQLException {
        return EntityDBConnection.selectCoordinates(params);
    }

    public static synchronized String selectCoordinateJSONString(Map<String, String> params) throws SQLException {
        return EntityDBConnection.selectCoordinates(params).stream().map(Coordinate::toJSONString).collect(Collectors.joining(",", "[", "]"));
    }

    public static synchronized JSONArray selectCoordinateJSONArray(Map<String, String> params) throws SQLException {
        return EntityDBConnection.selectCoordinates(params).stream().map(Coordinate::toJSONObject).collect(JSONArray::new, JSONArray::put, JSONArray::put);
    }

    public static synchronized String selectCoordinateWithCommentsJSONString(Map<String, String> params) throws SQLException {
        StringJoiner stringJoiner = new StringJoiner(",", "[", "]");
        for (Coordinate coordinate : EntityDBConnection.selectCoordinates(params)) {
            stringJoiner.add(coordinate.toJSONStringWithComments());
        }
        return stringJoiner.toString();
    }

    public static synchronized JSONArray selectCoordinateWithCommentsJSONArray(Map<String, String> params) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        for (Coordinate coordinate : EntityDBConnection.selectCoordinates(params)) {
            jsonArray.put(coordinate.toJSONObjectWithComments());
        }
        return jsonArray;
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
                .add("\"id\":\"" + id + "\"")
                .add("\"longitude\":\"" + longitude + "\"")
                .add("\"latitude\":\"" + latitude + "\"")
                .add("\"sent\":" + EntityRelatesToJSON.escapeDoubleQuotes(datetime))
                .add("\"dangertype\":" + EntityRelatesToJSON.escapeDoubleQuotes(dangertype.name()))
                .add("\"description\":" + EntityRelatesToJSON.escapeDoubleQuotes(description))
                .add("\"username\":" + EntityRelatesToJSON.escapeDoubleQuotes(usrName))
                .toString();
    }

    public String toJSONStringWithComments() throws SQLException {
        return new StringJoiner(",", "{", "}")
                .add("\"id\":\"" + id + "\"")
                .add("\"longitude\":\"" + longitude + "\"")
                .add("\"latitude\":\"" + latitude + "\"")
                .add("\"sent\":" + EntityRelatesToJSON.escapeDoubleQuotes(datetime))
                .add("\"dangertype\":" + EntityRelatesToJSON.escapeDoubleQuotes(dangertype.name()))
                .add("\"description\":" + EntityRelatesToJSON.escapeDoubleQuotes(description))
                .add("\"username\":" + EntityRelatesToJSON.escapeDoubleQuotes(usrName))
                .add("\"comments\":" + Comment.selectCommentJSONString(Stream.of(new String[]{"QUERY", "ID"}, new String[]{"ID", String.valueOf(id)}).collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]))))
                .toString();
    }

    public JSONObject toJSONObjectWithComments() throws SQLException {
        return new JSONObject(toJSONStringWithComments());
    }

    @Override
    public synchronized boolean insertSQL() throws SQLException {
        // TODO
//        Class.forName("org.sqlite.JDBC");

        Instant instant = Instant.parse(datetime);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDateTime = localDateTime.format(formatter);

        String sql = String.format("INSERT INTO coordinate" +
                        " (CDT_ID,CDT_LONGITUDE,CDT_LATITUDE,CDT_DATETIME,CDT_DANGERTYPE,CDT_DESCRIPTION,CDT_USR_NAME)" +
                        " VALUES (%s,%s,%s,%s,%s,%s,%s);",
                id,
                longitude,
                latitude,
                EntityRelatesToSQL.escapeSingleQuotes(formattedDateTime),
                EntityRelatesToSQL.escapeSingleQuotes(dangertype.name()),
                EntityRelatesToSQL.escapeSingleQuotes(description),
                EntityRelatesToSQL.escapeSingleQuotes(usrName)
        );
        System.out.println(sql);

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
        connection.commit();

        return true;
    }

    @Override
    public synchronized boolean deleteSQL() throws SQLException {
        // TODO
        return true;
    }

    @Override
    public synchronized boolean updateSQL() throws SQLException {
        // TODO
        return true;
    }

    public enum Dangertype {
        DEER,
        REINDEER,
        MOOSE,
        OTHER;

        Dangertype() {
        }

        @Override
        public final String toString() {
            return new StringJoiner(", ", Dangertype.class.getSimpleName() + "{", "}")
                    .add("name='" + name() + "'")
                    .add("ordinal=" + ordinal())
                    .toString();
        }
    }
}