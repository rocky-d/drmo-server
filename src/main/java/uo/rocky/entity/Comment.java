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

public final class Comment implements EntityRelatesToJSON, EntityRelatesToSQLite {
    private static Connection connection = null;

    private long id;
    private String content;
    private String datetime;  // TODO: refactor datatype
    private long cdtId;

    public Comment(long id, String content, String datetime, long cdtId) {
        this.id = id;
        this.content = content;
        this.datetime = datetime;
        this.cdtId = cdtId;
    }

    public static Comment valueOf(JSONObject jsonObject) throws JSONException {
        return new Comment(
                Instant.now().toEpochMilli(),
                jsonObject.getString("comment"),
                jsonObject.getString("sent"),
                jsonObject.getLong("id")
        );
    }

    static Connection getConnection() {
        return connection;
    }

    static void setConnection(Connection connection) {
        Comment.connection = connection;
    }

    public static synchronized List<Comment> selectSQLite(Map<String, String> params) throws Exception {
        return EntitySQLiteConnection.selectComment(params);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public long getCdtId() {
        return cdtId;
    }

    public void setCdtId(long cdtId) {
        this.cdtId = cdtId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Comment.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("content=" + (null == content ? null : "'" + content + "'"))
                .add("datetime=" + (null == datetime ? null : "'" + datetime + "'"))
                .add("cdtId=" + cdtId)
                .toString();
    }

    @Override
    public String toJSONString() {
        return new StringJoiner(",", "{", "}")
                .add("\"commentid\"=\"" + id + "\"")
                .add("\"comment\"=" + (null == content ? null : "\"" + content + "\""))
                .add("\"sent\"=" + (null == datetime ? null : "\"" + datetime + "\""))
                .add("\"id\"=\"" + cdtId + "\"")
                .toString();
    }

    @Override
    public synchronized boolean insertSQLite() throws Exception {
        // TODO
//        Class.forName("org.sqlite.JDBC");

        Instant instant = Instant.parse(datetime);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDateTime = localDateTime.format(formatter);

        String query = String.format("INSERT INTO comment" +
                        " (CMT_ID,CMT_CONTENT,CMT_DATETIME,CMT_CDT_ID)" +
                        " VALUES (%s,'%s','%s',%s);",
                id,
                EntityRelatesToSQLite.escapeSingleQuotes(content),
                formattedDateTime,  // TODO
                cdtId
        );
        System.out.println(query);

        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
        connection.commit();

        return true;
    }

    @Override
    public synchronized boolean deleteSQLite() throws Exception {
        // TODO
        return true;
    }

    @Override
    public synchronized boolean updateSQLite() throws Exception {
        // TODO
        return true;
    }
}