package com.example.whereismypizza.model

import com.google.gson.annotations.SerializedName

data class Category(
    val alias: String,
    val title: String
)

data class Coordinate(
    val latitude: Double,
    val longitude: Double,
)

data class Location(
    val address1: String,
    val address2: String,
    val address3: String,
    val city: String,
    @SerializedName("zip_code")
    val zipCode: String,
    val country: String,
    val state: String,
    @SerializedName("display_address")
    val displayAddress: List<String>
)

data class Business(
    val id: String,
    val alias: String,
    val name: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("is_closed")
    val isClosed: Boolean,
    val url: String,
    @SerializedName("review_count")
    val reviewCount: Long,
    val categories: List<Category>,
    val rating: Double,
    val coordinates: Coordinate,
    val location: Location,
    val phone: String,
    @SerializedName("display_phone")
    val displayPhone: String,
    val distance: Double
    // ignore some other fields
)

data class Businesses(
    val businesses: List<Business>,
    val total : Long
    // ignore some other fields
)