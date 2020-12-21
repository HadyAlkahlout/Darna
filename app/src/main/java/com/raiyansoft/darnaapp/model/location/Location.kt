package com.raiyansoft.darnaapp.model.location

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("city_id")
    val city_id: Int,
    @SerializedName("default")
    val default: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("region_id")
    val region_id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_city")
    val title_city: String
)