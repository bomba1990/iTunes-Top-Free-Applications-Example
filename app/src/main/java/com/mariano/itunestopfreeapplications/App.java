package com.mariano.itunestopfreeapplications;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by mariano on 1/6/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
