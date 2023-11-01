package programming3.rocky.entity;

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