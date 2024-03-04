package com.example.barapp.presentation.screens.techcard.techcard_screen

import com.example.barapp.domain.models.TechCard
import com.example.barapp.domain.models.TechCardDto
import com.example.barapp.domain.models.QuestionDto
import com.example.barapp.domain.models.Role
import com.example.barapp.domain.models.TechCardRecipeDto
import com.example.barapp.domain.models.TestDto
import com.example.barapp.domain.models.UserDto

data class TechCardScreenState(
    var techCard: TechCard = TechCard(),
    var recipes: List<TechCardRecipeDto> = emptyList(),
    var isLoading:Boolean = true,
)
