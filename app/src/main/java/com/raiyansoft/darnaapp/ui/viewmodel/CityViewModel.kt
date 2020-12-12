package com.raiyansoft.darnaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.darnaapp.model.city.City
import com.raiyansoft.darnaapp.model.general.FullGeneral
import com.raiyansoft.darnaapp.repositories.ApiRepository
import com.raiyansoft.darnaapp.uitl.Commons
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataCity = MutableLiveData<FullGeneral<List<City>>>()
    val dataRegions = MutableLiveData<FullGeneral<List<City>>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!

    private suspend fun getCities() {
        val response = repository.getCities(lang)
        if (response.isSuccessful){
            dataCity.postValue(response.body())
        }
    }

    private suspend fun getRegions(cityId: Int){
        val response = repository.getRegions(lang, cityId)
        if (response.isSuccessful){
            dataRegions.postValue(response.body())
        }
    }

    private fun getData() {
        viewModelScope.launch {
            getCities()
        }
    }

    fun region(id: Int){
        viewModelScope.launch {
            getRegions(id)
        }
    }

    init {
        getData()
    }

}