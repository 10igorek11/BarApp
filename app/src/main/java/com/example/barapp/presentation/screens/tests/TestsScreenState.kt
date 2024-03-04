package com.example.barapp.presentation.screens.tests

import com.example.barapp.domain.models.PassingStatus
import com.example.barapp.domain.models.Test

data class TestsScreenState(
    var tests:List<TestItem> = emptyList(),
    var errorStatus:Boolean = false,
    var isLoading:Boolean = true
)
