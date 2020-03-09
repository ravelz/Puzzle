package com.rp.puzzle.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by Rahul Pareta on 19/5/18.
 */

public class BitmapUtil {


    public static Bitmap cropBitmap(Bitmap mBitmap,
                                    int startX,
                                    int startY,
                                    int width,
                                    int height,
                                    int borderSize,
                                    int borderColor) {

        Bitmap bmp= Bitmap.createBitmap(mBitmap,
                startX,
                startY,
                width-(2*borderSize),
                height-(2*borderSize));
        Bitmap bmpWithBorder = Bitmap.createBitmap(
                bmp.getWidth() + borderSize * 2,
                bmp.getHeight() + borderSize * 2,
                bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(borderColor);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }


    public static Bitmap scaleBitmap(Bitmap mBitmap, int width, int height) {
        return Bitmap.createScaledBitmap(mBitmap, width, height, false);
    }

}
