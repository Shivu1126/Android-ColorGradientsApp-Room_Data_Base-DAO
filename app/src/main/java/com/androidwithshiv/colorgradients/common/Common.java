package com.androidwithshiv.colorgradients.common;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

public class Common {
    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static String intToHexColorCode(int colorCode){
        return String.format("#%06X", (0xFFFFFF & colorCode));
    }
    public static int hexToIntColorCode(String hexColor){
        Log.d("hexColor", hexColor);
        return Color.parseColor(hexColor);
    }
}
