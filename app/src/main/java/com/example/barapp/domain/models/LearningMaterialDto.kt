package com.example.barapp.domain.models

data class LearningMaterialDto(
    val id: Int = 0,
    val title: String = "",
    val preview: String? = null,
    val text: String? = null,
    val contents: List<ContentDto> = emptyList(),
)
