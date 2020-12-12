package com.raiyansoft.darnaapp.model.setting.faq

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("question")
    val question: String
)