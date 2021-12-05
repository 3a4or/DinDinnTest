package com.example.dindinn.ui.categories

import androidx.lifecycle.MutableLiveData
import com.example.dindinn.base.BaseViewModel
import com.example.dindinn.data.entities.categories.Drink
import com.example.dindinn.data.network.BaseRepository
import com.example.dindinn.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val repo: BaseRepository) : BaseViewModel() {

    var searchQuery = MutableLiveData("")
    var autoCompleteList = MutableLiveData<List<String>>(mutableListOf())
    var categoriesList = SingleLiveEvent<List<Drink>>()

    fun autoComplete(word: String) {
        repo.getSearchResults(word.lowercase(Locale.getDefault()).trim())?.subscribe(
            { response -> response?.apply {
                autoCompleteList.value = this.ingredients.map { it.toString() }
            }
            },
            { throwable -> error.value = throwable.message }
        )
    }

    fun getCategories() {
        dataLoading.value = true
        repo.getCategories()?.subscribe(
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