package com.example.caroonline.models;

import java.util.ArrayList;
import java.util.List;

public class Game {
    String roomId;
    List<Node> listNode ;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<Node> getListNode() {
        return listNode;
    }

    public void setListNode(List<Node> listNode) {
        this.listNode = listNode;
    }
    public Game(String roomId, ArrayList<Node> nodes) {
        this.roomId = roomId;
        this.listNode = new ArrayList<>();

    }
}
