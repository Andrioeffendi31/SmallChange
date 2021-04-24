package id.ac.umn.leleair.kelompok.smallchange.Model;

public class User {
    public String id;
    public String username;
    /*public int userImg;*/

    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
