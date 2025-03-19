package com.maran.androidalfa.data.repositories

import android.util.Log
import com.maran.androidalfa.domain.entities.Book
import com.maran.androidalfa.data.network.BooksApi
import com.maran.androidalfa.domain.mappers.NetworkModelToEntity
import com.maran.androidalfa.domain.repositories.IBooksRepository
import javax.inject.Inject

class BookRemoteRepository @Inject constructor(private val booksApi: BooksApi) : IBooksRepository {
    override suspend fun getBookByQuery(query: String): Result<List<Book>> {
        try {
            val response = booksApi.searchBooks(query)
            val body = response.body()
            return if (response.isSuccessful && body != null) {
                Result.success(
                    body.let { it.results.map { book -> NetworkModelToEntity.mapBook(book) } }
                )
            } else {
                Result.failure(Exception(response.message() ?: ""))
            }
        } catch (e: Exception) {
            return Result.failure(Exception("Error"))
        }
    }

    override suspend fun getAllBooks(): Result<List<Book>> {
        try {
            val response = booksApi.getAllBooks()
            return if (response.isSuccessful && response.body() != null) {
                Result.success(
                    response.body()
                    !!.let { it.results.map { book -> NetworkModelToEntity.mapBook(book) } }
                )
            } else {
                Result.failure(Exception(response.message() ?: ""))
            }
        } catch (e: Exception) {
            return Result.failure(Exception("Error"))
        }
    }
}