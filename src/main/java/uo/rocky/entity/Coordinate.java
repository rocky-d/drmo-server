package uo.rocky.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TOWRITE
 * <p>
 * Extends {@link EntityBase}.
 *
 * @author Rocky Haotian Du
 */
public final class Coordinate extends EntityBase {

    private long id;
    private double longitude;
    private double latitude;
    private LocalDateTime localdatetime;
    private String datetimeoffset;
    private Dangertype dangertype;
    private String description;
    private String usrName;

    public Coordinate(long id, double longitude, double latitude, LocalDateTime localdatetime, String datetimeoffset, Dangertype dangertype, String description, String usrName) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.localdatetime = localdatetime;
        this.datetimeoffset = datetimeoffset;
        this.dangertype = dangertype;
        this.description = description;
        this.usrName = usrName;
    }

    public static Coordinate valueOf(JSONObject jsonObject) {
        OFFSETDATETIME_FORMATTER.parse(jsonObject.getString("sent"));
        return new Coordinate(
                Instant.now().toEpochMilli(),
                jsonObject.getDouble("longitude"),
                jsonObject.getDouble("latitude"),
                LocalDateTime.parse(jsonObject.getString("sent").substring(0, 23), LOCALDATETIME_FORMATTER_T),
                23 < jsonObject.getString("sent").length() ? jsonObject.getString("sent").substring(23) : "",
                Dangertype.valueOf(jsonObject.getString("dangertype").toUpperCase()),
                jsonObject.has("description") ? jsonObject.getString("description") : null,
                jsonObject.getString("username")
        );
    }

    public static Coordinate valueOf(ResultSet resultSet) throws SQLException {
        return new Coordinate(
                resultSet.getLong("CDT_ID"),
                resultSet.getDouble("CDT_LONGITUDE"),
                resultSet.getDouble("CDT_LATITUDE"),
                LocalDateTime.parse(resultSet.getString("CDT_LOCALDATETIME"), LOCALDATETIME_FORMATTER_SPACE),
                resultSet.getString("CDT_DATETIMEOFFSET"),
                Dangertype.valueOf(resultSet.getString("CDT_DANGERTYPE").toUpperCase()),
                resultSet.getString("CDT_DESCRIPTION"),
                resultSet.getString("CDT_USR_NAME")
        );
    }

    public static boolean insertCoordinate(Coordinate coordinate) throws SQLException {
        return coordinate.insertSQL();
    }

    public static boolean deleteCoordinate() throws SQLException {
        return false;
    }

    public static boolean updateCoordinate() throws SQLException {
        return false;
    }

    public static List<Coordinate> selectCoordinateList(Map<String, String> params) throws SQLException {
        return EntitySQLConnection.selectCoordinates(params);
    }

    public static String selectCoordinateJSONString(Map<String, String> params) throws SQLException {
        return EntitySQLConnection.selectCoordinates(params).stream().map(Coordinate::toJSONString).collect(Collectors.joining(",", "[", "]"));
    }

    public static JSONArray selectCoordinateJSONArray(Map<String, String> params) throws SQLException {
        return EntitySQLConnection.selectCoordinates(params).stream().map(Coordinate::toJSONObject).collect(JSONArray::new, JSONArray::put, JSONArray::put);
    }

    public static String selectCoordinateWithCommentsJSONString(Map<String, String> params) throws SQLException {
        StringJoiner stringJoiner = new StringJoiner(",", "[", "]");
        for (Coordinate coordinate : EntitySQLConnection.selectCoordinates(params)) {
            stringJoiner.add(coordinate.toJSONStringWithComments());
        }
        return stringJoiner.toString();
    }

    public static JSONArray selectCoordinateWithCommentsJSONArray(Map<String, String> params) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        for (Coordinate coordinate : EntitySQLConnection.selectCoordinates(params)) {
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

    public LocalDateTime getLocaldatetime() {
        return localdatetime;
    }

    public void setLocaldatetime(LocalDateTime localdatetime) {
        this.localdatetime = localdatetime;
    }

    public String getDatetimeoffset() {
        return datetimeoffset;
    }

    public void setDatetimeoffset(String datetimeoffset) {
        this.datetimeoffset = datetimeoffset;
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
                .add("localdatetime=" + (null == localdatetime ? "null" : "'" + localdatetime + "'"))
                .add("datetimeoffset=" + (null == datetimeoffset ? "null" : "'" + datetimeoffset + "'"))
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
                .add("\"sent\":" + EntityRelatesToJSON.escapeDoubleQuotes(localdatetime.format(LOCALDATETIME_FORMATTER_T) + datetimeoffset))
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
                .add("\"sent\":" + EntityRelatesToJSON.escapeDoubleQuotes(localdatetime.format(LOCALDATETIME_FORMATTER_T) + datetimeoffset))
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
        if (!EntitySQLConnection.selectCoordinates(Stream.of(new String[]{"QUERY", "ID"}, new String[]{"ID", String.valueOf(id)}).collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]))).isEmpty()) {
            return false;
        }
//        if (EntitySQLConnection.selectUsers(Stream.of(new String[]{"QUERY", "USERNAME"}, new String[]{"USERNAME", usrName}).collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]))).isEmpty()) {
//            return false;
//        }

        String sql = String.format("INSERT INTO coordinate" +
                        " (CDT_ID,CDT_LONGITUDE,CDT_LATITUDE,CDT_LOCALDATETIME,CDT_DATETIMEOFFSET,CDT_DANGERTYPE,CDT_DESCRIPTION,CDT_USR_NAME)" +
                        " VALUES (%s,%s,%s,%s,%s,%s,%s,%s);",
                id,
                longitude,
                latitude,
                EntityRelatesToSQL.escapeSingleQuotes(localdatetime.format(LOCALDATETIME_FORMATTER_SPACE)),
                EntityRelatesToSQL.escapeSingleQuotes(datetimeoffset),
                EntityRelatesToSQL.escapeSingleQuotes(dangertype.name()),
                EntityRelatesToSQL.escapeSingleQuotes(description),
                EntityRelatesToSQL.escapeSingleQuotes(usrName)
        );
//        System.out.println(sql);

        Statement statement = getConnection().createStatement();
        statement.executeUpdate(sql);
        statement.close();
        getConnection().commit();
        return true;
    }

    @Override
    public synchronized boolean deleteSQL() throws SQLException {
        return false;
    }

    @Override
    public synchronized boolean updateSQL() throws SQLException {
        return false;
    }

    /**
     * TOWRITE
     *
     * @author Rocky Haotian Du
     */
    public enum Dangertype {

        DEER(),
        REINDEER(),
        MOOSE(),
        OTHER();

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