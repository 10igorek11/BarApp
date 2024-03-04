package com.example.barapp.presentation.screens.techcard.techcard_edit_recipe

import com.example.barapp.domain.models.CocktailType
import com.example.barapp.domain.models.TechCard
import com.example.barapp.domain.models.TechCardRecipeDto

data class TechCardEditScreenState(
    var techCard: TechCard = TechCard(),
    val applicationScopeError: String? = null,
    val rawMaterialRequirementsError: String? = null,
    val technologicalProcessError: String? = null,
    val recipesError: String? = null,
    val initialRecipes: List<TechCardRecipeDto> = emptyList(),
    val recipes: List<TechCardRecipeDto> = emptyList(),
    var cocktailTypes:List<CocktailType> = emptyList(),
    var isLoading:Boolean = true,
    var isModified:Boolean = false,
)
