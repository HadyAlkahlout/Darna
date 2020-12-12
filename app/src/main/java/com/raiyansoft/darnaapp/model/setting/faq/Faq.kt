package com.raiyansoft.darnaapp.model.setting.faq

import com.google.gson.annotations.SerializedName

data class Faq(
    @SerializedName("data")
    val `data`: List<Question>
)