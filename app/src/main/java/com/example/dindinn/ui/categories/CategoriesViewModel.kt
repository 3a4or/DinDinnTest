package com.example.dindinn.ui.categories

import androidx.lifecycle.MutableLiveData
import com.example.dindinn.base.BaseViewModel
import com.example.dindinn.data.network.RetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class CategoriesViewModel : BaseViewModel() {

    var searchQuery = MutableLiveData("")
    var autoCompleteList = MutableLiveData<List<String>>(mutableListOf())

    fun autoComplete(word: String) {

        RetrofitClient.instance.getSearchResults(word.lowercase(Locale.getDefault()).trim())

            .debounce(1500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                response?.apply {
                    autoCompleteList.value = this.ingredients.map { it.toString() }
                }
            }
    }
}