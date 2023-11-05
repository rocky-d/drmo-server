package uo.rocky.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.time.Instant;
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

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        Comment.connection = connection;
    }

    public static synchronized List<Comment> selectSQLite(Map<String, String> params) throws Exception {
        // TODO
        return null;
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