package com.example.dindinn.ui.ingredients

import com.example.dindinn.base.BaseViewModel
import com.example.dindinn.data.entities.ingredients.Ingredient
import com.example.dindinn.data.network.RetrofitClient
import com.example.dindinn.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class IngredientsViewModel : BaseViewModel() {

    var ingredientsList = SingleLiveEvent<List<Ingredient>>()

    fun getIngredients(id: String) {
        dataLoading.value = true
        RetrofitClient.instance.getIngredients(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
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