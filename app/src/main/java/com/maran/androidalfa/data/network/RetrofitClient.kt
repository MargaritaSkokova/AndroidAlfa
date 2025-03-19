package com.maran.androidalfa.data.network

import com.google.gson.GsonBuilder
import com.maran.androidalfa.data.network.interceptors.LoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private var gson = GsonBuilder()
            .setLenient()
            .create()

        fun retrofitClient(): Retrofit = Retrofit.Builder()
            .baseUrl("https://gutendex.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient().build())
            .build()


        private fun okHttpClient() = OkHttpClient().newBuilder()
            .addInterceptor(LoggingInterceptor())
    }
}