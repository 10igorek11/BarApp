package com.example.barapp.data.remote.models

data class LearningMaterialDtoResponse(
    val id: Int,
    val title: String,
    val preview: String?,
    val text: String?,
    val contents: List<ContentDtoResponse>,
)
