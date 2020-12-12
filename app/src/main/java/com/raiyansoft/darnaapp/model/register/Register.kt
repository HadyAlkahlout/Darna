package com.raiyansoft.darnaapp.model.register

import com.google.gson.annotations.SerializedName

data class Register(
    @SerializedName("token")
    val token: String,
    @SerializedName("user_id")
    val user_id: Int
)