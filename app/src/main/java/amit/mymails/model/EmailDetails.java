package amit.mymails.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amit on 10/16/2016.
 */

public class EmailDetails {
    @SerializedName("subject")
    private String subject;
    @SerializedName("participants")
    private List<Participants> participants;
    @SerializedName("preview")
    private String preview;
    @SerializedName("isRead")
    private String isRead;
    @SerializedName("isStarred")
    private String isStarred;
    @SerializedName("id")
    private String id;
    @SerializedName("body")
    private String body;
    @SerializedName("ts")
    private String ts;

    public EmailDetails(String subject, String ts, String body, String isStarred, String preview, List<Participants> participants, String isRead, String id) {
        this.subject = subject;
        this.ts = ts;
        this.body = body;
        this.isStarred = isStarred;
        this.preview = preview;
        this.participants = participants;
        this.isRead = isRead;
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public List<Participants> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participants> participants) {
        this.participants = participants;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(String isStarred) {
        this.isStarred = isStarred;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
