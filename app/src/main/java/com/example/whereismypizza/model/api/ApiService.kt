package com.example.whereismypizza.model.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


/**
 * This contains all the API we can call. In this case only one.
 */
class ApiService @Inject constructor() {

    val api: YelpAPI =
        Retrofit.Builder()
            .baseUrl(YELP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YelpAPI::class.java)


    companion object {
        private const val YELP_BASE_URL = "https://api.yelp.com/v3/"
    }
}