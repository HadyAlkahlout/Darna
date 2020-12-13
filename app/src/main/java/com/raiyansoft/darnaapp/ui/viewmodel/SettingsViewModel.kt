package com.raiyansoft.darnaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.darnaapp.model.general.FullGeneral
import com.raiyansoft.darnaapp.model.general.General
import com.raiyansoft.darnaapp.model.setting.*
import com.raiyansoft.darnaapp.model.setting.faq.Faq
import com.raiyansoft.darnaapp.repositories.ApiRepository
import com.raiyansoft.darnaapp.uitl.Commons
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataSettings = MutableLiveData<FullGeneral<Settings>>()
    val dataFaq = MutableLiveData<FullGeneral<Faq>>()
    val dataPrivacy = MutableLiveData<FullGeneral<Privacy>>()
    val dataConditions = MutableLiveData<FullGeneral<Conditions>>()
    val dataAbout = MutableLiveData<FullGeneral<About>>()
    val dataCall = MutableLiveData<General>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.UserToken, "")!!

    private suspend fun getSettings(){
        val response = repository.getSettings()
        if (response.isSuccessful){
            dataSettings.postValue(response.body())
        }
    }

    private suspend fun getFaq() {
        val response = repository.getFaq(lang)
        if (response.isSuccessful){
            dataFaq.postValue(response.body())
        }
    }

    private suspend fun getPrivacy() {
        val response = repository.getPolicy(lang)
        if (response.isSuccessful){
            dataPrivacy.postValue(response.body())
        }
    }

    private suspend fun getConditions() {
        val response = repository.getTerms(lang)
        if (response.isSuccessful){
            dataConditions.postValue(response.body())
        }
    }

    private suspend fun getAbout() {
        val response = repository.aboutUs(lang)
        if (response.isSuccessful){
            dataAbout.postValue(response.body())
        }
    }

    private suspend fun callUs(call: CallData) {
        val response = repository.callUs(lang, token, call)
        if (response.isSuccessful){
            dataCall.postValue(response.body())
        }
    }

    fun getData() {
        viewModelScope.launch {
            getSettings()
            getFaq()
            getPrivacy()
            getConditions()
            getAbout()
        }
    }

    fun sendCall(call: CallData){
        viewModelScope.launch {
            callUs(call)
        }
    }

    init {
        getData()
    }

}