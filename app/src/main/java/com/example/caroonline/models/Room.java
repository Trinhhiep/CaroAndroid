package com.example.caroonline.models;

import com.example.caroonline.Constraints;
import com.example.caroonline.FirebaseSingleton;
import com.example.caroonline.Utility;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String id;
    private String name;
    private List<Player> playerList;
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

    public List<Player> getPlayerList() {
        return playerList;
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
        this.playerList = new ArrayList<>();
        this.status = Constraints.STATUS_WAITING;
    }

    public void add(Player player) {
        if (couldAddPlayer()){
            playerList.add(player);
        }
        updateStatus(playerList.size());
    }

    private void updateStatus(int playerCount) {
        if (playerCount == maxPlayerCount) {
            status = Constraints.STATUS_READY;
            return;
        }
        status = Constraints.STATUS_WAITING;
    }

    public boolean couldAddPlayer() {
        if (this.getPlayerCount() < maxPlayerCount)
            return true;
        return false;
    }

    public void remove(String playerName) {
        boolean removePlayerIsAdmin = false;
        for (Player player : playerList) {
            if (player.getName().compareTo(playerName) == 0) {
                if (player.isAdmin())
                    removePlayerIsAdmin = true;
                playerList.remove(player);
                if (removePlayerIsAdmin)
                    updateAdmin();
                updateStatus(playerList.size());
                return;
            }
        }
        // khi 1 thang roi, neu no la chu phong thi dua chu ohong cho thang con lai, con k thi thoi.
    }

    private void updateAdmin() {
        for (Player player : playerList
        ) {
            player.setAdmin(true);
            return;
        }
    }

    public int getPlayerCount() {
        return playerList.size();
    }
}
