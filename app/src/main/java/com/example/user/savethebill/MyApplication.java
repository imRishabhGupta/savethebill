package com.example.user.savethebill;


/**
 * Created by rohanpc on 6/8/2016.
 */
public class MyApplication extends android.app.Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //Firebase.setAndroidContext(this);
    }
}