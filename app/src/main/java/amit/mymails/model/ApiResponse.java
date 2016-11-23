package amit.mymails.model;

/**
 * Created by amit on 9/25/2016.
 */

public class ApiResponse {
    Email emails;

    public ApiResponse(Email emails) {
        this.emails = emails;
    }

    public Email getEmails() {
        return emails;
    }

    public void setEmails(Email emails) {
        this.emails = emails;
    }
}
