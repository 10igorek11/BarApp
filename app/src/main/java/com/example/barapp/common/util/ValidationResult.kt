package com.example.barapp.common.util

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)