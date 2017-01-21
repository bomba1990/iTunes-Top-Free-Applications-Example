package com.mariano.itunestopfreeapplications.data.source;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by mariano on 10/01/17.
 */

public interface ApiService {
    @GET("/us/rss/topfreeapplications/limit=20/json")
    Call<ResponseBody> getApplications();
}
