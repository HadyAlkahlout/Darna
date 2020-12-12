package com.raiyansoft.darnaapp.model.setting

import com.google.gson.annotations.SerializedName

data class Privacy(
    @SerializedName("privacy")
    val privacy: String
)