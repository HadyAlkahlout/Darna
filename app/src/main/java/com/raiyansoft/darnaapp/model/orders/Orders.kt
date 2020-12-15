package com.raiyansoft.darnaapp.model.orders

import com.google.gson.annotations.SerializedName

data class Orders(
    @SerializedName("three_days")
    val three_days: List<Order>,
    @SerializedName("today")
    val today: List<Order>,
    @SerializedName("tomorrow")
    val tomorrow: List<Order>,
    @SerializedName("two_days")
    val two_days: List<Order>
)