package com.example.caroonline.models;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String id;
    private String name;
    private List<String> playerIdList;
    private int maxPlayerCount;
    private int status;
    public Room() {

    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getPlayerIdList() {
        return playerIdList;
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

    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public Room(String name, int maxPlayerCount) {
        this.name = name;
        this.maxPlayerCount = maxPlayerCount;
        this.id = Utility.generateRandomString();
        this.playerIdList = new ArrayList<>();
        this.status = Constraints.STATUS_WAITING;
    }

    public void addPlayer(String playerId) {
        if (couldAddPlayer())
            playerIdList.add(playerId);
        updateStatus(playerIdList.size());
    }

    private void updateStatus(int playerCount) {
        if(playerCount == maxPlayerCount)
            status = Constraints.STATUS_READY;
    }

    private boolean couldAddPlayer() {
        int playerCount = playerIdList.size();
        if (playerCount < maxPlayerCount)
            return true;
        return false;
    }

    public int getPlayerCount() {
        return playerIdList.size();
    }
}
