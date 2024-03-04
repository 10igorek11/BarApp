package com.example.barapp.presentation.screens.learning.material_edit_screen

import android.content.Context
import com.example.barapp.domain.models.ContentDto

sealed class LearningMaterialEditScreenEvent{
    data class OnLoad(val id:Int, val context: Context): LearningMaterialEditScreenEvent()
    data class OnTextChange(val text:String): LearningMaterialEditScreenEvent()
    data class OnPreviewChange(val preview:String): LearningMaterialEditScreenEvent()
    object OnPreviewDelete: LearningMaterialEditScreenEvent()
    data class OnContentAdd(val content: ContentDto): LearningMaterialEditScreenEvent()
    data class OnContentDelete(val content: ContentDto): LearningMaterialEditScreenEvent()
    data class OnConfirm(val context: Context): LearningMaterialEditScreenEvent()
    data class OnTitleChange(val title: String) : LearningMaterialEditScreenEvent()
    object OnDelete: LearningMaterialEditScreenEvent()
}
