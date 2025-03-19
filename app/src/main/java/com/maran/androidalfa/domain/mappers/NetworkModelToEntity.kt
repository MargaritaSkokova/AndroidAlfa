package com.maran.androidalfa.domain.mappers

import com.maran.androidalfa.data.network.networkModels.AuthorModel
import com.maran.androidalfa.data.network.networkModels.BookModel
import com.maran.androidalfa.domain.entities.Author
import com.maran.androidalfa.domain.entities.Book

class NetworkModelToEntity {
    companion object {
        fun mapBook(model: BookModel): Book {
            return Book(
                model.id,
                model.title,
                model.authors.map { author -> mapAuthor(author) },
                model.subjects
            )
        }

        fun mapAuthor(model: AuthorModel): Author {
            return Author(model.name)
        }
    }
}