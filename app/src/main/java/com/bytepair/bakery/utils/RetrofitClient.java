/**
 * Helpful guide for creating and using singleton for Retrofit
 * https://code.tutsplus.com/tutorials/getting-started-with-retrofit-2--cms-27792
 */

package com.bytepair.bakery.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseURL) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}