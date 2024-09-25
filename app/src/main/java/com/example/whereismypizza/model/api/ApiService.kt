package com.example.whereismypizza.model.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * This contains all the API we can call. In this case only one.
 */
object ApiService {

    private const val YELP_API = "https://api.yelp.com/v3/"

    val api: YelpAPI by lazy {
        Retrofit.Builder()
            .baseUrl(YELP_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YelpAPI::class.java)
    }

}