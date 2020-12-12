package com.raiyansoft.darnaapp.model.profile

import com.google.gson.annotations.SerializedName

data class SubCat(
    @SerializedName("cat")
    val cat: String,
    @SerializedName("id")
    val id: Int
)