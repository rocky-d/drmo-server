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
public final class Comment extends EntityBase {

    private long id;
    private String content;
    private LocalDateTime localdatetime;
    private String datetimeoffset;
    private long cdtId;

    public Comment(long id, String content, LocalDateTime localdatetime, String datetimeoffset, long cdtId) {
        this.id = id;
        this.content = content;
        this.localdatetime = localdatetime;
        this.datetimeoffset = datetimeoffset;
        this.cdtId = cdtId;
    }

    public static Comment valueOf(JSONObject jsonObject) {
        Instant.parse(jsonObject.getString("sent"));
        return new Comment(
                Instant.now().toEpochMilli(),
                jsonObject.getString("comment"),
                LocalDateTime.parse(jsonObject.getString("sent").substring(0, 23), LOCALDATETIME_FORMATTER_T),
                23 < jsonObject.getString("sent").length() ? jsonObject.getString("sent").substring(23) : "",
                jsonObject.getLong("id")
        );
    }

    public static Comment valueOf(ResultSet resultSet) throws SQLException {
        return new Comment(
                resultSet.getLong("CMT_ID"),
                resultSet.getString("CMT_CONTENT"),
                LocalDateTime.parse(resultSet.getString("CDT_LOCALDATETIME"), LOCALDATETIME_FORMATTER_SPACE),
                resultSet.getString("CDT_DATETIMEOFFSET"),
                resultSet.getLong("CMT_CDT_ID")
        );
    }

    public static boolean insertComment(Comment comment) throws SQLException {
        return comment.insertSQL();
    }

    public static boolean deleteComment() throws SQLException {
        return false;
    }

    public static boolean updateComment() throws SQLException {
        return false;
    }

    public static List<Comment> selectCommentList(Map<String, String> params) throws SQLException {
        return EntitySQLConnection.selectComments(params);
    }

    public static String selectCommentJSONString(Map<String, String> params) throws SQLException {
        return EntitySQLConnection.selectComments(params).stream().map(Comment::toJSONString).collect(Collectors.joining(",", "[", "]"));
    }

    public static JSONArray selectCommentJSONArray(Map<String, String> params) throws SQLException {
        return EntitySQLConnection.selectComments(params).stream().map(Comment::toJSONObject).collect(JSONArray::new, JSONArray::put, JSONArray::put);
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

    public long getCdtId() {
        return cdtId;
    }

    public void setCdtId(long cdtId) {
        this.cdtId = cdtId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Comment.class.getSimpleName() + "{", "}")
                .add("id=" + id)
                .add("content=" + (null == content ? null : "'" + content + "'"))
                .add("localdatetime=" + (null == localdatetime ? "null" : "'" + localdatetime + "'"))
                .add("datetimeoffset=" + (null == datetimeoffset ? "null" : "'" + datetimeoffset + "'"))
                .add("cdtId=" + cdtId)
                .toString();
    }

    @Override
    public String toJSONString() {
        return new StringJoiner(",", "{", "}")
                .add("\"commentid\":\"" + id + "\"")
                .add("\"comment\":" + EntityRelatesToJSON.escapeDoubleQuotes(content))
                .add("\"sent\":" + EntityRelatesToJSON.escapeDoubleQuotes(localdatetime.format(LOCALDATETIME_FORMATTER_T) + datetimeoffset))
                .add("\"id\":\"" + cdtId + "\"")
                .toString();
    }

    @Override
    public synchronized boolean insertSQL() throws SQLException {
        if (!EntitySQLConnection.selectComments(Stream.of(new String[]{"QUERY", "COMMENTID"}, new String[]{"COMMENTID", String.valueOf(id)}).collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]))).isEmpty()) {
            return false;
        }
//        if (EntitySQLConnection.selectCoordinates(Stream.of(new String[]{"QUERY", "ID"}, new String[]{"ID", String.valueOf(cdtId)}).collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]))).isEmpty()) {
//            return false;
//        }

        String sql = String.format("INSERT INTO comment" +
                        " (CMT_ID,CMT_CONTENT,CMT_LOCALDATETIME,CMT_DATETIMEOFFSET,CMT_CDT_ID)" +
                        " VALUES (%s,%s,%s,%s,%s);",
                id,
                EntityRelatesToSQL.escapeSingleQuotes(content),
                EntityRelatesToSQL.escapeSingleQuotes(localdatetime.format(LOCALDATETIME_FORMATTER_SPACE)),
                EntityRelatesToSQL.escapeSingleQuotes(datetimeoffset),
                cdtId
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
}