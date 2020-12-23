package com.raiyansoft.darnaapp.model.driver

import com.google.gson.annotations.SerializedName

data class Driver(
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
    @SerializedName("name")
    val name: String,
    @SerializedName("region_id")
    val region_id: Int,
    @SerializedName("title")
    val title: String,
    var isSelect: Boolean = false
)