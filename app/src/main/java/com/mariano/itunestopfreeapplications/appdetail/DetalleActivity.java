package com.mariano.itunestopfreeapplications.appdetail;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Visibility;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mariano.itunestopfreeapplications.R;
import com.mariano.itunestopfreeapplications.data.Application;

import io.realm.Realm;

public class DetalleActivity extends AppCompatActivity {

    public static final String ARG_APP_ID = "arg_app_id";
    private Realm realm;
    private ImageView mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mLogo = (ImageView) findViewById(R.id.logo);


        boolean isPhone = getResources().getBoolean(R.bool.is_phone);
        if(isPhone){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        realm = Realm.getDefaultInstance();
        long categoryId = getIntent().getLongExtra(ARG_APP_ID, -1);


        final Application application = realm.where(Application.class)
                .equalTo("id",categoryId)
                .findFirst();
        getSupportActionBar().setTitle(application.getTitle());

        Glide.with(this).load(application.getImage()).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontTransform().into(mLogo);

        ((TextView) findViewById(R.id.contenido)).setText(application.getSummary());
        ((TextView) findViewById(R.id.copy)).setText(application.getRights());

        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(application.getLink()));
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Visibility enterTransition = buildEnterTransition();
            getWindow().setEnterTransition(enterTransition);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Visibility buildEnterTransition() {
        Fade enterTransition = new Fade();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));

        return enterTransition;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
