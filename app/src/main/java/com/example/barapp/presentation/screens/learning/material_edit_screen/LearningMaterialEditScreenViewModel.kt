package com.example.barapp.presentation.screens.learning.material_edit_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barapp.R
import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.ContentDto
import com.example.barapp.domain.models.LearningMaterialDto
import com.example.barapp.domain.usecases.SetPhotoUseCase
import com.example.barapp.domain.usecases.auth.GetLoggedInUserUseCase
import com.example.barapp.domain.usecases.learningmaterial.CreateLearningMaterialUseCase
import com.example.barapp.domain.usecases.learningmaterial.DeleteLearningMaterialUseCase
import com.example.barapp.domain.usecases.learningmaterial.GetLearningMaterialByIdUseCase
import com.example.barapp.domain.usecases.learningmaterial.UpdateLearningMaterialUseCase
import com.example.barapp.presentation.screens.learning.ScreenEvent
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearningMaterialEditScreenViewModel @Inject constructor(
    private val getLearningMaterialByIdUseCase: GetLearningMaterialByIdUseCase,
    private val deleteLearningMaterialUseCase: DeleteLearningMaterialUseCase,
    private val createLearningMaterialUseCase: CreateLearningMaterialUseCase,
    private val updateLearningMaterialUseCase: UpdateLearningMaterialUseCase,
    private val setPhotoUseCase: SetPhotoUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LearningMaterialEditScreenState())
    var state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        LearningMaterialEditScreenState()
    )
    private val screenEventChannel = Channel<ScreenEvent>()
    val screenEvents = screenEventChannel.receiveAsFlow()
    fun onEvent(event: LearningMaterialEditScreenEvent) {
        when (event) {
            is LearningMaterialEditScreenEvent.OnLoad -> {
                viewModelScope.launch {
                    if (event.id > 0) {
                        when (val material: Resource<LearningMaterialDto> =
                            getLearningMaterialByIdUseCase(event.id)) {
                            is Resource.Error -> {
                                _state.update {
                                    it.copy(
                                        isLoading = false
                                    )
                                }
                                screenEventChannel.send(
                                    ScreenEvent.DEFAULT
                                )
                            }

                            is Resource.Loading -> return@launch
                            is Resource.Success -> {
                                _state.update {
                                    it.copy(
                                        material = material.data!!,
                                        contents = material.data.contents,
                                        isLoading = false
                                    )
                                }
                                screenEventChannel.send(
                                    ScreenEvent.SUCCESS
                                )
                            }
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                contents = emptyList(),
                                material = LearningMaterialDto(
                                    title = event.context.getString(R.string.material_num_one)
                                )
                            )
                        }
                        screenEventChannel.send(
                            ScreenEvent.SUCCESS
                        )
                    }
                }
            }

            is LearningMaterialEditScreenEvent.OnConfirm -> {
                viewModelScope.launch {
                    if (_state.value.material.text?.trim()
                            .isNullOrBlank() && _state.value.contents.isEmpty()
                    ) {
                        _state.update {
                            it.copy(
                                materialError = event.context.getString(R.string.empty_text_or_nocontent_error)
                            )
                        }
                        return@launch
                    }
                    if(_state.value.material.id==0) {
                        val result = createLearningMaterialUseCase(_state.value.material.copy(
                            contents = _state.value.contents.map {
                                val photo = it.photo
                                when(val result = setPhotoUseCase(photo)){
                                    is Resource.Error -> {
                                        screenEventChannel.send(ScreenEvent.ERROR)
                                        return@launch
                                    }
                                    is Resource.Loading -> {
                                        screenEventChannel.send(ScreenEvent.ERROR)
                                        return@launch
                                    }
                                    is Resource.Success -> {
                                        it.copy(
                                            photo = result.data!!.data.url
                                        )
                                    }
                                }

                            },
                            text = if(_state.value.material.text?.trim().isNullOrBlank()) null else _state.value.material.text?.trim()
                        ))
                        when(result){
                            is Resource.Error -> {
                                return@launch
                            }
                            is Resource.Loading -> {
                                return@launch
                            }
                            is Resource.Success -> {

                            }
                        }
                    }
                    else{
                        val mergedContents = mutableListOf<ContentDto>()
                        mergedContents.addAll(_state.value.material.contents.filter{
                            !_state.value.contents.contains(it)
                        }.map{
                            it.copy(
                                isDeleted = true
                            )
                        })
                        mergedContents.addAll(_state.value.contents.map {
                            val photo = it.photo
                            if(photo.take(7)=="https:/"){
                                it
                            }
                            else {
                                when(val result = setPhotoUseCase(photo)){
                                    is Resource.Error -> {

                                        screenEventChannel.send(ScreenEvent.ERROR)
                                        return@launch
                                    }
                                    is Resource.Loading -> {
                                        screenEventChannel.send(ScreenEvent.ERROR)
                                        return@launch
                                    }
                                    is Resource.Success -> {
                                        it.copy(
                                            photo = result.data!!.data.url
                                        )
                                    }
                                }
                            }
                        })
                        val result = updateLearningMaterialUseCase(_state.value.material.copy(
                            preview = if(_state.value.material.preview?.take(7)=="https:/" || _state.value.material.preview==null )
                                _state.value.material.preview
                            else {
                                when(val result = setPhotoUseCase(_state.value.material.preview!!)){
                                    is Resource.Error -> {

                                        screenEventChannel.send(ScreenEvent.ERROR)
                                        return@launch
                                    }
                                    is Resource.Loading -> {
                                        screenEventChannel.send(ScreenEvent.ERROR)
                                        return@launch
                                    }
                                    is Resource.Success -> {
                                        result.data!!.data.url
                                    }
                                }
                                 },
                            contents = mergedContents,
                            text = if(_state.value.material.text?.trim().isNullOrBlank()) null else _state.value.material.text?.trim()
                        ))
                        when(result){
                            is Resource.Error -> {
                                return@launch
                            }
                            is Resource.Loading -> {
                                return@launch
                            }
                            is Resource.Success -> {
                            }
                        }
                    }
                    screenEventChannel.send(
                        ScreenEvent.DEFAULT
                    )
                }
            }

            is LearningMaterialEditScreenEvent.OnContentAdd -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            contents = it.contents.plus(event.content),
                            materialError = null,
                            isModified = true
                        )
                    }
                }
            }

            is LearningMaterialEditScreenEvent.OnContentDelete -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            contents = it.contents.minus(event.content),
                            isModified = true
                        )
                    }
                }
            }

            is LearningMaterialEditScreenEvent.OnPreviewChange -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            material = it.material.copy(
                                preview = event.preview
                            ),
                            isModified = true
                        )
                    }
                }
            }

            LearningMaterialEditScreenEvent.OnPreviewDelete -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            material = it.material.copy(
                                preview = null
                            ),
                            isModified = true
                        )
                    }
                }
            }

            is LearningMaterialEditScreenEvent.OnTextChange -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            material = it.material.copy(
                                text = event.text
                            ),
                            materialError = null,
                            isModified = true
                        )
                    }
                }
            }

            is LearningMaterialEditScreenEvent.OnTitleChange -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            material = it.material.copy(
                                title = event.title
                            ),
                            isModified = true
                        )
                    }
                }
            }

            LearningMaterialEditScreenEvent.OnDelete -> {
                viewModelScope.launch {
                    if (_state.value.material.id > 0) {
                        deleteLearningMaterialUseCase(_state.value.material.id)
                    }
                    screenEventChannel.send(ScreenEvent.DEFAULT)
                }
            }
        }
    }

}