package com.maran.androidalfa.data.network

import com.maran.androidalfa.data.network.networkModels.BooksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApi {
    @GET("/books")
    suspend fun searchBooks(@Query("search") query: String) : Response<BooksResponse>

    @GET("/books")
    suspend fun getAllBooks() : Response<BooksResponse>
}