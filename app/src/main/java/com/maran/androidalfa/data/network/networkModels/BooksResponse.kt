package com.maran.androidalfa.data.network.networkModels

import kotlinx.serialization.Serializable

@Serializable
data class BooksResponse(val results: List<BookModel>)