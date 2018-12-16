package advenstudios.prostamapav2;

public class User {

    //  @SerializedName("id")
    String id;

    // @SerializedName("firstName")
    String firstName;

    // @SerializedName("lastName")
    String lastName;

    // @SerializedName("password")
    String password;

    // @SerializedName("email")
    String email;
    public User() {
    }

    public User(String id, String name, String lastName, String email, String password ) {
        this.id = id;
        this.firstName = name;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public User(String name, String lastName, String email, String password ) {
        this.firstName = name;
        this.lastName = lastName;
        this.password= password;
        this.email = email;
    }

    public User(String email, String password ) {
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
    public String getEmail(String get_email){
        return email;
    }
}
