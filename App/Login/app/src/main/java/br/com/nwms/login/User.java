package br.com.nwms.login;

public class User {

    String name, username, password;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this("", username, password);
        //this("", -1, username, password);
    }
}
