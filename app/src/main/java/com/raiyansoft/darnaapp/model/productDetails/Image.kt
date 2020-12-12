package com.raiyansoft.darnaapp.model.productDetails

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String
)