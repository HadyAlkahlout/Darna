package com.raiyansoft.darnaapp.model.productDetails

import com.google.gson.annotations.SerializedName

data class ProductDetails(
    @SerializedName("category_id")
    val category_id: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("filters")
    val filters: List<Filter>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("images")
    val images: List<Image>,
    @SerializedName("market_id")
    val market_id: Int,
    @SerializedName("note")
    val note: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("quantity_type")
    val quantity_type: Int,
    @SerializedName("time")
    val time: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("user_id")
    val user_id: Int
)