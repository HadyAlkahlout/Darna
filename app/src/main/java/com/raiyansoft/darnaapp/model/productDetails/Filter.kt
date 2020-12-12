package com.raiyansoft.darnaapp.model.productDetails

import com.google.gson.annotations.SerializedName

data class Filter(
    @SerializedName("id")
    val id: Int,
    @SerializedName("options")
    val options: List<Option>,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_ar")
    val title_ar: String,
    @SerializedName("title_en")
    val title_en: String,
    @SerializedName("type")
    val type: String
)