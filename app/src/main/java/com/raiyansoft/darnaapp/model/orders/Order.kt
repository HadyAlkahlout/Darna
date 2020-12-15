package com.raiyansoft.darnaapp.model.orders

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("date")
    val date: String,
    @SerializedName("from_date")
    val from_date: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("status_id")
    val status_id: Int,
    @SerializedName("time")
    val time: Int,
    @SerializedName("total_cost")
    val total_cost: String
)