package com.mariano.itunestopfreeapplications.apps;

import android.support.annotation.NonNull;

import com.mariano.itunestopfreeapplications.BasePresenter;
import com.mariano.itunestopfreeapplications.BaseView;
import com.mariano.itunestopfreeapplications.data.Application;

import io.realm.RealmResults;

/**
 * Created by mariano on 20/01/17.
 */

public interface AppsContract {
    interface View extends BaseView<Presenter> {
        void setPresenter(@NonNull AppsContract.Presenter presenter);
        void showAppDetailView(@NonNull final long id);
        void showApps(RealmResults<Application> apps);
        void onErrorService();
    }
    interface Presenter extends BasePresenter {
        void onItemClick(Application obj, android.view.View v);

    }
}
