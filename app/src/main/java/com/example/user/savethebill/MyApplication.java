package com.example.user.savethebill;

import com.firebase.client.Firebase;

/**
 * Created by rohanpc on 6/8/2016.
 */
public class MyApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}