package advenstudios.prostamapav2;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    int id;

    @SerializedName("firstName")
    String fName;

    @SerializedName("lastName")
    String lastName;

    @SerializedName("password")
    String password;

    @SerializedName("email")
    String email;

    public User(int id, String fName, String lastName, String email, String password ) {
        this.id = id;
        this.fName = fName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public User(String fName, String lastName, String email, String password ) {
        this.fName = fName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public User(String email, String password ) {
        this.password = password;
        this.email = email;
    }

}