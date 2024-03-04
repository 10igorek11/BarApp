package com.example.barapp.domain.usecases.validation

import android.content.Context
import androidx.core.text.isDigitsOnly
import com.example.barapp.R
import com.example.barapp.common.util.ValidationResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ValidatePhoneUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(value: String) : ValidationResult{
        if(value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.required_field_error)
            )
        }
        if(value.length < 11) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.invalid_phone_format)
            )
        }
        if(value[0]!='8' && value.take(2)!="+7")  {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.invalid_phone_format)
            )
        }
        if(value[0]=='8' && value.length!=11)  {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.invalid_phone_format)
            )
        }
        if(value[0]=='8' && !value.isDigitsOnly())  {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.invalid_phone_format)
            )
        }
        if(value.take(2)=="+7" && value.length!=12)  {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.invalid_phone_format)
            )
        }
        if(value.take(2)=="+7" && !value.drop(1).isDigitsOnly())  {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.invalid_phone_format)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}