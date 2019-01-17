package advenstudios.prostamapav2;

import com.google.gson.annotations.SerializedName;

public class User2 {

    @SerializedName("id")
    String id;

    @SerializedName("firstName")
    String firstName;

    @SerializedName("lastName")
    String lastName;

    @SerializedName("password")
    String password;

    @SerializedName("usrstatus")
    String status;

    @SerializedName("email")
    String email;
    public User2() {
    }

    public User2(String id, String name, String lastName, String email, String password ) {
        this.id = id;
        this.firstName = name;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public User2(String name, String lastName, String email, String status) {
        this.firstName = name;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
    }

    public User2(String email, String password ) {
        this.password= password;
        this.email = email;
    }

    //-------------------    setery -----------------------------

    public void setId(String id){
        this.id = id;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setStatus(String status){
        this.status = status;
    }

    //-------------------    getery -----------------------------

    public String getId(){
        return id;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getPassword(){
        return password;
    }
    public String getEmail(){
        return email;
    }
    public String getStatus(){
        return status;
    }
}
