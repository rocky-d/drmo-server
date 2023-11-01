package programming3.rocky.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.StringJoiner;

public class Comment {
    private long id;
    private String content;
    private long cdtId;

    public Comment(long id, String content, long cdtId) {
        this.id = id;
        this.content = content;
        this.cdtId = cdtId;
    }

    public Comment(JSONObject jsonObject) throws JSONException {
        id = Instant.now().toEpochMilli();
        content = jsonObject.getString("comment");
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
                .add("cdtId=" + cdtId)
                .toString();
    }
}