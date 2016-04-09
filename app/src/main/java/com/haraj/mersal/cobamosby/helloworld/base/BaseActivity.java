package com.haraj.mersal.cobamosby.helloworld.base;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.haraj.mersal.cobamosby.R;

import butterknife.Bind;

/**
 * Created by riandyrn on 4/6/16.
 */
public abstract class BaseActivity<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpActivity<V, P> implements BaseView {

    @Bind(R.id.progressLayout) LinearLayout progressLayout;
    @Bind(R.id.contentLayout) LinearLayout contentLayout;
    @Bind(R.id.progressCaption) TextView progressCaption;

    @NonNull
    @Override
    public abstract P createPresenter();

    @Override
    public void showProgress() {
        progressLayout.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
    }

    @Override
    public void setProgressCaption(String caption) {
        progressCaption.setText(caption);
    }

    @Override
    public void showContent() {
        progressLayout.setVisibility(View.GONE);
        contentLayout.setVisibility(View.VISIBLE);
    }
}
