package com.example.dindinn.data.network

import com.example.dindinn.MyApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    companion object {
        const val BASE_URL = "www.thecocktaildb.com/api/json/v1/1/"
        val instance: Api by lazy { HOLDER.INSTANCE }
    }

    private object HOLDER {
        val INSTANCE = webService()

        fun webService() : Api {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val  okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(59, TimeUnit.SECONDS)
                .readTimeout(59, TimeUnit.SECONDS)
                .writeTimeout(59, TimeUnit.SECONDS)
                .addInterceptor(NetworkInterceptor(MyApp.appContext))
                .addInterceptor(interceptor)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build().create(Api::class.java)
        }
    }
}