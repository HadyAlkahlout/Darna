package com.raiyansoft.darnaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.darnaapp.model.driver.Driver
import com.raiyansoft.darnaapp.model.general.FullGeneral
import com.raiyansoft.darnaapp.model.general.General
import com.raiyansoft.darnaapp.repositories.ApiRepository
import com.raiyansoft.darnaapp.uitl.Commons
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class DriverViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataDrivers = MutableLiveData<FullGeneral<List<Driver>>>()
    val dataDelete = MutableLiveData<General>()
    val dataCreate = MutableLiveData<General>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.UserToken, "")!!

    private suspend fun getDrivers() {
        val response = repository.getDrivers(lang, token)
        if (response.isSuccessful){
            dataDrivers.postValue(response.body())
        }
    }

    private suspend fun delete(id: Int) {
        val response = repository.deleteDriver(lang, token, id)
        if (response.isSuccessful){
            dataDelete.postValue(response.body())
        }
    }

    private suspend fun create(params: Map<String, RequestBody>) {
        val response = repository.createDriver(lang, token, params)
        if (response.isSuccessful){
            dataCreate.postValue(response.body())
        }
    }

    fun getData(){
        viewModelScope.launch {
            getDrivers()
        }
    }

    fun deleteDriver(id: Int){
        viewModelScope.launch {
            delete(id)
        }
    }

    fun createDriver(params: Map<String, RequestBody>){
        viewModelScope.launch {
            create(params)
        }
    }

    init {
        getData()
    }

}