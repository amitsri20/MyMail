package amit.mymails.model;

/**
 * Created by amit on 10/15/2016.
 */

public class Email {
    private String subject;
    private String participants;
    private String preview;
    private String isRead;
    private long ts;
    private String id;

    public Email(String subject, String participants, String isRead, long ts, String id, String preview) {
        this.subject = subject;
        this.participants = participants;
        this.isRead = isRead;
        this.ts = ts;
        this.id = id;
        this.preview = preview;
    }

    public String getSubject() {
        return subject;
    }

    public String getParticipants() {
        return participants;
    }

    public String getIsRead() {
        return isRead;
    }

    public long getTs() {
        return ts;
    }

    public String getId() {
        return id;
    }

    public String getPreview() {
        return preview;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }
}
