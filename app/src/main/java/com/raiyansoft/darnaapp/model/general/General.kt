package com.raiyansoft.darnaapp.model.general

import com.google.gson.annotations.SerializedName

data class General(
    @SerializedName("code")
    val code: Int,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String
)