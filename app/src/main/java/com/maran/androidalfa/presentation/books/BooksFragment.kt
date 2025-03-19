package com.maran.androidalfa.presentation.books

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import com.maran.androidalfa.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksFragment : Fragment() {

    private val viewModel: BooksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initialize(
            getString(R.string.name),
            getString(R.string.author),
            getString(R.string.subject),
            getString(R.string.not_found)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_books, container, false)

        val getAllBooksButton = view.findViewById<Button>(R.id.all_books_button)
        val getBooksWithQueryButton = view.findViewById<Button>(R.id.query_books_button)
        val textField = view.findViewById<TextInputLayout>(R.id.text_field)
        val editText = view.findViewById<EditText>(R.id.edit_text)
        val textList = view.findViewById<TextView>(R.id.books_holder)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_circular)
        val scrollView = view.findViewById<ScrollView>(R.id.scroll_view)

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

        val booksObserver = Observer<String> { books ->
            textList.text = books
        }

        viewModel.currentBooksList.observe(viewLifecycleOwner, booksObserver)

        val statusObserver = Observer<LoadingStatus> { status ->
            when (status) {
                LoadingStatus.NONE -> {}
                LoadingStatus.IN_PROGRESS -> {
                    progressBar.visibility = View.VISIBLE; scrollView.visibility = View.INVISIBLE
                }

                LoadingStatus.DONE -> {
                    progressBar.visibility = View.INVISIBLE; scrollView.visibility = View.VISIBLE
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