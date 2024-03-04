package com.example.barapp.presentation.screens.techcard.techcard_edit_recipe

import android.content.Context
import com.example.barapp.domain.models.CocktailType
import com.example.barapp.domain.models.TechCardRecipeDto

sealed class TechCardEditScreenEvent{
    data class OnLoad(val id:Int, val context: Context): TechCardEditScreenEvent()
    data class SelectedTypeChanged(val type: CocktailType?): TechCardEditScreenEvent()
    data class OnApplicationScopeChange(val text:String): TechCardEditScreenEvent()
    data class OnRawMaterialRequirementsChange(val text:String): TechCardEditScreenEvent()
    data class OnTechnologicalProcessChange(val text:String): TechCardEditScreenEvent()
    data class OnRecipeAdd(val techCardRecipeDto: TechCardRecipeDto): TechCardEditScreenEvent()
    data class OnRecipeUpdate(val techCardRecipeDto: TechCardRecipeDto): TechCardEditScreenEvent()
    data class OnRecipeDelete(val techCardRecipeDto: TechCardRecipeDto): TechCardEditScreenEvent()
    data class OnConfirm(val context: Context): TechCardEditScreenEvent()
    data class OnNameChange(val name: String) : TechCardEditScreenEvent()
    object OnDelete: TechCardEditScreenEvent()
}
