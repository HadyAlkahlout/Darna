package com.raiyansoft.darnaapp.model.product

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("category_id")
    val category_id: String,
    @SerializedName("category_title")
    val category_title: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("time")
    val time: Int,
    @SerializedName("title")
    val title: String
)