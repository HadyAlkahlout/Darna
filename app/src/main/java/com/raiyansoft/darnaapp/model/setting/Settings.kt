package com.raiyansoft.darnaapp.model.setting

import com.google.gson.annotations.SerializedName

data class Settings(
    @SerializedName("android_version")
    val android_version: String,
    @SerializedName("facebook")
    val facebook: String,
    @SerializedName("force_close")
    val force_close: String,
    @SerializedName("force_update")
    val force_update: String,
    @SerializedName("instagram")
    val instagram: String,
    @SerializedName("market_upgrade")
    val market_upgrade: String,
    @SerializedName("special_market")
    val special_market: String,
    @SerializedName("special_product")
    val special_product: String,
    @SerializedName("twitter")
    val twitter: String
)