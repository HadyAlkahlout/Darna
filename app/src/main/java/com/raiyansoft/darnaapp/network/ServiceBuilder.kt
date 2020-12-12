package com.raiyansoft.darnaapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceBuilder {

    companion object {

        private val baseURL = "https://darna-app.com/api/"
        var apis: Api? = null


        init {
            val client = OkHttpClient.Builder()
                .build()
            apis = getRetrofitInstance(client).create(Api::class.java)
        }

        fun getRetrofitInstance(client:OkHttpClient): Retrofit =
             Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

    }

}