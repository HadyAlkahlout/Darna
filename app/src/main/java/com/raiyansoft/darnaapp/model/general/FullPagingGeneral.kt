package com.raiyansoft.darnaapp.model.general

import com.google.gson.annotations.SerializedName

data class FullPagingGeneral<T>(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: Paging<T>,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String
)