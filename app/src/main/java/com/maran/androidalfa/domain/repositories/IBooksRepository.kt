package com.maran.androidalfa.domain.repositories

import com.maran.androidalfa.domain.entities.Book

interface IBooksRepository {
    suspend fun getBookByQuery(query: String): Result<List<Book>>
    suspend fun getAllBooks(): Result<List<Book>>
}