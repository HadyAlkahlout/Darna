package com.raiyansoft.darnaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.darnaapp.model.general.FullGeneral
import com.raiyansoft.darnaapp.model.general.FullPagingGeneral
import com.raiyansoft.darnaapp.model.general.General
import com.raiyansoft.darnaapp.model.product.Product
import com.raiyansoft.darnaapp.model.productDetails.ProductDetails
import com.raiyansoft.darnaapp.model.profile.Profile
import com.raiyansoft.darnaapp.repositories.ApiRepository
import com.raiyansoft.darnaapp.uitl.Commons
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataProducts = MutableLiveData<FullPagingGeneral<Product>>()
    val dataProductDetails = MutableLiveData<FullGeneral<ProductDetails>>()
    val dataQuantity = MutableLiveData<General>()
    val dataCreate = MutableLiveData<General>()
    val dataUpdate = MutableLiveData<General>()
    val dataDelete = MutableLiveData<General>()
    val dataDeleteImage = MutableLiveData<General>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.UserToken, "")!!

    private suspend fun products(page: Int) {
        val response = repository.getProducts(lang, token, page)
        if (response.isSuccessful){
            dataProducts.postValue(response.body())
        }
    }

    private suspend fun product(id: Int) {
        val response = repository.getProductDetails(lang, token, id)
        if (response.isSuccessful){
            dataProductDetails.postValue(response.body())
        }
    }

    private suspend fun updateQuantity(id: Int) {
        val response = repository.updateQuantity(lang, token, id)
        if (response.isSuccessful){
            dataQuantity.postValue(response.body())
        }
    }

    private suspend fun create(map : Map<String, RequestBody>, images: List<MultipartBody.Part>) {
        val response = repository.createProduct(lang, token, map, images)
        if (response.isSuccessful){
            dataCreate.postValue(response.body())
        }
    }

    private suspend fun update(map : Map<String, RequestBody>, images: List<MultipartBody.Part>) {
        val response = repository.updateProduct(lang, token, map, images)
        if (response.isSuccessful){
            dataUpdate.postValue(response.body())
        }
    }

    private suspend fun delete(id: Int) {
        val response = repository.deleteProduct(lang, token, id)
        if (response.isSuccessful){
            dataDelete.postValue(response.body())
        }
    }

    private suspend fun deleteIMG(id: Int) {
        val response = repository.deleteImage(lang, token, id)
        if (response.isSuccessful){
            dataDeleteImage.postValue(response.body())
        }
    }

    fun getProducts(page: Int){
        viewModelScope.launch {
            products(page)
        }
    }

    fun getProductDetails(id: Int){
        viewModelScope.launch {
            product(id)
        }
    }

    fun emptyQuantity(id: Int){
        viewModelScope.launch {
            updateQuantity(id)
        }
    }

    fun createProduct(map : Map<String, RequestBody>, images: List<MultipartBody.Part>){
        viewModelScope.launch {
            create(map, images)
        }
    }

    fun updateProduct(map : Map<String, RequestBody>, images: List<MultipartBody.Part>){
        viewModelScope.launch {
            update(map, images)
        }
    }

    fun deleteProduct(id: Int){
        viewModelScope.launch {
            delete(id)
        }
    }

    fun deleteImage(id: Int){
        viewModelScope.launch {
            deleteIMG(id)
        }
    }

}