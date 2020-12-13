package com.raiyansoft.darnaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.darnaapp.model.general.General
import com.raiyansoft.darnaapp.repositories.ApiRepository
import com.raiyansoft.darnaapp.uitl.Commons
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CompleteRegisterViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataComplete = MutableLiveData<General>()
    val dataDeliveryComplete = MutableLiveData<General>()
    val dataUpdate = MutableLiveData<General>()
    val dataStatus = MutableLiveData<General>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.UserToken, "")!!

    private suspend fun sendData(params: Map<String, RequestBody>) {
        val response = repository.completeRegister(lang, token, params)
        if (response.isSuccessful){
            dataComplete.postValue(response.body())
        }
    }

    private suspend fun sendDeliveryData(params: Map<String, RequestBody>) {
        val response = repository.completeDeliveryRegister(lang, token, params)
        if (response.isSuccessful){
            dataDeliveryComplete.postValue(response.body())
        }
    }

    private suspend fun update(params: Map<String, RequestBody>, image: MultipartBody.Part) {
        val response = repository.updateMarketInfo(lang, token, params, image)
        if (response.isSuccessful){
            dataUpdate.postValue(response.body())
        }
    }

    private suspend fun status(params: Map<String, RequestBody>) {
        val response = repository.changeMarketStatus(lang, token, params)
        if (response.isSuccessful){
            dataStatus.postValue(response.body())
        }
    }

    fun completeRegister(params: Map<String, RequestBody>){
        viewModelScope.launch {
            sendData(params)
        }
    }

    fun completeDeliveryRegister(params: Map<String, RequestBody>){
        viewModelScope.launch {
            sendDeliveryData(params)
        }
    }

    fun updateMarketInfo(params: Map<String, RequestBody>, image: MultipartBody.Part){
        viewModelScope.launch {
            update(params, image)
        }
    }

    fun changeMarketStatus(params: Map<String, RequestBody>){
        viewModelScope.launch {
            status(params)
        }
    }

}