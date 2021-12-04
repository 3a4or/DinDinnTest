package com.example.dindinn.ui.orders

import com.example.dindinn.MyApp
import com.example.dindinn.base.BaseViewModel
import com.example.dindinn.data.entities.Data
import com.example.dindinn.data.entities.OrdersResponse
import com.example.dindinn.utils.SingleLiveEvent
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.InputStream

class OrdersViewModel : BaseViewModel() {

    var items = SingleLiveEvent<List<Data>>()

    init {
        items.value = emptyList()
    }

    fun getOrders() {
        Observable.just(readJSONFromAsset())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val orders = Gson().fromJson(it, OrdersResponse::class.java)
                items.value = orders.data
            }
    }

    private fun readJSONFromAsset(): String? {
        var json: String? = null
        try {
            val  inputStream: InputStream = MyApp.appContext.assets.open("orders.json")
            json = inputStream.bufferedReader().use{it.readText()}
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}