package com.haraj.mersal.cobamosby.helloworld;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.isseiaoki.simplecropview.CropImageView;

/**
 * Created by riandyrn on 4/7/16.
 */
public class EditProfilePictPresenter extends MvpBasePresenter<EditProfilePictView> {

    private Context context;
    private ContentResolver contentResolver;
    private Uri uri;

    private ProcessRawBitmapTask processRawBitmapTask;
    private UploadBitmapTask uploadBitmapTask;

    public EditProfilePictPresenter(Context context, ContentResolver contentResolver, Uri uri) {
        this.context = context;
        this.contentResolver = contentResolver;
        this.uri = uri;

        Log.d(this.getClass().getSimpleName(), "URI.getPath(): " + uri.getPath());
    }

    private void cancelProcessRawBitmapTaskIfRunning() {
        if(processRawBitmapTask != null) {
            processRawBitmapTask.cancel(true);
        }
    }

    private void cancelUploadBitmapTaskIfRunning() {
        if(uploadBitmapTask != null) {
            uploadBitmapTask.cancel(true);
        }
    }

    public void loadBitmap() {

        if(isViewAttached()) {
            getView().showProgress();
            getView().setProgressCaption("Open image...");
        }

        cancelProcessRawBitmapTaskIfRunning();

        processRawBitmapTask = new ProcessRawBitmapTask(contentResolver, uri, new ProcessRawBitmapTask.ProcessRawBitmapTaskListener() {
            @Override
            public void onRawBitmapProcessed(Bitmap bitmap) {
                if(isViewAttached()) {
                    getView().showBaseImage(bitmap);
                }
            }
        });

        processRawBitmapTask.execute();
    }

    private void uploadBitmap(final Bitmap croppedBitmap) {
        cancelUploadBitmapTaskIfRunning();

        uploadBitmapTask = new UploadBitmapTask(context, croppedBitmap, new UploadBitmapTask.UploadBitmapTaskListener() {
            @Override
            public void onError(Exception ex) {
                Log.e(this.getClass().getSimpleName(), "Error: " + ex.getMessage());
            }

            @Override
            public void onProgress(int percentage) {
                Log.d(this.getClass().getSimpleName(), "Upload percentage: " + percentage);
            }

            @Override
            public void onComplete(String url) {
                if(isViewAttached()) {
                    getView().showCroppedImage(url);
                }
            }
        });

        uploadBitmapTask.execute();
    }

    public void cropImage(CropImageView cropImageView) {
        cropImageView.setMaxHeight(128);
        cropImageView.setMaxWidth(128);

        Bitmap croppedBitmap = cropImageView.getCroppedBitmap();
        uploadBitmap(scaleDown(croppedBitmap, 512, true));
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);

        if(!retainInstance) {
            cancelProcessRawBitmapTaskIfRunning();
        }
    }

    private Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {

        Log.d(this.getClass().getSimpleName(), "realImage.getWidth(): " + realImage.getWidth() + ", realImage.getHeight(): " + realImage.getHeight());
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}
