package com.mariano.itunestopfreeapplications.apps;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.mariano.itunestopfreeapplications.R;
import com.mariano.itunestopfreeapplications.data.Category;
import com.mariano.itunestopfreeapplications.data.source.LoadDataService;
import com.mariano.itunestopfreeapplications.data.source.RealmService;
import com.mariano.itunestopfreeapplications.util.ActivityUtils;
import com.mariano.itunestopfreeapplications.util.ui.BaseActivity;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class AppsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private DrawerLayout mDrawerLayout;

    private AppsPresenter mAppsPresenter;
    private Toolbar mToolbar;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getWindow().setBackgroundDrawableResource(R.color.blanco);

        Intent mServiceIntent = new Intent(this, LoadDataService.class);
        startService(mServiceIntent);

        realm = Realm.getDefaultInstance();


        AppsFragment appsFragment =
                (AppsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (appsFragment == null) {
            // Create the fragment
            appsFragment = AppsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), appsFragment, R.id.contentFrame);
        }

        // Create the presenter
        mAppsPresenter = new AppsPresenter(new RealmService(realm), appsFragment);

        //TODO: Load previously saved state, if available.
        if (savedInstanceState != null) {
            int currentFiltering = savedInstanceState.getInt(CURRENT_FILTERING_KEY,0);
            mAppsPresenter.setFiltering(currentFiltering);
        }

        initNavigation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.START);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            getWindow().setReenterTransition(slideTransition);
            getWindow().setExitTransition(slideTransition);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_FILTERING_KEY, mAppsPresenter.getFiltering());
        super.onSaveInstanceState(outState);
    }


    private void initNavigation() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Menu menu = navigationView.getMenu();
        RealmResults<Category> categories = realm.where(Category.class).findAll();
        for(Category cat : categories) {
            menu.add(R.id.nav_inicio, (int)cat.getId(), 0, cat.getLabel());
        }
        categories.addChangeListener(new RealmChangeListener<RealmResults<Category>>() {
            @Override
            public void onChange(RealmResults<Category> element) {
                for(Category cat : element) {
                    if(menu.findItem((int)cat.getId()) == null)
                        menu.add(R.id.nav_inicio, (int)cat.getId(), 0, cat.getLabel());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAppsPresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAppsPresenter.onStop();
    }

    @Override
    protected void closeRealm() {
        mAppsPresenter.closeRealm();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else  {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuF = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuF);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAppsPresenter.showApps(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAppsPresenter.showApps(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            //getSupportActionBar().setTitle(R.string.app_name);
            mAppsPresenter.showAllApps();
        }else{
            mAppsPresenter.showApps(id);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
