package com.raiyansoft.darnaapp.model.categories

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("subCategory")
    val subCategory: Int
)