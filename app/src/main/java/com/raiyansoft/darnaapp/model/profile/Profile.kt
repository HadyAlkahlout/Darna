package com.raiyansoft.darnaapp.model.profile

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("address_id")
    val address_id: Int,
    @SerializedName("address_name")
    val address_name: Int,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("cat_id")
    val cat_id: Int,
    @SerializedName("company_name")
    val company_name: String,
    @SerializedName("country_code")
    val country_code: String,
    @SerializedName("day_from")
    val day_from: Int,
    @SerializedName("day_to")
    val day_to: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("end_at")
    val end_at: Any,
    @SerializedName("market_status")
    val market_status: Int,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("open_from")
    val open_from: String,
    @SerializedName("open_to")
    val open_to: String,
    @SerializedName("orders")
    val orders: Int,
    @SerializedName("subCats")
    val subCats: List<SubCat>,
    @SerializedName("type")
    val type: String,
    @SerializedName("user_id")
    val user_id: Int,
    @SerializedName("market_name")
    val market_name: String,
    @SerializedName("branch_name")
    val branch_name: String,
    @SerializedName("branch_id")
    val branch_id: Int
)