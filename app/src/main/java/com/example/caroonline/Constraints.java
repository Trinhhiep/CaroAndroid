package com.example.caroonline;

import android.graphics.Color;

public class Constraints {
    public static final int STATUS_WAITING = 0;
    public static final int STATUS_PLAYING = 1;
    public static final int STATUS_READY = 2;
    public static final int SPAN_COUNT_ITEM_NODE = 16; // lưu đây r. thì từ position bạn quy ra tọa độ để xét thắng thua easy r.
    public static final int PLAYER_COLOR = Color.parseColor("#ff008000");
    public static final int COLOR_BLACK = Color.parseColor("#000000");
    public static final int COLOR_GREEN = Color.parseColor("#ff99cc00");
    public static final int COLOR_WHITE = Color.parseColor("#ffffff");
    public static final int  COUNT_ITEM_IMAGE=400;
    public static final int COLUMN_COUNT_ITEM_IMAGE =20;
    public static final int ROW_COUNT_ITEM_IMAGE =COUNT_ITEM_IMAGE/COLUMN_COUNT_ITEM_IMAGE;
    public static final int IMAGE_ID_O = 1;
    public static final int IMAGE_ID_X = 2;
    public static final int IMAGE_ID_NULL = 0;

    public static String roomStatus(int status){
        if(status == STATUS_WAITING)
            return "waiting";
        else if(status == STATUS_PLAYING)
            return "playing";
        return "ready";
    }
}
