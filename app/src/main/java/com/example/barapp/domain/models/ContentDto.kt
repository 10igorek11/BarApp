package com.example.barapp.domain.models

data class ContentDto(
    val id: Int = 0,
    val photo: String = "",
    val materialId: Int = 0,
    val isDeleted: Boolean = false,
)
