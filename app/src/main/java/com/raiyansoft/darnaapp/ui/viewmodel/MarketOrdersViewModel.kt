package com.raiyansoft.darnaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.darnaapp.model.general.FullGeneral
import com.raiyansoft.darnaapp.model.general.General
import com.raiyansoft.darnaapp.model.orderDetails.OrderDetails
import com.raiyansoft.darnaapp.model.orders.Orders
import com.raiyansoft.darnaapp.model.setting.*
import com.raiyansoft.darnaapp.repositories.ApiRepository
import com.raiyansoft.darnaapp.uitl.Commons
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class MarketOrdersViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataOrders = MutableLiveData<FullGeneral<Orders>>()
    val dataDetails = MutableLiveData<FullGeneral<OrderDetails>>()
    val dataStatus = MutableLiveData<General>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.UserToken, "")!!

    private suspend fun getOrders(){
        val response = repository.getMarketOrders(lang,token)
        if (response.isSuccessful){
            dataOrders.postValue(response.body())
        }
    }

    private suspend fun details(id: Int){
        val response = repository.marketOrderDetails(lang,token, id)
        if (response.isSuccessful){
            dataDetails.postValue(response.body())
        }
    }

    private suspend fun status(params: Map<String, RequestBody>){
        val response = repository.changeOrderStatus(lang,token, params)
        if (response.isSuccessful){
            dataStatus.postValue(response.body())
        }
    }

    private fun getData() {
        viewModelScope.launch {
            getOrders()
        }
    }

    fun marketOrderDetails(id: Int) {
        viewModelScope.launch {
            details(id)
        }
    }

    fun changeOrderStatus(params: Map<String, RequestBody>) {
        viewModelScope.launch {
            status(params)
        }
    }

    init {
        getData()
    }

}