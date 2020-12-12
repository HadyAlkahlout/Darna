package com.raiyansoft.darnaapp.model.city

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String
)