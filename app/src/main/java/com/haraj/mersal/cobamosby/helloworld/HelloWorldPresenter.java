package com.haraj.mersal.cobamosby.helloworld;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by riandyrn on 4/6/16.
 */
public class HelloWorldPresenter extends MvpBasePresenter<HelloWorldView> {

    private GreetingGeneratorTask greetingTask;

    private void cancelGreetingTaskIfRunning() {
        if(greetingTask != null) {
            greetingTask.cancel(true);
        }
    }

    public void greetHello() {
        cancelGreetingTaskIfRunning();

        greetingTask = new GreetingGeneratorTask("Hello", new GreetingGeneratorTask.GreetingTaskListener() {
            @Override
            public void onGreetingGenerated(String greetingText) {
                if(isViewAttached()) {
                    getView().showHello(greetingText);
                }
            }
        });

        greetingTask.execute();
    }

    public void greetGoodbye() {
        cancelGreetingTaskIfRunning();

        greetingTask = new GreetingGeneratorTask("Goodbye", new GreetingGeneratorTask.GreetingTaskListener() {
            @Override
            public void onGreetingGenerated(String greetingText) {
                if(isViewAttached()) {
                    getView().showGoodbye(greetingText);
                }
            }
        });

        greetingTask.execute();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if(!retainInstance) {
            cancelGreetingTaskIfRunning();
        }
    }
}
