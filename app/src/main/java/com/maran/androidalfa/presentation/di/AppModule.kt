package com.maran.androidalfa.presentation.di

import com.maran.androidalfa.data.network.BooksApi
import com.maran.androidalfa.data.network.RetrofitClient
import com.maran.androidalfa.data.repositories.BookRemoteRepository
import com.maran.androidalfa.domain.repositories.IBooksRepository
import com.maran.androidalfa.domain.useCases.GetAllBooksUseCase
import com.maran.androidalfa.domain.useCases.GetBooksByQueryUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(value = [SingletonComponent::class])
abstract class AppModule {
    @Binds
    abstract fun booksRemoteRepositoryProvider(bookRemoteRepository: BookRemoteRepository): IBooksRepository

    companion object {
        @Provides
        @Singleton
        fun retrofitClientProvider(): Retrofit {
            return RetrofitClient.retrofitClient().newBuilder().build()
        }

        @Provides
        @Singleton
        fun booksApiProvider(retrofit: Retrofit): BooksApi {
            return retrofit.create(BooksApi::class.java)
        }

        @Provides
        @Singleton
        fun getBooksUseCaseProvider(booksRepository: IBooksRepository): GetAllBooksUseCase {
            return GetAllBooksUseCase(booksRepository)
        }

        @Provides
        @Singleton
        fun queryBooksUseCaseProvider(booksRepository: IBooksRepository): GetBooksByQueryUseCase {
            return GetBooksByQueryUseCase(booksRepository)
        }

    }
}