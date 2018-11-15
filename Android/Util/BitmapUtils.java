package com.pointercn.doorbell.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * @author xin
 * bitmap工具类
 * 1. {@link #decodeSampledBitmapFromResource}提供从资源文件加载图片为bitmap，根据控件大小来压缩，以节省不必要的内存
 * 2. {@link #bitmap2Byte 将bitmap转为byte数组,不压缩}
 * 3. {@link #btimap2Base64Str 将bitmap转为base64的字符串，不压缩}
 * 4. {@link #btye2Bitmap 将byte数组转为bitmap，不压缩}
 */
public class BitmapUtils {
    private static final String TAG = BitmapUtils.class.getSimpleName();

    /**
     * 从资源文件中获取压缩后的bitmap
     *
     * @param res       资源
     * @param resId     资源id
     * @param reqWidth  需要的宽度
     * @param reqHeight 需要的高度
     * @return 压缩后的bitmap
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        Log.i(TAG, "inSampleSize:" + options.inSampleSize);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * @param bitmap
     * @return
     * @desc bitmap转byte数组, 不压缩方式
     */
    public static byte[] bitmap2Byte(Bitmap bitmap) {
        if (bitmap == null) {
            Log.e(TAG, "bitmap为null");
            return new byte[1];
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datas = baos.toByteArray();
        return datas;
    }

    /**
     * bitmap转换为base64字符串
     *
     * @param bitmap
     * @return
     */
    public static String btimap2Base64Str(Bitmap bitmap) {
        return Base64.encodeToString(bitmap2Byte(bitmap), 0);
    }

    /**
     * @param imgByte
     * @return
     * @desc 将byte转换为bitmap，采用无压缩转换方式
     */
    public static Bitmap btye2Bitmap(byte[] imgByte) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        return bitmap;
    }

    /**
     * 计算压缩大小
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
