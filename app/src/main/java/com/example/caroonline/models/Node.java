package com.example.caroonline.models;

import android.graphics.Point;
import android.widget.ImageView;

public class Node {
    String playerName;
    Point point;
    ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Node(String playerName, Point point, ImageView imageView) {
        this.playerName = playerName;
        this.point = point;
        this.imageView = imageView;
    }
    public Node(){

    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }


}
