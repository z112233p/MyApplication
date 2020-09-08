package com.example.myapplication.tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;

import java.io.File;
import java.io.IOException;

public class ImageUtils {

    /**
     * Bitmap画像を縦横サイズにリサイズ
     * @param org 編集前のBitmap
     * @param destWidth 指定横幅サイズ
     * @param destHeight 指定縦幅サイズ
     * @return Bitmap リサイズ画像
     */
    public static Bitmap resizeBitmap(Bitmap org, int destWidth, int destHeight) {

        // 空のbitmap、背景は全透明
        Bitmap blankResized = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888);

        // 縦縮か横縮尺か判定
        int xPadding = 0;
        int yPadding = 0;
        int scaledWidth = blankResized.getWidth();
        int scaledHeight = blankResized.getHeight();
        float scale = 1;
        if (org.getWidth() > org.getHeight()) {
            // 縮尺とパディングの設定
            scale = (float) blankResized.getWidth() / (float) org.getWidth();
            scaledHeight = (int) (org.getHeight() * scale);
            yPadding = (blankResized.getHeight() - scaledHeight) / 2;
        }
        else {
            // 縮尺とパディングの設定
            scale = (float) blankResized.getHeight() / (float) org.getHeight();
            scaledWidth = (int) (org.getWidth() * scale);
            xPadding = (blankResized.getWidth() - scaledWidth) / 2;
        }

        // 画像を縮小してコピーするときれいになる
        Bitmap orgScale = Bitmap.createScaledBitmap(org, scaledWidth, scaledHeight, true);
        Canvas canvas = new Canvas(blankResized);
        canvas.drawBitmap(
                orgScale,
                new Rect(0, 0, scaledWidth, scaledHeight),
                new Rect(xPadding, yPadding, xPadding + scaledWidth, yPadding + scaledHeight),
                null);

        return blankResized;
    }

    public static Bitmap rotateImage(final Bitmap bitmap, final File fileWithExifInfo) {
        Bitmap rotatedBitmap = bitmap;
        int orientation = 0;
        try {
            orientation = getImageOrientation(fileWithExifInfo.getAbsolutePath());
            if (orientation != 0) {
                final Matrix matrix = new Matrix();
                matrix.postRotate(orientation);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);
                bitmap.recycle();
            }
        } catch (final IOException e) {
        }
        return rotatedBitmap;
    }

    public static int getImageOrientation(final String file) throws IOException {
        final ExifInterface exif = new ExifInterface(file);
        final int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return 0;
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

    public static int getOrientation(final int orientation) {
        switch (orientation) {
            case 0:
                return ExifInterface.ORIENTATION_NORMAL;
            case 90:
                return ExifInterface.ORIENTATION_ROTATE_90;
            case 180:
                return ExifInterface.ORIENTATION_ROTATE_180;
            case 270:
                return ExifInterface.ORIENTATION_ROTATE_270;
            default:
                return 0;
        }
    }
}