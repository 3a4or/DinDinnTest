package com.example.dindinn.ui.ingredients

import com.example.dindinn.base.BaseViewModel
import com.example.dindinn.data.entities.ingredients.Ingredient
import com.example.dindinn.data.network.BaseRepository
import com.example.dindinn.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IngredientsViewModel @Inject constructor(private val repo: BaseRepository) : BaseViewModel() {

    var ingredientsList = SingleLiveEvent<List<Ingredient>>()

    fun getIngredients(id: String) {
        dataLoading.value = true
        repo.getIngredients(id)?.subscribe(
                { response -> response?.apply {
                    dataLoading.value = false
                    ingredientsList.value = this.drinks
                }
                },
                { throwable ->
                    dataLoading.value = false
                    error.value = throwable.message
                }
            )
    }
}