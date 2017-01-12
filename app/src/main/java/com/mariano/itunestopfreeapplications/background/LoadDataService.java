package com.mariano.itunestopfreeapplications.background;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.mariano.itunestopfreeapplications.AppConfig;
import com.mariano.itunestopfreeapplications.api.ApiService;
import com.mariano.itunestopfreeapplications.models.Application;
import com.mariano.itunestopfreeapplications.models.Category;
import com.mariano.itunestopfreeapplications.models.events.onFailEvent;
import com.mariano.itunestopfreeapplications.util.Utilities;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmQuery;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mariano on 11/01/17.
 */

public class LoadDataService extends IntentService {

    private Realm realm;
    private EventBus eventbus;

    public LoadDataService() {
        super("LoadDataService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        realm = Realm.getDefaultInstance();
        eventbus = EventBus.getDefault();
        eventbus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        eventbus.unregister(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ApiService service = Utilities.apiService();
        Call<ResponseBody> call = service.getApplications();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject responseObject = new JSONObject(response.body().string());
                        JSONArray entrys = responseObject.getJSONObject("feed").getJSONArray("entry");
                        for(int x = 0; x < entrys.length();x++){
                            JSONObject object = entrys.getJSONObject(x);
                            JSONObject objectCategory = object.getJSONObject("category").getJSONObject("attributes");

                            RealmQuery<Category> query = realm.where(Category.class)
                                    .equalTo("id", objectCategory.getLong("im:id"));
                            Category category;
                            if(query.count() != 0){
                                category = query.findFirst();
                            }else {
                                category = new Category(
                                        objectCategory.getLong("im:id"),
                                        objectCategory.getString("label"),
                                        objectCategory.getString("term")
                                );
                            }
                            Application application = new Application(
                                    object.getJSONObject("id").getJSONObject("attributes").getLong("im:id"),
                                    object.getJSONObject("summary").getString("label"),
                                    null,
                                    object.getJSONObject("title").getString("label"),
                                    object.getJSONArray("im:image").getJSONObject(0).getString("label"),
                                    object.getJSONObject("link").getJSONObject("attributes").getString("href"),
                                    object.getJSONObject("rights").getString("label")
                            );
                            realm.beginTransaction();
                            if(!category.isManaged())
                                category = realm.copyToRealmOrUpdate(category);
                            application.setCategory(category);
                            application = realm.copyToRealmOrUpdate(application);
                            if(!category.entries.contains(application))
                                category.entries.add(application);
                            realm.commitTransaction();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                eventbus.post(new onFailEvent(t.getLocalizedMessage()));
                Log.e(AppConfig.TAG,t.getLocalizedMessage());
                t.printStackTrace();
            }
        });
    }
    @Subscribe
    public void onEvent(onFailEvent event){}
}
