package com.maran.androidalfa.domain.useCases

import com.maran.androidalfa.domain.repositories.IBooksRepository
import javax.inject.Inject

class GetBooksByQueryUseCase @Inject constructor(private val booksRepository: IBooksRepository) {
    suspend fun invoke(query: String) = booksRepository.getBookByQuery(query)
}