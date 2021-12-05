package com.example.dindinn.di

import com.example.dindinn.MyApp
import com.example.dindinn.data.network.Api
import com.example.dindinn.data.network.NetworkInterceptor
import com.example.dindinn.utils.IdManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() : OkHttpClient{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .connectTimeout(59, TimeUnit.SECONDS)
            .readTimeout(59, TimeUnit.SECONDS)
            .writeTimeout(59, TimeUnit.SECONDS)
            .addInterceptor(NetworkInterceptor(MyApp.appContext))
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(okHttpClient: OkHttpClient): Api {
        return Retrofit.Builder()
            .baseUrl(IdManager.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build().create(Api::class.java)
    }
}