package com.maran.androidalfa.data.network.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)

            if (!response.isSuccessful) {
                Log.e("NET_INTERCEPTOR", response.message)
            }

            return response
        } catch (e: Exception) {
            Log.e("NET_INTERCEPTOR", e.message ?: "Network error")
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_2)
                .code(500)
                .message(e.message ?: "Network error")
                .body("".toResponseBody(null))
                .build()
        }
    }
}