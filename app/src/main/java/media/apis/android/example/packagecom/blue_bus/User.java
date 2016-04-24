package media.apis.android.example.packagecom.blue_bus;

/**
 * Created by Ali on 06/02/2016.
 */
public class User {
    String name, username, password;
    int age;

    public User(String name, int age, String username, String password) {
        this.name=name;
        this.age=age;
        this.username=username;
        this.password=password;
    }

    public User(String username, String password) {
        this.username=username;
        this.password=password;
        this.age=-1;
        this.name="";
    }
}
