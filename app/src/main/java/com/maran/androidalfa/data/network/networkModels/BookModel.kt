package com.maran.androidalfa.data.network.networkModels

import kotlinx.serialization.Serializable

@Serializable
data class BookModel (
    val id: Long,
    val title: String,
    val authors: List<AuthorModel>,
    val subjects: List<String>
)