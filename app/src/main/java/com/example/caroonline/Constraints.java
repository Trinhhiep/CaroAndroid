package com.example.caroonline;

import android.graphics.Color;

public class Constraints {
    public static final int STATUS_WAITING = 0;
    public static final int STATUS_PLAYING = 1;
    public static final int STATUS_READY = 2;
    public static final int SPAN_COUNT_ITEM_NODE = 16; // lưu đây r. thì từ position bạn quy ra tọa độ để xét thắng thua easy r.
    public static final int PLAYER_COLOR = Color.parseColor("#ff008000");
    public static final int  SPAN_COUNT_ITEM_IMAGE=15;
    public static final int IMAGE_ID_O = 0;
    public static final int IMAGE_ID_X = 1;
    public static final int IMAGE_ID_NULL = 2;

    public static String roomStatus(int status){
        if(status == STATUS_WAITING)
            return "waiting";
        else if(status == STATUS_PLAYING)
            return "playing";
        return "ready";
    }
}
