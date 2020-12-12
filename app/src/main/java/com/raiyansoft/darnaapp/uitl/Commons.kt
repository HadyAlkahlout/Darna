package com.raiyansoft.darnaapp.uitl

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.*

object Commons {

    const val LANGUAGE = "lang"
    const val SpecialMarket = "special_market"
    const val SpecialProduct = "special_product"
    const val MarketUpgrade = "market_upgrade"
    const val Facebook = "facebook"
    const val Twitter = "twitter"
    const val Instagram = "instagram"
    const val UserToken = "token"
    const val UserId = "userId"
    const val UserName = "UserName"
    const val UserType = "userType"
    const val OpenLevel = "OpenLevel"
    const val StoreStatus = "storeStatus"

    fun setLocale(lang: String, context: Context) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources
            .updateConfiguration(config, context.resources.displayMetrics)
    }

    fun getSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE)

    fun getSharedEditor(context: Context) : SharedPreferences.Editor = getSharedPreferences(context).edit()

}