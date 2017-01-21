package com.mariano.itunestopfreeapplications.apps;

import android.support.annotation.NonNull;
import android.view.View;

import com.mariano.itunestopfreeapplications.data.Application;
import com.mariano.itunestopfreeapplications.data.events.onFailEvent;
import com.mariano.itunestopfreeapplications.data.source.RealmService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mariano on 20/01/17.
 */
public class AppsPresenter implements AppsContract.Presenter {
    private final RealmService mRealmService;

    private final AppsContract.View mTasksView;

//TODO:    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;
    private AppsViewAdapter mAdapter;

    public AppsPresenter(@NonNull RealmService realmService, @NonNull AppsContract.View tasksView) {
        mRealmService = checkNotNull(realmService, "realmService cannot be null");
        mTasksView = checkNotNull(tasksView, "tasksView cannot be null!");

        mTasksView.setPresenter(this);
    }

    @Override
    public void start() {
        mTasksView.showApps(mRealmService.getAllApps());
    }
    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(onFailEvent event){
        mTasksView.onErrorService();
    }

    @Override
    public void closeRealm() {
        mRealmService.closeRealm();
    }

    @Override
    public void onItemClick(Application obj, View v) {
        mTasksView.showAppDetailView(obj.getId());
    }
}
