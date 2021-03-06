package com.mariano.itunestopfreeapplications.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mariano.itunestopfreeapplications.R;
import com.mariano.itunestopfreeapplications.adapters.AplicacionesViewAdapter;
import com.mariano.itunestopfreeapplications.background.LoadDataService;
import com.mariano.itunestopfreeapplications.models.Application;
import com.mariano.itunestopfreeapplications.models.Category;
import com.mariano.itunestopfreeapplications.models.events.onFailEvent;
import com.mariano.itunestopfreeapplications.util.ui.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AplicacionesViewAdapter.ClickListener {

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private Realm realm;
    private AplicacionesViewAdapter adapter;

    private RealmResults<Category> mCategorys;
    private RealmResults<Application> mApps;
    private View mContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getWindow().setBackgroundDrawableResource(R.color.blanco);


        mContainer = findViewById(R.id.container);

        realm = Realm.getDefaultInstance();

        boolean isPhone = getResources().getBoolean(R.bool.is_phone);
        if(isPhone)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        mCategorys = realm.where(Category.class).findAll();
        mApps = realm.where(Application.class).findAll();

        recyclerView = (RecyclerView) findViewById(R.id.list);
        if(isPhone)
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        adapter = new AplicacionesViewAdapter(this, mApps);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapter.setOnItemClickListener(this);

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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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

        for(Category cat : mCategorys) {
            menu.add(R.id.nav_inicio, (int)cat.getId(), 0, cat.getLabel());
        }
        mCategorys.addChangeListener(new RealmChangeListener<RealmResults<Category>>() {
            @Override
            public void onChange(RealmResults<Category> element) {
                for(Category cat : element) {
                    if(menu.findItem((int)cat.getId()) == null)
                        menu.add(R.id.nav_inicio, (int)cat.getId(), 0, cat.getLabel());
                }
            }
        });
    }
    @Subscribe
    public void onEvent(onFailEvent event){
        Snackbar.make(mContainer,R.string.failed_connection,Snackbar.LENGTH_INDEFINITE).setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mServiceIntent = new Intent(MainActivity.this, LoadDataService.class);
                startService(mServiceIntent);

            }
        }).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
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
                mApps = realm.where(Application.class).contains("title",query, Case.INSENSITIVE).findAll();
                adapter.updateData(mApps);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mApps = realm.where(Application.class).contains("title",query,Case.INSENSITIVE).findAll();
                adapter.updateData(mApps);
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
            getSupportActionBar().setTitle(R.string.app_name);
            mApps = realm.where(Application.class).findAll();
            adapter.updateData(mApps);
        }else{
            Category category = realm.where(Category.class)
                    .equalTo("id",id)
                    .findFirst();
            getSupportActionBar().setTitle(category.getLabel());
            adapter.updateData(category.entries);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(int position, View v) {
        Intent intent = new Intent(getApplicationContext(), DetalleActivity.class);
        intent.putExtra(DetalleActivity.ARG_APP_ID,adapter.getItem(position).getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }else{
            startActivity(intent);
        }
    }
}
