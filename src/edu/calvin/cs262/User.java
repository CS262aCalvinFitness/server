package edu.calvin.cs262;

/**
 * Created by elc3 on 11/14/2016.
 */
public class User {

    private int ID;
    private String Username;

    User() { }

    User(int ID, String Username) {
        this.ID = ID;
        this.Username = Username;
    }

    public int getID() { return ID; }

    public String getUsername() { return Username; }
}
