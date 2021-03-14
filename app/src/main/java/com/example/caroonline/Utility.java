package com.example.caroonline;

import android.content.Context;
import android.util.DisplayMetrics;

import com.example.caroonline.Constraints;

import java.util.Random;

public class Utility {
    private static final String CHAR_LIST = "0123456789";
    private static final int RANDOM_STRING_LENGTH = 8;

    public static int widthPixelsDp(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
    public static int widthOfImage(Context context) {
        return (widthPixelsDp(context) / Constraints.SPAN_COUNT_ITEM_IMAGE) - 2;
    }
    public static int widthOfNode(Context context) {
        return (widthPixelsDp(context) / Constraints.SPAN_COUNT_ITEM_NODE) - 1;
    }

    public static String generateRandomString() // random chuoi string ne ok
    {
        StringBuffer randStr = new StringBuffer();
        for(int i=0; i<RANDOM_STRING_LENGTH; i++)
        {
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    public static int getRandomNumber()
    {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1)
        {
            return randomInt;
        }
        else
        {
            return randomInt - 1;
        }
    }
}
