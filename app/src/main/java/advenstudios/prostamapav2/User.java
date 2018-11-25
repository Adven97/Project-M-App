package advenstudios.prostamapav2;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("lastName")
    String lastName;

    @SerializedName("passwordHash")
    String passwordHash;

    @SerializedName("email")
    String email;

    public User(int id, String name, String lastName, String email, String password ) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.passwordHash = password;
        this.email = email;
    }

    public User(String name, String lastName, String email, String password ) {
        this.name = name;
        this.lastName = lastName;
        this.passwordHash = password;
        this.email = email;
    }

    public User(String email, String password ) {
        this.passwordHash = password;
        this.email = email;
    }

}