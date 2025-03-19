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
    NONE
}

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val getBooksByQueryUseCase: GetBooksByQueryUseCase
) :
    ViewModel() {
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    val currentBooksList: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val currentLoadingState: MutableLiveData<LoadingStatus> by lazy {
        MutableLiveData<LoadingStatus>(LoadingStatus.NONE)
    }

    private lateinit var titleString: String
    private lateinit var authorString: String
    private lateinit var subjectString: String
    private lateinit var notFoundString: String

    fun initialize(title: String, author: String, subject: String, notFound: String) {
        titleString = title
        authorString = author
        subjectString = subject
        notFoundString = notFound
    }

    fun getAllBooks() {
        coroutineScope.launch {
            currentLoadingState.value = LoadingStatus.IN_PROGRESS
            getAllBooksUseCase.invoke()
                .onSuccess {
                    currentBooksList.value = convertBooksToString(it)
                }
                .onFailure {
                    currentBooksList.value = it.message ?: notFoundString
                }
            currentLoadingState.value = LoadingStatus.DONE
        }
    }

    fun getBooksWithQuery(query: String) {
        coroutineScope.launch {
            currentLoadingState.value = LoadingStatus.IN_PROGRESS
            getBooksByQueryUseCase.invoke(query)
                .onSuccess {
                    currentBooksList.value = convertBooksToString(it)
                }
                .onFailure {
                    currentBooksList.value = it.message ?: notFoundString
                }
            currentLoadingState.value = LoadingStatus.DONE
        }
    }

    fun convertBooksToString(books: List<Book>): String {
        if (books.isEmpty()) return notFoundString

        val stringBuilder = StringBuilder()
        books.forEachIndexed { index, book ->
            stringBuilder.append(
                "${index + 1}. $titleString: ${book.title} \n${if (book.authors.isEmpty()) "" else "$authorString:"} ${
                    book.authors.map { author -> author.name }.joinToString(" ")
                } \n${if (book.subjects.isEmpty()) "" else "$subjectString:"}  ${
                    book.subjects.joinToString(
                        " "
                    )
                } \n\n"
            )
        }

        return stringBuilder.toString()
    }
}