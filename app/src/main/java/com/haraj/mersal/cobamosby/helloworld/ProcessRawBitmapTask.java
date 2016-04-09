package com.haraj.mersal.cobamosby.helloworld;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.io.IOException;

/**
 * Created by riandyrn on 4/7/16.
 */
public class ProcessRawBitmapTask extends AsyncTask<Void, Void, Bitmap> {

    public interface ProcessRawBitmapTaskListener {
        void onRawBitmapProcessed(Bitmap bitmap);
    }

    private ContentResolver contentResolver;
    private Uri uri;
    private ProcessRawBitmapTaskListener callback;

    public ProcessRawBitmapTask(ContentResolver contentResolver, Uri uri, ProcessRawBitmapTaskListener callback) {
        this.contentResolver = contentResolver;
        this.uri = uri;
        this.callback = callback;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        callback.onRawBitmapProcessed(bitmap);
    }
}
