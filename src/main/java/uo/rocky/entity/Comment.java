package uo.rocky.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.StringJoiner;

public final class Comment implements RelatesToJSON, RelatesToSQLite {
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

    public Comment(JSONObject jsonObject) throws JSONException {
        id = Instant.now().toEpochMilli();
        content = jsonObject.getString("comment");
        datetime = jsonObject.getString("sent");
        cdtId = jsonObject.getLong("id");
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
                .add("content='" + content + "'")
                .add("datetime='" + datetime + "'")
                .add("cdtId=" + cdtId)
                .toString();
    }

    @Override
    public String toJSONString() {
        return new StringJoiner(",", "{", "}")
                .add("\"commentid\"=\"" + id + "\"")
                .add("\"comment\"=\"" + content + "\"")
                .add("\"sent\"=\"" + datetime + "\"")
                .add("\"id\"=\"" + cdtId + "\"")
                .toString();
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        return RelatesToJSON.super.toJSONObject();
    }

    @Override
    public synchronized void insertSQLite() throws Exception {
        // TODO
    }

    @Override
    public synchronized void deleteSQLite() throws Exception {
        // TODO
    }

    @Override
    public synchronized void updateSQLite() throws Exception {
        // TODO
    }

    @Override
    public synchronized void selectSQLite() throws Exception {
        // TODO
    }
}