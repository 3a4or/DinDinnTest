package com.example.dindinn.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    val dataLoading = MutableLiveData(false)

    var error = MutableLiveData<String>()
}