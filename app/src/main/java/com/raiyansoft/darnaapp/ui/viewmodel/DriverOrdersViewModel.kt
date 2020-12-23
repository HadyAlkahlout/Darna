package com.raiyansoft.darnaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.darnaapp.model.general.FullGeneral
import com.raiyansoft.darnaapp.model.general.FullPagingGeneral
import com.raiyansoft.darnaapp.model.general.General
import com.raiyansoft.darnaapp.model.orderDetails.OrderDetails
import com.raiyansoft.darnaapp.model.orders.Order
import com.raiyansoft.darnaapp.model.orders.Orders
import com.raiyansoft.darnaapp.model.setting.*
import com.raiyansoft.darnaapp.repositories.ApiRepository
import com.raiyansoft.darnaapp.uitl.Commons
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class DriverOrdersViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataOrders = MutableLiveData<FullPagingGeneral<Order>>()
    val dataDetails = MutableLiveData<FullGeneral<OrderDetails>>()
    val dataStatus = MutableLiveData<General>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.UserToken, "")!!

    private suspend fun orders(page: Int){
        val response = repository.getDriverOrders(lang,token,page)
        if (response.isSuccessful){
            dataOrders.postValue(response.body())
        }
    }

    private suspend fun details(id: Int){
        val response = repository.driverDetails(lang,token, id)
        if (response.isSuccessful){
            dataDetails.postValue(response.body())
        }
    }

    private suspend fun status(params: Map<String, RequestBody>){
        val response = repository.changeStatusDriver(lang,token, params)
        if (response.isSuccessful){
            dataStatus.postValue(response.body())
        }
    }

    fun getOrders(page: Int) {
        viewModelScope.launch {
            orders(page)
        }
    }

    fun orderDetails(id: Int) {
        viewModelScope.launch {
            details(id)
        }
    }

    fun changeOrderStatus(params: Map<String, RequestBody>) {
        viewModelScope.launch {
            status(params)
        }
    }

}