package com.example.caroonline.models;

public class Node { //đợi mình nói nè. adpater = data + viewholder. // r bạn sẽ nghĩ tới việc làm sao để lưu đặc điểm cái hhinhf ảnh hiển thị ra.
    int imageId; // bạn nghĩ sẽ cho 1 cái name: nếu name là "0" thì hình 0 "x" thì hình x,......




    public Node(int imageId) {
        this.imageId = imageId;

    }

    public Node(){

    }

    // get moi la get.
    public int getImageId() {
        return imageId;
    }

    // set la set
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

}
