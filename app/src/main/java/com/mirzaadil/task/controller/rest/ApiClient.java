package com.mirzaadil.task.controller.rest;

import com.mirzaadil.task.controller.utils.ApplicationConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mirzaadil on 8/23/17.
 */

public class ApiClient {
    private static Retrofit retrofit = null;

    //Eager Initialization
    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApplicationConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
