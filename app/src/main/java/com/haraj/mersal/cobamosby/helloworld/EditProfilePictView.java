package com.haraj.mersal.cobamosby.helloworld;

import android.graphics.Bitmap;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.haraj.mersal.cobamosby.helloworld.base.BaseView;

/**
 * Created by riandyrn on 4/7/16.
 */
public interface EditProfilePictView extends BaseView {

    void showBaseImage(Bitmap baseImage);

    void showCroppedImage(String url);
}
