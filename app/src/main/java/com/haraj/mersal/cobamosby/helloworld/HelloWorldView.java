package com.haraj.mersal.cobamosby.helloworld;

import com.haraj.mersal.cobamosby.helloworld.base.BaseView;

/**
 * Created by riandyrn on 4/6/16.
 */
public interface HelloWorldView extends BaseView {

    void showHello(String text);

    void showGoodbye(String text);
}
