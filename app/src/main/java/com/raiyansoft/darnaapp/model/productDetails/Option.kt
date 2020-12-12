package com.raiyansoft.darnaapp.model.productDetails

import com.google.gson.annotations.SerializedName

data class Option(
    @SerializedName("id")
    val id: Int,
    @SerializedName("price")
    val price: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_ar")
    val title_ar: String,
    @SerializedName("title_en")
    val title_en: String
)