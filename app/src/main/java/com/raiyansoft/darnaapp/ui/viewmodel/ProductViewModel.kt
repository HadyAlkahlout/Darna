package com.raiyansoft.darnaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.darnaapp.model.general.FullGeneral
import com.raiyansoft.darnaapp.model.general.FullPagingGeneral
import com.raiyansoft.darnaapp.model.product.Product
import com.raiyansoft.darnaapp.model.profile.Profile
import com.raiyansoft.darnaapp.repositories.ApiRepository
import com.raiyansoft.darnaapp.uitl.Commons
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataProducts = MutableLiveData<FullPagingGeneral<Product>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.UserToken, "")!!

    private suspend fun products(page: Int) {
        val response = repository.getProducts(lang, token, page)
        if (response.isSuccessful){
            dataProducts.postValue(response.body())
        }
    }

    fun getProducts(page: Int){
        viewModelScope.launch {
            products(page)
        }
    }

}