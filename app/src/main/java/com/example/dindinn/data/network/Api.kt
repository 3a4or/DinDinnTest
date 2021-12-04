package com.example.dindinn.data.network

import com.example.dindinn.data.entities.SearchResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("search.php?i=")
    fun getSearchResults(@Query("i") i: String): Observable<SearchResponse?>
}