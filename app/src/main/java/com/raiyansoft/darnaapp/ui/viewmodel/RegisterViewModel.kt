package com.raiyansoft.darnaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.darnaapp.model.general.FullGeneral
import com.raiyansoft.darnaapp.model.general.General
import com.raiyansoft.darnaapp.model.register.Activation
import com.raiyansoft.darnaapp.model.register.Register
import com.raiyansoft.darnaapp.repositories.ApiRepository
import com.raiyansoft.darnaapp.uitl.Commons
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataRegister = MutableLiveData<FullGeneral<Register>>()
    val dataActivate = MutableLiveData<General>()
    val dataResend = MutableLiveData<General>()
    val dataLogout = MutableLiveData<General>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.UserToken, "")!!

    private suspend fun register(params: Map<String, RequestBody>){
        val response = repository.register(lang, params)
        if (response.isSuccessful){
            dataRegister.postValue(response.body())
        }
    }

    private suspend fun activate(activation: Activation){
        val response = repository.activate(lang, token, activation)
        if (response.isSuccessful){
            dataActivate.postValue(response.body())
        }
    }

    private suspend fun resend(){
        val response = repository.resendActivation(lang, token)
        if (response.isSuccessful){
            dataResend.postValue(response.body())
        }
    }

    private suspend fun logout(){
        val response = repository.logout(lang, token)
        if (response.isSuccessful){
            dataLogout.postValue(response.body())
        }
    }

    fun makeAccount(params: Map<String, RequestBody>) {
        viewModelScope.launch {
            register(params)
        }
    }

    fun activateAccount(activation: Activation) {
        viewModelScope.launch {
            activate(activation)
        }
    }

    fun resendActivation() {
        viewModelScope.launch {
            resend()
        }
    }

    fun exitAccount() {
        viewModelScope.launch {
            logout()
        }
    }

}