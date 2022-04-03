package com.moneybox.minimb.data.networking

import com.moneybox.minimb.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Networking {
    private fun createClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(NoAuthenticationInterceptor())
            .build()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(createClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createService():ApiMoneyBox = createRetrofit().create(ApiMoneyBox::class.java)
}