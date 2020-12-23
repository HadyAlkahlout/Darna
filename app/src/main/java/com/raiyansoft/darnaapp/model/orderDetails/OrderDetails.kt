package com.raiyansoft.darnaapp.model.orderDetails

import com.google.gson.annotations.SerializedName
import com.raiyansoft.darnaapp.model.product.Product

data class OrderDetails(
    @SerializedName("date")
    val date: String,
    @SerializedName("delivery_cost")
    val delivery_cost: String,
    @SerializedName("discount")
    val discount: Int,
    @SerializedName("from_date")
    val from_date: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("market_name")
    val market_name: String,
    @SerializedName("market_location")
    val market_location: String,
    @SerializedName("payment")
    val payment: String,
    @SerializedName("payment_id")
    val payment_id: Int,
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("status")
    val status: String,
    @SerializedName("status_id")
    val status_id: Int,
    @SerializedName("total")
    val total: String,
    @SerializedName("user_name")
    val user_name: String,
    @SerializedName("user_location")
    val user_location: String,
    @SerializedName("receive_time")
    val receive_time: String,
    @SerializedName("delivery_time")
    val delivery_time: String,
    @SerializedName("time")
    val time: Int
)