package com.mariano.itunestopfreeapplications.apps;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mariano.itunestopfreeapplications.R;
import com.mariano.itunestopfreeapplications.appdetail.DetalleActivity;
import com.mariano.itunestopfreeapplications.data.Application;
import com.mariano.itunestopfreeapplications.data.source.LoadDataService;
import com.mariano.itunestopfreeapplications.util.ui.DividerItemDecoration;

import io.realm.RealmResults;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mariano on 20/01/17.
 */

public class AppsFragment extends Fragment implements AppsViewAdapter.ClickListener, AppsContract.View {

    private AppsViewAdapter adapter;
    private View mContainer;
    private AppsContract.Presenter mPresenter;

    public static AppsFragment newInstance() {
        return new AppsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new AppsViewAdapter(getContext(), null);

        adapter.setOnItemClickListener(this);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.fragment_apps, container, false);

        RecyclerView recyclerView = (RecyclerView) mContainer.findViewById(R.id.list);

        boolean isPhone = getResources().getBoolean(R.bool.is_phone);
        if(isPhone)
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        return mContainer;
    }
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull AppsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showAppDetailView(@NonNull long id) {
        Intent intent = new Intent(getContext(), DetalleActivity.class);
        intent.putExtra(DetalleActivity.ARG_APP_ID,id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        }else{
            startActivity(intent);
        }
    }

    @Override
    public void showApps(RealmResults<Application> apps) {
        adapter.updateData(apps);
    }

    @Override
    public void onErrorService() {
        Snackbar.make(mContainer,R.string.failed_connection,Snackbar.LENGTH_INDEFINITE).setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mServiceIntent = new Intent(getActivity(), LoadDataService.class);
                getActivity().startService(mServiceIntent);

            }
        }).show();
    }

    @Override
    public void onItemClick(Application obj, View v) {
        mPresenter.onItemClick(obj,v);
    }
}


