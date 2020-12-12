package com.raiyansoft.darnaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.darnaapp.model.branch.Branch
import com.raiyansoft.darnaapp.model.categories.Category
import com.raiyansoft.darnaapp.model.city.City
import com.raiyansoft.darnaapp.model.general.FullGeneral
import com.raiyansoft.darnaapp.model.general.General
import com.raiyansoft.darnaapp.repositories.ApiRepository
import com.raiyansoft.darnaapp.uitl.Commons
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class BranchesViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ApiRepository()
    val dataBranches = MutableLiveData<FullGeneral<List<Branch>>>()
    val dataDelete = MutableLiveData<General>()
    val dataAdd = MutableLiveData<General>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.UserToken, "")!!

    private suspend fun getBranches() {
        val response = repository.getBranches(lang, token)
        if (response.isSuccessful){
            dataBranches.postValue(response.body())
        }
    }

    private suspend fun deleteBranch(id: Int) {
        val response = repository.deleteBranch(lang, token, id)
        if (response.isSuccessful){
            dataDelete.postValue(response.body())
        }
    }

    private suspend fun addBranch(params: Map<String, RequestBody>) {
        val response = repository.createBranch(lang, token, params)
        if (response.isSuccessful){
            dataAdd.postValue(response.body())
        }
    }

    fun add(params: Map<String, RequestBody>){
        viewModelScope.launch {
            addBranch(params)
        }
    }

    fun delete(id: Int){
        viewModelScope.launch {
            deleteBranch(id)
        }
    }

    fun getData(){
        viewModelScope.launch {
            getBranches()
        }
    }

    init {
        getData()
    }

}