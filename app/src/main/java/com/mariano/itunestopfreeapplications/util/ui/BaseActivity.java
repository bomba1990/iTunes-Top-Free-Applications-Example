package com.mariano.itunestopfreeapplications.util.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        closeRealm();
        super.onDestroy();
    }

    protected abstract void closeRealm();
}
