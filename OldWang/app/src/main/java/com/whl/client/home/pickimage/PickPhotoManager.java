package com.whl.client.home.pickimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PickPhotoManager {
    public static int max = 0;

    private static ArrayList<TempImageItem> tempSelectBitmapList = new ArrayList<TempImageItem>();   //选择的图片的临时列表

    public static Bitmap revisionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static ArrayList<TempImageItem> getSelectedBmpList() {
        return tempSelectBitmapList;
    }

    public static void saveSelectedBmp(TempImageItem imageItem) {
        tempSelectBitmapList.add(imageItem);
    }

    public static void remove(TempImageItem imageItem) {
        tempSelectBitmapList.remove(imageItem);
    }

    public static void clearSelectedBmpList() {
        tempSelectBitmapList.clear();
    }

}
