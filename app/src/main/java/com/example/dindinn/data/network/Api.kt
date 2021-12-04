package com.example.dindinn.data.network

import com.example.dindinn.data.entities.OrdersResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface Api {

    @GET("list.php?c=list")
    fun getOrders(): Observable<OrdersResponse?>
}