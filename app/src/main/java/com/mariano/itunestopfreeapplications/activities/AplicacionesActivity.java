package com.mariano.itunestopfreeapplications.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mariano.itunestopfreeapplications.R;
import com.mariano.itunestopfreeapplications.adapters.AplicacionesViewAdapter;
import com.mariano.itunestopfreeapplications.models.Category;
import com.mariano.itunestopfreeapplications.util.ui.DividerItemDecoration;

import io.realm.Realm;

public class AplicacionesActivity extends AppCompatActivity {

    public static final String ARG_CATEGORY_ID = "arg_category_id";
    private Realm realm;
    private RecyclerView recyclerView;
    private AplicacionesViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplicaciones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = Realm.getDefaultInstance();

        long categoryId = getIntent().getLongExtra(ARG_CATEGORY_ID, -1);


        Category category = realm.where(Category.class)
                .equalTo("id",categoryId)
                .findFirst();
        getSupportActionBar().setTitle(category.getLabel());

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AplicacionesViewAdapter(this, category.entries);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapter.setOnItemClickListener(new AplicacionesViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(getApplicationContext(), DetalleActivity.class);
                intent.putExtra(DetalleActivity.ARG_APP_ID,adapter.getItem(position).getId());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
