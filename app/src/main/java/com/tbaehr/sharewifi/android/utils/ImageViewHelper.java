package com.tbaehr.sharewifi.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by timo.baehr@gmail.com on 08.10.2016.
 */
public class ImageViewHelper {

    public final static String PROFILE_IMAGE = "profile.jpg";

    public static void loadImageInBackground(@NonNull final Uri uri, @NonNull final OnDownloadListener onDownloadListener) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(uri.toString());
                    onDownloadListener.onDownloadStarted();
                    URLConnection urlConnection = url.openConnection();
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                    final Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                    onDownloadListener.onDownloadCompleted(bitmap);

                    bufferedInputStream.close();
                    inputStream.close();
                } catch (Exception e) {
                    onDownloadListener.onDownloadError();
                    e.printStackTrace();
                }
            }
        });
    }

    public static void saveToInternalStorage(Context context, String fileName, Bitmap bitmap) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            int maxQuality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, maxQuality, fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap loadImageFromStorage(Context context, String fileName) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            return BitmapFactory.decodeStream(fileInputStream);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        return null;
    }

    public static Bitmap scale(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    public static Bitmap cropCircular(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public interface OnDownloadListener {
        void onDownloadStarted();

        void onDownloadCompleted(Bitmap bitmap);

        void onDownloadError();
    }

}
