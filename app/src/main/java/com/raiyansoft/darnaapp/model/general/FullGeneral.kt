package com.raiyansoft.darnaapp.model.general

import com.google.gson.annotations.SerializedName

data class FullGeneral<T>(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: T,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String
)