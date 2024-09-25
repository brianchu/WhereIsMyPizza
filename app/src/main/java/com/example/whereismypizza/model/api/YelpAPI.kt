package com.example.whereismypizza.model.api

import com.example.whereismypizza.model.Businesses
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YelpAPI {
    @GET("businesses/search")
    suspend fun getRestaurant(
        @Header("Authorization") authorization: String,
        @Query("term") searchTerm: String,
        @Query("location") location: String
    ): Businesses
}

