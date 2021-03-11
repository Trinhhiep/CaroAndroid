package com.example.caroonline.models;

import com.example.caroonline.FirebaseSingleton;

public class Player {
    private String name;
    private boolean isAdmin;

    public Player(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Player(String name, boolean isAdmin) {
        this.name = name;
        this.isAdmin = isAdmin;
    }
}
