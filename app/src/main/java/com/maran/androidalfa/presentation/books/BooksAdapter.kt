package com.maran.androidalfa.presentation.books

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.maran.androidalfa.R
import com.maran.androidalfa.domain.entities.Book
import com.maran.androidalfa.presentation.ui.BookView

class BooksAdapter : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    private val books = mutableListOf<Book>()

    fun setBooks(newList: List<Book>) {
        books.clear()
        books.addAll(newList)
        notifyDataSetChanged()
    }

    class BookViewHolder(val view: BookView) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = BookView(parent.context, null)
        view.backgroundColorValue = ContextCompat.getColor(parent.context, R.color.md_theme_primaryContainer)
        view.contentColor = ContextCompat.getColor(parent.context, R.color.md_theme_onPrimaryContainer)
        view.titleColor = ContextCompat.getColor(parent.context, R.color.md_theme_primary)
        return BookViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: BookViewHolder, position: Int) {
        viewHolder.view.author = books[position].authors.joinToString(" ") { author -> author.name }
        viewHolder.view.subject = books[position].subjects.joinToString(", ")
        viewHolder.view.title = books[position].title
    }


    override fun getItemCount() = books.size

}