package com.mariano.itunestopfreeapplications.apps;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;

import com.mariano.itunestopfreeapplications.appdetail.DetalleActivity;
import com.mariano.itunestopfreeapplications.data.source.RealmService;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mariano on 20/01/17.
 */
public class AppsPresenter implements AppsContract.Presenter {
    private final RealmService mRealmService;

    private final AppsContract.View mTasksView;

//TODO:    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;

    public AppsPresenter(@NonNull RealmService realmService, @NonNull AppsContract.View tasksView) {
        mRealmService = checkNotNull(realmService, "realmService cannot be null");
        mTasksView = checkNotNull(tasksView, "tasksView cannot be null!");

        mTasksView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void closeRealm() {
        mRealmService.closeRealm();
    }

    @Override
    public void onItemClick(int position, View v) {
        Intent intent = new Intent(getContext(), DetalleActivity.class);
        intent.putExtra(DetalleActivity.ARG_APP_ID,adapter.getItem(position).getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        }else{
            startActivity(intent);
        }
    }
}
