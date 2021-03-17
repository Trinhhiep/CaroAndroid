package com.example.caroonline.models;

import com.example.caroonline.Constraints;
import com.example.caroonline.FirebaseSingleton;
import com.example.caroonline.Utility;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String id;
    private String name;
    private String admin;
    private String other;
    private int status;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Room() {

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room(String name, String admin) {
        this.name = name;
        this.id = Utility.generateRandomString();
        this.admin = admin;
        this.status = Constraints.STATUS_WAITING;
        this.other = "";
    }

    public void add(String playerName) {
        if (couldAddPlayer()) {
            this.other = playerName; //  them vo  chac chan la khach nha ban
        }
        setStatus(Constraints.STATUS_READY);
    }

    public boolean couldAddPlayer() {
        if (other.isEmpty()) // ok k ban.string mà cung có isEmpty dung r  bangh e
            return true;
        return false;
    }

    //  xu  li xoa r ne ban.haha vl ban
    public void remove(String playerName) {
        //lam  remove coi ban
        // neu thang remove la admin thi ban  lam gi
        if (this.admin.compareTo(playerName) == 0)// bạn đã xử lí đổi admin set other
            this.admin = other;
        this.other = ""; //  hay.
        this.setStatus(Constraints.STATUS_WAITING);
    }

    public boolean couldDestroy(){
        if(this.admin.isEmpty())
            return  true;
        return false;
    }

    public int getPlayerCount() {
        if (other.isEmpty())
            return 1;
        return 2;
    }

    //  xong  room r. gio qua may class khac.
}
