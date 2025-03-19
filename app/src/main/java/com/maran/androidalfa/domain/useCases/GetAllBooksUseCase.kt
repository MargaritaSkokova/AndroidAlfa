package com.maran.androidalfa.domain.useCases

import com.maran.androidalfa.domain.repositories.IBooksRepository
import javax.inject.Inject

class GetAllBooksUseCase @Inject constructor(private val booksRepository: IBooksRepository) {
    suspend fun invoke() = booksRepository.getAllBooks()
}