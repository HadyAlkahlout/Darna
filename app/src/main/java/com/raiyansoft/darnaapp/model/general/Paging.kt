package com.raiyansoft.darnaapp.model.general

import com.google.gson.annotations.SerializedName

data class Paging<T>(
    @SerializedName("count")
    val count: Int,
    @SerializedName("count_total")
    val count_total: Int,
    @SerializedName("data")
    val `data`: List<T>,
    @SerializedName("pages")
    val pages: Int
)