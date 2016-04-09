package com.haraj.mersal.cobamosby.helloworld;

import android.os.AsyncTask;

/**
 * Created by riandyrn on 4/6/16.
 */
public class GreetingGeneratorTask extends AsyncTask<Void, Void, Integer> {

    public interface GreetingTaskListener {
        void onGreetingGenerated(String greetingText);
    }

    private String baseText;
    private GreetingTaskListener callback;

    public GreetingGeneratorTask(String baseText, GreetingTaskListener callback) {
        this.baseText = baseText;
        this.callback = callback;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}

        return (int) (Math.random() * 100);
    }

    @Override
    protected void onPostExecute(Integer randomInt) {
        callback.onGreetingGenerated(baseText + " " + randomInt);
    }
}
