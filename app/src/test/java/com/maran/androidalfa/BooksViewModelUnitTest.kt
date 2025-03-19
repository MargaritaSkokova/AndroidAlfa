package com.maran.androidalfa

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.maran.androidalfa.domain.entities.Author
import com.maran.androidalfa.domain.entities.Book
import com.maran.androidalfa.domain.repositories.IBooksRepository
import com.maran.androidalfa.domain.useCases.GetAllBooksUseCase
import com.maran.androidalfa.domain.useCases.GetBooksByQueryUseCase
import com.maran.androidalfa.presentation.books.BooksViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor

@RunWith(MockitoJUnitRunner::class)
class BooksViewModelUnitTest {
    private val testString = "Test"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var booksRepository: IBooksRepository
    private lateinit var allBooksUseCase: GetAllBooksUseCase
    private lateinit var queryUseCase: GetBooksByQueryUseCase
    private lateinit var booksViewModel: BooksViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.initMocks(this)
        allBooksUseCase = GetAllBooksUseCase(booksRepository)
        queryUseCase = GetBooksByQueryUseCase(booksRepository)
        booksViewModel = BooksViewModel(allBooksUseCase, queryUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllBooksInViewModelShouldReturnStringWithBooks() = runTest {
        // Arrange
        val book = Book(0, testString, listOf(Author(testString)), listOf(testString))
        Mockito.`when`(booksRepository.getAllBooks()).thenReturn(Result.success(listOf(book)))
        booksViewModel.initialize(testString, testString, testString, testString)

        // Act
        val observer = mock<Observer<String>>()
        booksViewModel.currentBooksList.observeForever(observer)
        booksViewModel.getAllBooks()
        advanceUntilIdle()

        // Assert
        verify(observer).onChanged(booksViewModel.convertBooksToString(listOf(book)))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getBooksWithCorrectQueryInViewModelShouldReturnStringWithBooks() = runTest {
        // Arrange
        val book = Book(0, testString, listOf(Author(testString)), listOf(testString))
        Mockito.`when`(booksRepository.getBookByQuery(testString)).thenReturn(Result.success(listOf(book)))
        booksViewModel.initialize(testString, testString, testString, testString)

        // Act
        val observer = mock<Observer<String>>()
        booksViewModel.currentBooksList.observeForever(observer)
        booksViewModel.getBooksWithQuery(testString)
        advanceUntilIdle()

        // Assert
        verify(observer).onChanged(booksViewModel.convertBooksToString(listOf(book)))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getBooksWithIncorrectQueryInViewModelShouldNotReturnStringWithBooks() = runTest {
        // Arrange
        val notFoundString = "Not Found"
        Mockito.`when`(booksRepository.getBookByQuery("")).thenReturn(Result.failure(Exception(notFoundString)))
        booksViewModel.initialize(testString, testString, testString, notFoundString)

        // Act
        val observer = mock<Observer<String>>()
        booksViewModel.currentBooksList.observeForever(observer)
        booksViewModel.getBooksWithQuery("")
        advanceUntilIdle()

        // Assert
        verify(observer).onChanged(notFoundString)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getBooksFailureWithEmptyMessageReturnsNotFoundString() = runTest {
        // Arrange
        val notFoundString = "Not Found"
        Mockito.`when`(booksRepository.getBookByQuery("")).thenReturn(Result.failure(Exception()))
        booksViewModel.initialize(testString, testString, testString, notFoundString)

        // Act
        val observer = mock<Observer<String>>()
        booksViewModel.currentBooksList.observeForever(observer)
        booksViewModel.getBooksWithQuery("")
        advanceUntilIdle()

        // Assert
        verify(observer).onChanged(notFoundString)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getBooksEmptyListReturnsNotFoundString() = runTest {
        // Arrange
        val notFoundString = "Not Found"
        Mockito.`when`(booksRepository.getBookByQuery(testString)).thenReturn(Result.success(listOf()))
        booksViewModel.initialize(testString, testString, testString, notFoundString)

        // Act
        val observer = mock<Observer<String>>()
        booksViewModel.currentBooksList.observeForever(observer)
        booksViewModel.getBooksWithQuery(testString)
        advanceUntilIdle()

        // Assert
        verify(observer).onChanged(notFoundString)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllBooksShouldUpdateLiveData() = runTest {
        // Arrange
        val oldBook = Book(0, testString, listOf(Author(testString)), listOf(testString))
        val newBook = Book(1, testString, listOf(Author(testString)), listOf(testString))
        Mockito.`when`(booksRepository.getAllBooks())
            .thenReturn(Result.success(listOf(oldBook)))
            .thenReturn(Result.success(listOf(newBook)))

        booksViewModel.initialize(testString, testString, testString, testString)

        val observer = mock<Observer<String>>()
        booksViewModel.currentBooksList.observeForever(observer)

        // Act
        booksViewModel.getAllBooks()
        advanceUntilIdle()
        booksViewModel.getAllBooks()
        advanceUntilIdle()

        // Assert
        val captor = argumentCaptor<String>()
        verify(observer, atLeastOnce()).onChanged(captor.capture())
        assert(captor.lastValue == booksViewModel.convertBooksToString(listOf(newBook)))
    }
}