package com.example.barapp.presentation.screens.learning.material_edit_screen

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.barapp.R
import com.example.barapp.common.presentation.ImageUtil
import com.example.barapp.domain.models.ContentDto
import com.example.barapp.presentation.screens.ImageViewer
import com.example.barapp.presentation.screens.dialogs.CreateRenameDialog
import com.example.barapp.presentation.screens.dialogs.SaveQuitDialog
import com.example.barapp.presentation.screens.home.TopBar
import com.example.barapp.presentation.screens.learning.ScreenEvent
import com.example.barapp.presentation.theme.GrayColor20
import com.example.barapp.presentation.theme.GrayColor40
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LearningMaterialEditScreen(navController: NavController, materialId: Int) {
    val viewModel = hiltViewModel<LearningMaterialEditScreenViewModel>()
    val state = viewModel.state.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var isLoaded by remember {
        mutableStateOf(false)
    }
    if (!isLoaded) {
        viewModel.onEvent(LearningMaterialEditScreenEvent.OnLoad(materialId, context))
    }
    LaunchedEffect(context) {
        viewModel.screenEvents.collect {
            when (it) {
                ScreenEvent.SUCCESS -> {
                    isLoaded = true
                }

                ScreenEvent.DEFAULT -> {
                    navController.popBackStack()
                }

                ScreenEvent.ERROR -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.server_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        var renameDialogShowed by remember {
            mutableStateOf(false)
        }
        var saveQuitDialogShowed by remember {
            mutableStateOf(false)
        }
        var showImage by remember {
            mutableStateOf<ImageRequest?>(null)
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            topBar = {
                TopBar(
                    navigationIcon = {
                        IconButton(onClick = {
                            if (state.value.isModified) {
                                saveQuitDialogShowed = true
                            } else {
                                navController.popBackStack()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = state.value.material.title,
                            style = MaterialTheme.typography.h5.copy(
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    menu = {
                        Box {
                            var menuExpanded by remember {
                                mutableStateOf(false)
                            }
                            IconButton(
                                modifier = Modifier.align(Alignment.CenterEnd),
                                onClick = { menuExpanded = true }) {
                                Icon(
                                    imageVector = Icons.Rounded.MoreHoriz,
                                    contentDescription = null
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .wrapContentWidth()
                            ) {
                                me.saket.cascade.CascadeDropdownMenu(
                                    modifier = Modifier
                                        .background(Color.Transparent)
                                        .border(1.dp, GrayColor40, RoundedCornerShape(5.dp))
                                        .clip(RoundedCornerShape(5.dp)),
                                    shadowElevation = 10.dp,
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = false }) {
                                    androidx.compose.material.DropdownMenuItem(
                                        onClick = {
                                            renameDialogShowed = true
                                            menuExpanded = false
                                        },
                                        modifier = Modifier
                                            .background(MaterialTheme.colors.surface)
                                            .align(Alignment.End)
                                    ) {
                                        Row {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = null
                                            )
                                            Text(
                                                text = stringResource(id = R.string.rename),
                                                modifier = Modifier.padding(horizontal = 10.dp),
                                                style = MaterialTheme.typography.caption.copy(
                                                    color = MaterialTheme.colors.onBackground
                                                )
                                            )
                                        }
                                    }
                                    androidx.compose.material.DropdownMenuItem(
                                        onClick = {
                                            viewModel.onEvent(
                                                LearningMaterialEditScreenEvent.OnConfirm(
                                                    context
                                                )
                                            )
                                            navController.popBackStack()
                                            menuExpanded = false
                                        },
                                        modifier = Modifier
                                            .background(MaterialTheme.colors.surface)
                                            .align(Alignment.End)
                                    ) {
                                        Row {
                                            Row {
                                                Icon(
                                                    imageVector = Icons.Default.Save,
                                                    contentDescription = null,
                                                )
                                                Text(
                                                    text = stringResource(id = R.string.save),
                                                    modifier = Modifier.padding(horizontal = 10.dp),
                                                    style = MaterialTheme.typography.caption.copy(
                                                        color = MaterialTheme.colors.onBackground
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    androidx.compose.material.DropdownMenuItem(
                                        onClick = {
                                            viewModel.onEvent(LearningMaterialEditScreenEvent.OnDelete)
                                            menuExpanded = false
                                        },
                                        modifier = Modifier
                                            .background(MaterialTheme.colors.surface)
                                            .align(Alignment.End)
                                    ) {
                                        Row {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = null,
                                                tint = MaterialTheme.colors.secondary
                                            )
                                            Text(
                                                text = stringResource(id = R.string.delete),
                                                modifier = Modifier.padding(horizontal = 10.dp),
                                                style = MaterialTheme.typography.caption.copy(
                                                    color = MaterialTheme.colors.secondary
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    backgroundColor = MaterialTheme.colors.surface
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(15.dp)
                        .size(60.dp)
                        .align(Alignment.BottomEnd)
                        .border(1.dp, MaterialTheme.colors.background, CircleShape),
                    backgroundColor = MaterialTheme.colors.secondary,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                    onClick = {
                        viewModel.onEvent(LearningMaterialEditScreenEvent.OnConfirm(context))
                    }) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colors.background
                    )
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                if (isLoaded) {
                    LazyColumn(
                        modifier = Modifier
                            .imePadding()
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                val imageData = remember { mutableStateOf<Uri?>(null) }
                                val previewImagePickLauncher =
                                    rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { photoUri ->
                                        imageData.value = photoUri
                                        photoUri?.let {
                                            ImageUtil.convert(context, photoUri)
                                                ?.let { convertedPhoto ->
                                                    viewModel.onEvent(
                                                        LearningMaterialEditScreenEvent.OnPreviewChange(
                                                            convertedPhoto
                                                        )
                                                    )
                                                }
                                        }
                                    }
                                val imgPreviewModifier = Modifier
                                    .padding(13.dp)
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clickable {
                                        previewImagePickLauncher.launch(
                                            "image/*"
                                        )
                                    }
                                if (state.value.material.preview == null && imageData.value == null) {
                                    Image(
                                        painter = painterResource(R.drawable.add_image),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = imgPreviewModifier
                                            .background(
                                                color = GrayColor20,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .border(1.dp, GrayColor40, RoundedCornerShape(10.dp))
                                            .clip(RoundedCornerShape(10.dp))
                                    )
                                } else {
                                    val model = if (imageData.value != null) ImageRequest.Builder(
                                        LocalContext.current
                                    )
                                        .data(imageData.value)
                                        .crossfade(true)
                                        .build()
                                    else ImageRequest.Builder(LocalContext.current)
                                        .data(state.value.material.preview)
                                        .crossfade(true)
                                        .build()
                                    AsyncImage(
                                        model = model,
                                        placeholder = painterResource(R.drawable.placeholder),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = imgPreviewModifier
                                            .background(
                                                color = GrayColor20,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .border(1.dp, GrayColor40, RoundedCornerShape(10.dp))
                                            .clip(RoundedCornerShape(10.dp))
                                    )
                                    IconButton(
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .size(50.dp)
                                            .align(Alignment.BottomEnd),
                                        onClick = {
                                            viewModel.onEvent(
                                                LearningMaterialEditScreenEvent.OnPreviewDelete
                                            )
                                        }
                                    ) {
                                        Icon(
                                            modifier = Modifier.fillMaxSize(),
                                            imageVector = Icons.Rounded.Delete,
                                            contentDescription = null,
                                            tint = MaterialTheme.colors.secondary
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 13.dp)
                                    .height(IntrinsicSize.Min),
                                label = {
                                    Text(text = stringResource(id = R.string.content))
                                },
                                shape = RoundedCornerShape(10.dp),
                                value = state.value.material.text ?: "",
                                isError = state.value.materialError != null,
                                onValueChange = { newValue ->
                                    viewModel.onEvent(
                                        LearningMaterialEditScreenEvent.OnTextChange(
                                            newValue
                                        )
                                    )
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = MaterialTheme.colors.surface
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done,
                                    capitalization = KeyboardCapitalization.Sentences,
                                )
                            )
                        }
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(13.dp)
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(id = R.string.album),
                                    style = MaterialTheme.typography.h6.copy(
                                        textAlign = TextAlign.Center,
                                        fontSize = 16.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        val pagerState = androidx.compose.foundation.pager.rememberPagerState(
                                            pageCount = {
                                                state.value.contents.size + 1
                                            }
                                        )
                                        androidx.compose.foundation.pager.HorizontalPager(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(200.dp),
                                            state = pagerState
                                        ) { page ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                val contentImagePickLauncher =
                                                    rememberLauncherForActivityResult(
                                                        ActivityResultContracts.GetContent()
                                                    ) { photoUri ->
                                                        photoUri?.let {
                                                            ImageUtil.convert(context, photoUri)
                                                                ?.let { convertedPhoto ->
                                                                    viewModel.onEvent(
                                                                        LearningMaterialEditScreenEvent.OnContentAdd(
                                                                            ContentDto(
                                                                                id = minOf(0, state.value.contents.minOf { it.id }) -1,
                                                                                materialId = state.value.material.id,
                                                                                photo = convertedPhoto
                                                                            )
                                                                        )
                                                                    )
                                                                }
                                                        }
                                                    }
                                                val imgModifier = Modifier
                                                    .height(200.dp)
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        contentImagePickLauncher.launch(
                                                            "image/*"
                                                        )
                                                    }
                                                if (page == state.value.contents.size) {
                                                    Image(
                                                        painter = painterResource(R.drawable.add_image),
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Fit,
                                                        modifier = imgModifier
                                                            .background(
                                                                color = GrayColor20,
                                                                shape = RoundedCornerShape(10.dp)
                                                            )
                                                            .border(
                                                                1.dp,
                                                                GrayColor40,
                                                                RoundedCornerShape(10.dp)
                                                            )
                                                            .clip(RoundedCornerShape(10.dp))
                                                    )
                                                } else {
                                                    val isUrl = state.value.contents[page].photo.take(7)=="https:/"
                                                    val contentModel = if(isUrl) {
                                                        ImageRequest.Builder(
                                                            LocalContext.current
                                                        )
                                                            .data(state.value.contents[page].photo)
                                                            .crossfade(true)
                                                            .build()
                                                    }
                                                    else{
                                                        val imageBytes = Base64.decode(state.value.contents[page].photo, Base64.DEFAULT)
                                                        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                                        ImageRequest.Builder(
                                                            LocalContext.current
                                                        )
                                                            .data(decodedImage)
                                                            .crossfade(true)
                                                            .build()
                                                    }
                                                    AsyncImage(
                                                        model = contentModel,
                                                        placeholder = painterResource(R.drawable.placeholder),
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Fit,
                                                        modifier = Modifier
                                                            .clickable {
                                                                showImage = contentModel
                                                            }
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 5.dp)
                                                            .height(200.dp)
                                                            .background(
                                                                color = GrayColor20,
                                                                shape = RoundedCornerShape(10.dp)
                                                            )
                                                            .border(
                                                                1.dp,
                                                                GrayColor40,
                                                                RoundedCornerShape(10.dp)
                                                            )
                                                            .clip(RoundedCornerShape(10.dp))
                                                    )
                                                    IconButton(
                                                        modifier = Modifier
                                                            .padding(10.dp)
                                                            .size(50.dp)
                                                            .align(Alignment.CenterStart),
                                                        onClick = {
                                                            viewModel.onEvent(
                                                                LearningMaterialEditScreenEvent.OnContentDelete(
                                                                    state.value.contents[page]
                                                                )
                                                            )
                                                        }
                                                    ) {
                                                        Icon(
                                                            modifier = Modifier.fillMaxSize(),
                                                            imageVector = Icons.Rounded.Delete,
                                                            contentDescription = null,
                                                            tint = MaterialTheme.colors.secondary
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        Row(
                                            Modifier
                                                .wrapContentWidth()
                                                .padding(bottom = 2.dp)
                                                .background(
                                                    MaterialTheme.colors.surface,
                                                    RoundedCornerShape(5.dp)
                                                )
                                                .padding(vertical = 2.dp, horizontal = 6.dp)
                                                .align(Alignment.BottomCenter),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            repeat(pagerState.pageCount) { iteration ->
                                                val color =
                                                    if (pagerState.currentPage == iteration) MaterialTheme.colors.primary else Color.LightGray
                                                Box(
                                                    modifier = Modifier
                                                        .padding(2.dp)
                                                        .clip(CircleShape)
                                                        .background(color)
                                                        .size(10.dp)
                                                )
                                            }
                                        }
                                    }

                            }
                        }

                        item {
                            state.value.materialError?.let { errorText ->
                                Text(
                                    text = errorText,
                                    color = MaterialTheme.colors.error,
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                        .fillMaxWidth()
                                        .height(16.dp),
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            } ?: Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }

        if (renameDialogShowed) {
            CreateRenameDialog(
                name = state.value.material.title,
                onDismiss = {
                    renameDialogShowed = false
                },
                onConfirm = {
                    viewModel.onEvent(LearningMaterialEditScreenEvent.OnTitleChange(it))
                }
            )
        }
        if (saveQuitDialogShowed) {
            SaveQuitDialog(
                onDismiss = {
                    saveQuitDialogShowed = false
                    navController.popBackStack()
                },
                onConfirm = {
                    viewModel.onEvent(LearningMaterialEditScreenEvent.OnConfirm(context))
                }
            )
        }
        showImage?.let {
            ImageViewer(
                showImage = showImage,
                onDismiss = {
                    showImage = null
                }
            )
        }
    }
}


private fun <E> List<E>.areListsEqual(list2: List<E>): Boolean {
    return this.size == list2.size && this.zip(list2).all { it.first == it.second }
}
