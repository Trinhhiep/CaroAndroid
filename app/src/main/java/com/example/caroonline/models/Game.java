package com.example.caroonline.models;

import java.util.List;

public class Game {
    String roomId;
    int currentPlayer;
    int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }



    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Game(String roomId,int currentPlayer,int status) {
        this.roomId = roomId;
        this.status = status;

this.currentPlayer=currentPlayer;
    }
}
