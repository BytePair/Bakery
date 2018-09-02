/**
 * Helpful guide for creating and using singleton for Retrofit
 * https://code.tutsplus.com/tutorials/getting-started-with-retrofit-2--cms-27792
 */

package com.bytepair.bakery.utils.services;

import com.bytepair.bakery.utils.RetrofitClient;

public class RecipeService {

    private static final String BASE_URL = "http://d17h27t6h515a5.cloudfront.net/";

    public static RecipeAPI getAPI() {
        return RetrofitClient.getClient(BASE_URL).create(RecipeAPI.class);
    }

}