package br.com.nwms.myapplication;

import java.util.ArrayList;

public class User {

    String username, password, listaDisciplinas;

    public User(String username, String password, String listaDisciplinas) {
        this.username = username;
        this.password = password;
        this.listaDisciplinas = listaDisciplinas;
    }

    public User(String username, String password) {
        this(username, password, "");
    }
}
