package com.haraj.mersal.cobamosby.helloworld;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haraj.mersal.cobamosby.R;
import com.haraj.mersal.cobamosby.helloworld.base.BaseActivity;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelloWorldActivity extends BaseActivity<HelloWorldView, HelloWorldPresenter>
        implements HelloWorldView {

    @Bind(R.id.greetingTextView) TextView greetingTextView;
    @Bind(R.id.imageView) ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @NonNull
    @Override
    public HelloWorldPresenter createPresenter() {
        return new HelloWorldPresenter();
    }

    @OnClick(R.id.helloButton)
    public void onHelloButtonClicked() {
        Log.d(this.getClass().getSimpleName(), "onHelloButtonClicked() called");

        showProgress();
        presenter.greetHello();
    }

    @OnClick(R.id.goodbyeButton)
    public void onGoodbyeButtonClicked() {
        Log.d(this.getClass().getSimpleName(), "onGoodbyeButtonClicked() called");

        showProgress();
        presenter.greetGoodbye();
    }

    @Override
    public void showHello(String text) {
        showContent();

        greetingTextView.setTextColor(Color.RED);
        greetingTextView.setText(text);
    }

    @Override
    public void showGoodbye(String text) {
        showContent();

        greetingTextView.setTextColor(Color.BLUE);
        greetingTextView.setText(text);
    }

    @OnClick(R.id.openGalleryButton)
    public void onOpenGalleryButton() {
        Intent intent = new Intent(
                Intent.ACTION_GET_CONTENT,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //Intent intent = new Intent();
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0) {
            if(resultCode == RESULT_OK) {
                Intent intent = new Intent(this, EditProfilePictActivity.class);
                Uri uri = data.getData();
                Log.d(this.getClass().getSimpleName(), uri.toString());
                intent.setData(uri);

                startActivity(intent);
            } else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
