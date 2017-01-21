package com.mariano.itunestopfreeapplications.apps;

import android.support.annotation.NonNull;
import android.view.View;

import com.mariano.itunestopfreeapplications.data.Application;
import com.mariano.itunestopfreeapplications.data.Category;
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

    private int mCurrentFiltering;

    private boolean mFirstLoad = true;
    private AppsViewAdapter mAdapter;

    public AppsPresenter(@NonNull RealmService realmService, @NonNull AppsContract.View tasksView) {
        mRealmService = checkNotNull(realmService, "realmService cannot be null");
        mTasksView = checkNotNull(tasksView, "tasksView cannot be null!");

        mTasksView.setPresenter(this);
    }

    @Override
    public void start() {
        showAllApps();
    }
    public void showAllApps(){
        mCurrentFiltering = 0;
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

    @Override
    public void showApps(Integer id) {
        if(id != null){
            Category category = mRealmService.getCategory(id);
            mTasksView.showApps(category.entries);
        }else{
            showAllApps();
        }

    }

    @Override
    public void showApps(String query) {
        mTasksView.showApps(mRealmService.getAllApps(query));
    }

    @Override
    public void setFiltering(int requestType) {
        mCurrentFiltering = requestType;
    }

    @Override
    public int getFiltering() {
        return mCurrentFiltering;
    }
}
