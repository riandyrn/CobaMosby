package com.haraj.mersal.cobamosby.helloworld.base;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by riandyrn on 4/6/16.
 */
public interface BaseView extends MvpView {

    void showProgress();

    void showContent();

    void setProgressCaption(String caption);
}
