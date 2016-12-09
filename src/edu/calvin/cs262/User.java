package edu.calvin.cs262;

/**
 * Created by elc3 on 11/14/2016.
 */
public class User {

    private int ID;
    private String Username;

    User() { }

    User(Integer ID, String Username) {
        this.ID = ID;
        this.Username = Username;
    }

    User(String Username) {
        this.ID = 0;
        this.Username = Username;
    }

    public int getID() { return ID; }

    public void setId(Integer new_id) { this.ID = new_id; }

    public String getUsername() { return Username; }
}
