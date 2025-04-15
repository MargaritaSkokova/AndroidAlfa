package com.maran.androidalfa.presentation.books

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.maran.androidalfa.R
import com.maran.androidalfa.domain.entities.Book
import com.maran.androidalfa.presentation.ui.VerticalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksFragment : Fragment() {

    private val viewModel: BooksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_books, container, false)

        val getAllBooksButton = view.findViewById<Button>(R.id.all_books_button)
        val getBooksWithQueryButton = view.findViewById<Button>(R.id.query_books_button)
        val textField = view.findViewById<TextInputLayout>(R.id.text_field)
        val editText = view.findViewById<EditText>(R.id.edit_text)
        val recyclerView = view.findViewById<RecyclerView>(R.id.scroll_view)
        val failureText = view.findViewById<TextView>(R.id.failure_status)
        val adapter = BooksAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(VerticalSpaceItemDecoration(36))

        val progressBar = view.findViewById<ProgressBar>(R.id.progress_circular)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (editText.text.isEmpty()) {
                    textField.error = getString(R.string.query_empty)
                } else {
                    textField.error = ""
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        val booksObserver = Observer<List<Book>> { books ->
            adapter.setBooks(books)
        }
        viewModel.currentBooksList.observe(viewLifecycleOwner, booksObserver)

        val statusObserver = Observer<LoadingStatus> { status ->
            when (status) {
                LoadingStatus.NONE -> {}
                LoadingStatus.IN_PROGRESS -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.INVISIBLE
                    failureText.visibility = View.INVISIBLE
                }

                LoadingStatus.DONE -> {
                    progressBar.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                    failureText.visibility = View.INVISIBLE
                }

                LoadingStatus.FAILURE -> {
                    progressBar.visibility = View.INVISIBLE
                    recyclerView.visibility = View.INVISIBLE
                    failureText.visibility = View.VISIBLE
                }
            }
        }

        viewModel.currentLoadingState.observe(viewLifecycleOwner, statusObserver)

        getAllBooksButton.setOnClickListener {
            viewModel.getAllBooks()
        }

        getBooksWithQueryButton.setOnClickListener {
            if (editText.text.isEmpty()) {
                textField.error = getString(R.string.query_empty)
            } else {
                textField.error = ""
                viewModel.getBooksWithQuery(editText.text.toString())
            }
        }

        return view
    }
}