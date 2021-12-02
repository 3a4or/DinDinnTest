package com.example.dindinn.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dindinn.data.entities.Result
import com.example.dindinn.data.network.BaseRepository

open class BaseViewModel : ViewModel() {

    val repository = BaseRepository()

    var error = MutableLiveData<Result.BaseError>()
}