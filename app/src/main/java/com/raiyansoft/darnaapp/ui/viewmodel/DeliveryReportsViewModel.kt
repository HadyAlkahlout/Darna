package com.raiyansoft.darnaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.darnaapp.model.categories.Category
import com.raiyansoft.darnaapp.model.general.FullGeneral
import com.raiyansoft.darnaapp.model.general.FullPagingGeneral
import com.raiyansoft.darnaapp.model.orders.Order
import com.raiyansoft.darnaapp.repositories.ApiRepository
import com.raiyansoft.darnaapp.uitl.Commons
import kotlinx.coroutines.launch

class DeliveryReportsViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataComplete = MutableLiveData<FullPagingGeneral<Order>>()
    val dataCanceled = MutableLiveData<FullPagingGeneral<Order>>()
    val dataRefused = MutableLiveData<FullPagingGeneral<Order>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.UserToken, "")!!

    private suspend fun getComplete(from: String, to: String, page: Int) {
        val response = repository.completeDeliveryOrder(lang, token, from, to, page)
        if (response.isSuccessful){
            dataComplete.postValue(response.body())
        }
    }

    private suspend fun getCanceled(from: String, to: String, page: Int) {
        val response = repository.canceledDeliveryOrder(lang, token, from, to, page)
        if (response.isSuccessful){
            dataCanceled.postValue(response.body())
        }
    }

    private suspend fun getRefused(from: String, to: String, page: Int) {
        val response = repository.refusedDeliveryOrder(lang, token, from, to, page)
        if (response.isSuccessful){
            dataRefused.postValue(response.body())
        }
    }

    fun completeOrder(from: String, to: String, page: Int){
        viewModelScope.launch {
            getComplete(from, to, page)
        }
    }

    fun canceledOrder(from: String, to: String, page: Int){
        viewModelScope.launch {
            getCanceled(from, to, page)
        }
    }

    fun refusedOrder(from: String, to: String, page: Int){
        viewModelScope.launch {
            getRefused(from, to, page)
        }
    }

}