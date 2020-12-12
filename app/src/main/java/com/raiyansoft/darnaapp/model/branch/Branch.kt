package com.raiyansoft.darnaapp.model.branch

import com.google.gson.annotations.SerializedName

data class Branch(
    @SerializedName("city_id")
    val city_id: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("region_id")
    val region_id: Int,
    @SerializedName("title")
    val title: String
)