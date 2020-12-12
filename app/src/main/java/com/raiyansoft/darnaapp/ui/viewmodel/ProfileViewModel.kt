package com.raiyansoft.darnaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.darnaapp.model.general.FullGeneral
import com.raiyansoft.darnaapp.model.profile.Profile
import com.raiyansoft.darnaapp.repositories.ApiRepository
import com.raiyansoft.darnaapp.uitl.Commons
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataProfile = MutableLiveData<FullGeneral<Profile>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.UserToken, "")!!

    private suspend fun getProfile() {
        val response = repository.getProfile(lang, token)
        if (response.isSuccessful){
            dataProfile.postValue(response.body())
        }
    }

    fun getData(){
        viewModelScope.launch {
            getProfile()
        }
    }

    init {
        getData()
    }

}