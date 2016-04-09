package com.haraj.mersal.cobamosby.helloworld;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.haraj.mersal.cobamosby.R;
import com.haraj.mersal.cobamosby.helloworld.base.BaseActivity;
import com.isseiaoki.simplecropview.CropImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfilePictActivity extends BaseActivity<EditProfilePictView, EditProfilePictPresenter>
        implements EditProfilePictView {

    @Bind(R.id.cropImageView) CropImageView cropImageView;
    @Bind(R.id.croppedImageView) ImageView croppedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_pict);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Set Profile Picture");
        ButterKnife.bind(this);

        presenter.loadBitmap();
    }

    @NonNull
    @Override
    public EditProfilePictPresenter createPresenter() {
        return new EditProfilePictPresenter(this, this.getContentResolver(), getIntent().getData());
    }

    @OnClick(R.id.cropButton)
    public void onCropButtonClicked() {
        showProgress();
        setProgressCaption("Upload image...");
        presenter.cropImage(cropImageView);
    }

    @Override
    public void showBaseImage(Bitmap baseImage) {
        cropImageView.setImageBitmap(baseImage);
        showContent();
    }

    @Override
    public void showCroppedImage(String url) {
        Picasso.with(this).load(url).into(croppedImageView, new Callback() {
            @Override
            public void onSuccess() {
                showContent();
            }

            @Override
            public void onError() {
                Log.e(this.getClass().getSimpleName(), "Error load image");
                showContent();
            }
        });
    }

    @Override
    protected void onPause() {
        cropImageView.setImageResource(0);
        croppedImageView.setImageResource(0);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadBitmap();
    }

}
