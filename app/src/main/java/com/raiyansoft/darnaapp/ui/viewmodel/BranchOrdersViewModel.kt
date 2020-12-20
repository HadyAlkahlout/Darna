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

class BranchOrdersViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataOrders = MutableLiveData<FullGeneral<Orders>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.UserToken, "")!!

    private suspend fun getOrders(){
        val response = repository.getBranchOrders(lang,token)
        if (response.isSuccessful){
            dataOrders.postValue(response.body())
        }
    }

    private fun getData() {
        viewModelScope.launch {
            getOrders()
        }
    }

    init {
        getData()
    }

}