package com.example.dindinn.ui.categories

import androidx.lifecycle.MutableLiveData
import com.example.dindinn.base.BaseViewModel
import com.example.dindinn.data.entities.categories.Drink
import com.example.dindinn.data.network.RetrofitClient
import com.example.dindinn.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class CategoriesViewModel : BaseViewModel() {

    var searchQuery = MutableLiveData("")
    var autoCompleteList = MutableLiveData<List<String>>(mutableListOf())
    var categoriesList = SingleLiveEvent<List<Drink>>()

    fun autoComplete(word: String) {
        RetrofitClient.instance.getSearchResults(word.lowercase(Locale.getDefault()).trim())
            .debounce(1500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response -> response?.apply {
                    autoCompleteList.value = this.ingredients.map { it.toString() }
                }
                },
                { throwable -> error.value = throwable.message }
            )
    }

    fun getCategories() {
        dataLoading.value = true
        RetrofitClient.instance.getCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response -> response?.apply {
                    dataLoading.value = false
                    categoriesList.value = this.drinks
                }
                },
                { throwable ->
                    dataLoading.value = false
                    error.value = throwable.message
                }
            )
    }
}