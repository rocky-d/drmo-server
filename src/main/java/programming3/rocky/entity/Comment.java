package programming3.rocky.entity;

public class Comment {
    private long id;
    private String content;
    private long cdtId;

    public Comment(long id, String content, long cdtId) {
        this.id = id;
        this.content = content;
        this.cdtId = cdtId;
    }
}