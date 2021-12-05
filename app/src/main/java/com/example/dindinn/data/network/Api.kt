package com.example.dindinn.data.network

import com.example.dindinn.data.entities.categories.CategoriesResponse
import com.example.dindinn.data.entities.ingredients.IngredientsResponse
import com.example.dindinn.data.entities.search.SearchResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("search.php?i=")
    fun getSearchResults(@Query("i") i: String): Observable<SearchResponse?>

    @GET("list.php?c=list")
    fun getCategories(): Observable<CategoriesResponse?>

    @GET("filter.php?c=")
    fun getIngredients(@Query("c") i: String): Observable<IngredientsResponse?>
}