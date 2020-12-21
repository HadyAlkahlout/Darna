package com.raiyansoft.darnaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.darnaapp.model.general.FullGeneral
import com.raiyansoft.darnaapp.model.general.General
import com.raiyansoft.darnaapp.model.profile.Profile
import com.raiyansoft.darnaapp.repositories.ApiRepository
import com.raiyansoft.darnaapp.uitl.Commons
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DeliveryProfileViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataUpdate = MutableLiveData<General>()
    val dataAvatar = MutableLiveData<General>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.UserToken, "")!!

    private suspend fun avatar(avatar: MultipartBody.Part) {
        val response = repository.updateAvatar(lang, token, avatar)
        if (response.isSuccessful){
            dataAvatar.postValue(response.body())
        }
    }

    private suspend fun update(params: Map<String, RequestBody>) {
        val response = repository.updateDeliveryProfile(lang, token, params)
        if (response.isSuccessful){
            dataUpdate.postValue(response.body())
        }
    }

    fun updateAvatar(avatar: MultipartBody.Part){
        viewModelScope.launch {
            avatar(avatar)
        }
    }

    fun updateProfile(params: Map<String, RequestBody>){
        viewModelScope.launch {
            update(params)
        }
    }

}