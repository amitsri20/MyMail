package amit.mymails.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 10/15/2016.
 */

public class Participants {
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;

    public Participants(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
