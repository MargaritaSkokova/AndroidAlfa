package com.maran.androidalfa.presentation.books

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maran.androidalfa.domain.entities.Book
import com.maran.androidalfa.domain.useCases.GetAllBooksUseCase
import com.maran.androidalfa.domain.useCases.GetBooksByQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class LoadingStatus {
    IN_PROGRESS,
    DONE,
    NONE,
    FAILURE,
}

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val getBooksByQueryUseCase: GetBooksByQueryUseCase
) :
    ViewModel() {
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    val currentBooksList: MutableLiveData<List<Book>> by lazy {
        MutableLiveData<List<Book>>()
    }

    val currentLoadingState: MutableLiveData<LoadingStatus> by lazy {
        MutableLiveData<LoadingStatus>(LoadingStatus.NONE)
    }

    fun getAllBooks() {
        coroutineScope.launch {
            currentLoadingState.value = LoadingStatus.IN_PROGRESS
            getAllBooksUseCase.invoke()
                .onSuccess {
                    currentBooksList.value = it
                    currentLoadingState.value = LoadingStatus.DONE
                }
                .onFailure {
                    currentBooksList.value = emptyList()
                    currentLoadingState.value = LoadingStatus.FAILURE
                }
        }
    }

    fun getBooksWithQuery(query: String) {
        coroutineScope.launch {
            currentLoadingState.value = LoadingStatus.IN_PROGRESS
            getBooksByQueryUseCase.invoke(query)
                .onSuccess {
                    currentBooksList.value = it
                    currentLoadingState.value = LoadingStatus.DONE
                }
                .onFailure {
                    currentBooksList.value = emptyList()
                    currentLoadingState.value = LoadingStatus.FAILURE
                }
        }
    }
}