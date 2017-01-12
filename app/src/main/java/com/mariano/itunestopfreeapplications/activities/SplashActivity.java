package com.mariano.itunestopfreeapplications.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.mariano.itunestopfreeapplications.AppConfig;
import com.mariano.itunestopfreeapplications.R;
import com.mariano.itunestopfreeapplications.background.LoadDataService;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

public class SplashActivity extends AppCompatActivity {

    private Intent mainIntent;
    private SharedPreferences sharedPref;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        try {
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e){
            try {
                Realm.deleteRealm(config);
                //Realm file has been deleted.
                realm = Realm.getDefaultInstance();
            } catch (Exception ex){
                throw ex;
                //No Realm file to remove.
            }
        }

        Intent mServiceIntent = new Intent(this, LoadDataService.class);
        startService(mServiceIntent);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean first_time  = sharedPref.getBoolean(AppConfig.SPLASH_FIRST_TIME, true);

        mainIntent = new Intent(getApplicationContext(), MainActivity.class);

        if(!first_time){
            startActivity(mainIntent);
            finish();
            return;
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(AppConfig.SPLASH_FIRST_TIME, false);
                editor.apply();

                startActivity(mainIntent);
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, AppConfig.SPLASH_DELAY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
