package com.maran.androidalfa.domain.entities

data class Book (
    val id: Long,
    val title: String,
    val authors: List<Author>,
    val subjects: List<String>
)