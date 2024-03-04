package com.example.barapp.domain.usecases.validation

import android.content.Context
import com.example.barapp.R
import com.example.barapp.common.util.ValidationResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ValidateFirstNameUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(value: String) : ValidationResult{
        if(value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.required_field_error)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}