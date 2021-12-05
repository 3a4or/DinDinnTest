package com.example.dindinn.data.network

import com.example.dindinn.data.entities.categories.CategoriesResponse
import com.example.dindinn.data.entities.ingredients.IngredientsResponse
import com.example.dindinn.data.entities.search.SearchResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseRepository @Inject constructor(val api: Api) {

    fun getSearchResults(word: String) : Observable<SearchResponse?>? {
        return api.getSearchResults(word.lowercase(Locale.getDefault()).trim())
            .debounce(1500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getCategories() : Observable<CategoriesResponse?>? {
        return api.getCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getIngredients(id: String) : Observable<IngredientsResponse?>? {
        return api.getIngredients(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}