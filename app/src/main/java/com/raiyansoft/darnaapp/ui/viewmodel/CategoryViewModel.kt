package com.raiyansoft.darnaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.darnaapp.model.categories.Category
import com.raiyansoft.darnaapp.model.city.City
import com.raiyansoft.darnaapp.model.general.FullGeneral
import com.raiyansoft.darnaapp.model.general.General
import com.raiyansoft.darnaapp.repositories.ApiRepository
import com.raiyansoft.darnaapp.uitl.Commons
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataCategory = MutableLiveData<FullGeneral<List<Category>>>()
    val dataInternalCategories = MutableLiveData<FullGeneral<List<Category>>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.UserToken, "")!!

    private suspend fun getCategories() {
        val response = repository.getCategories(lang, token)
        if (response.isSuccessful){
            dataCategory.postValue(response.body())
        }
    }

    private suspend fun getInternalCategories() {
        val response = repository.getInternalCategories(lang, token)
        if (response.isSuccessful){
            dataInternalCategories.postValue(response.body())
        }
    }

    private fun getData(){
        viewModelScope.launch {
            getCategories()
            getInternalCategories()
        }
    }

    init {
        getData()
    }

}