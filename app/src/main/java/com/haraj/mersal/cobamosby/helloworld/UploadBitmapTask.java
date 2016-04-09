package com.haraj.mersal.cobamosby.helloworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by riandyrn on 4/8/16.
 */
public class UploadBitmapTask extends AsyncTask<Void, Void, Void> {

    public interface UploadBitmapTaskListener {

        void onError(Exception ex);

        void onProgress(int percentage);

        void onComplete(String url);
    }

    private Context context;
    private Bitmap bitmap;
    private UploadBitmapTaskListener callback;
    private String url;

    private TransferListener transferListener;

    public UploadBitmapTask(Context context, Bitmap bitmap, UploadBitmapTaskListener mCallback) {
        this.context = context;
        this.bitmap = bitmap;
        this.callback = mCallback;

        transferListener = new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if(state.toString().equals("COMPLETED")) {
                    callback.onComplete(url);
                }

                Log.d(this.getClass().getSimpleName(), "current state: " + state.toString() + ", " + state.toString().equals("COMPLETED"));
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                callback.onProgress(percentage);
            }

            @Override
            public void onError(int id, Exception ex) {
                callback.onError(ex);
            }
        };
    }

    @Override
    protected Void doInBackground(Void... params) {
        url = upload(getFile(bitmap));
        Log.d(this.getClass().getSimpleName(), "url: " + url);
        return null;
    }

    private File getFile(Bitmap bitmap) {
        File filesDir = context.getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, "cropped_" + System.currentTimeMillis() + ".jpeg");

        OutputStream os;

        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            imageFile = null;
        }

        return imageFile;
    }

    private String upload(File file) {

        // Initialize the Amazon Cognito credentials provider
        /*CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context.getApplicationContext(),
                Constants.IDENTITY_POOL_ID, // Identity Pool ID
                Regions.EU_WEST_1 // Region
        );*/

        AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(Constants.ACCESS_KEY_ID, Constants.SECRET_ACCESS_KEY));
        s3.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));

        TransferUtility transferUtility = new TransferUtility(s3, context.getApplicationContext());

        TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME, file.getName(), file);
        observer.setTransferListener(transferListener);

        return generateURL(file);
    }

    private String generateURL(File file) {
        return Constants.BASE_URL + file.getName();
    }
}
