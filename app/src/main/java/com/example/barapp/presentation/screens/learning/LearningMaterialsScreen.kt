package com.example.barapp.presentation.screens.learning

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.barapp.R
import com.example.barapp.common.ADMIN_ROLE
import com.example.barapp.common.EMPLOYEE_ROLE
import com.example.barapp.domain.models.LearningMaterial
import com.example.barapp.presentation.navigation.DetailsScreen
import com.example.barapp.presentation.screens.ImageViewer
import com.example.barapp.presentation.theme.GrayColor20
import com.example.barapp.presentation.theme.GrayColor40

@Composable
fun LearningMaterialsScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<LearningMaterialsScreenViewModel>()
    val state = viewModel.state.collectAsState()
    val loggedInUser = viewModel.getLoggedInUser()
    val context = LocalContext.current
    val isLoaded = remember {
        mutableStateOf(false)
    }
    if (loggedInUser != null) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            var showImage by remember {
                mutableStateOf<ImageRequest?>(null)
            }
            LaunchedEffect(key1 = context) {
                viewModel.screenEvents.collect {
                    when (it) {
                        ScreenEvent.DEFAULT -> {
                            isLoaded.value = false
                        }

                        ScreenEvent.ERROR -> {

                        }

                        ScreenEvent.SUCCESS -> {
                            isLoaded.value = true
                        }
                    }
                }
            }
            if (state.value.errorStatus) {
                Snackbar(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .border(1.dp, GrayColor40, RoundedCornerShape(10.dp)),
                    backgroundColor = MaterialTheme.colors.surface,
                    action = {
                        TextButton(onClick = {
                            viewModel.onEvent(LearningMaterialsScreenEvent.LoadData)
                        }) {
                            Text(
                                text = stringResource(id = R.string.retry),
                                style = MaterialTheme.typography.h5
                            )
                        }
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.server_error)
                    )
                }
            }
            LearningMaterialsScreenContent(
                isLoaded = isLoaded,
                state = state,
                onRefresh = {
                    viewModel.onEvent(LearningMaterialsScreenEvent.LoadData)
                },
                role = loggedInUser.roleId,
                onItemClick = { id ->
                    when (loggedInUser.roleId) {
                        ADMIN_ROLE -> {
                            viewModel.onEvent(LearningMaterialsScreenEvent.Refresh)
                            navController.navigate(
                                DetailsScreen.LearningMaterialEditScreen.route.replace(
                                    "{id}",
                                    id.toString()
                                )
                            )
                        }

                        EMPLOYEE_ROLE -> {
                            navController.navigate(
                                DetailsScreen.LearningMaterialScreen.route.replace(
                                    "{id}",
                                    id.toString()
                                )
                            )
                        }

                        else -> {}
                    }
                },
                onAddBtnClick = {
                    viewModel.onEvent(LearningMaterialsScreenEvent.Refresh)
                    navController.navigate(
                        DetailsScreen.LearningMaterialEditScreen.route.replace(
                            "{id}",
                            "0"
                        )
                    )
                },
                onImageClick = {
                    showImage = it
                }
            )

            showImage?.let {
                    ImageViewer(
                        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background.copy(alpha = 0.7f)),
                        showImage = showImage,
                        onDismiss = {
                            showImage = null
                        }
                    )

            }
        }


    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LearningMaterialsScreenContent(
    state: State<LearningMaterialsScreenState>,
    role: Int,
    onRefresh: () -> Unit,
    isLoaded: MutableState<Boolean>,
    onImageClick: (image: ImageRequest?) -> Unit,
    onItemClick: (id: Int) -> Unit,
    onAddBtnClick: () -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.value.isLoading,
        onRefresh = onRefresh
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .pullRefresh(pullRefreshState)
    ) {
        if (isLoaded.value) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                item {
                    Spacer(Modifier.height(0.dp))
                }
                items(state.value.materials) { material ->
                    LearningMaterialsItemPanel(
                        material = material,
                        onItemClick = onItemClick,
                        role = role,
                        onImageClick = onImageClick
                    )
                }
                item {
                    Spacer(Modifier.height(60.dp))
                }
            }
            if (state.value.materials.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    text = stringResource(id = R.string.no_elements),
                    style = MaterialTheme.typography.h6.copy(
                        color = GrayColor40,
                        textAlign = TextAlign.Center
                    )
                )
            }
        } else {
            onRefresh()
        }
        PullRefreshIndicator(
            refreshing = state.value.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
        if (role == ADMIN_ROLE) {
            FloatingActionButton(
                modifier = Modifier
                    .size(90.dp)
                    .align(Alignment.BottomEnd)
                    .padding(15.dp)
                    .border(1.dp, MaterialTheme.colors.background, CircleShape),
                backgroundColor = MaterialTheme.colors.secondary,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                onClick = {
                    onAddBtnClick()
                }) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colors.background
                )
            }
        }
    }
}

@Composable
private fun LearningMaterialsItemPanel(
    material: LearningMaterial,
    onItemClick: (id: Int) -> Unit,
    role: Int,
    onImageClick: (image:ImageRequest?) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 13.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, GrayColor40),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            material.preview?.let {
                val model = ImageRequest.Builder(LocalContext.current)
                    .data(it)
                    .crossfade(true)
                    .build()
                AsyncImage(
                    model = model,
                    placeholder = painterResource(R.drawable.placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth()
                        .sizeIn(maxHeight = 200.dp)
                        .background(
                            color = GrayColor20,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, GrayColor40, RoundedCornerShape(10.dp))
                        .clickable {
                            onImageClick(model)
                        }
                        .clip(RoundedCornerShape(10.dp))
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(65f, false)
                ) {
                    Text(
                        text = material.title,
                        style = MaterialTheme.typography.h6
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                Button(
                    modifier = Modifier
                        .weight(35f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        onItemClick(material.id)
                    }) {
                    val buttonText = when (role) {
                        ADMIN_ROLE -> {
                            stringResource(id = R.string.edit)
                        }

                        EMPLOYEE_ROLE -> {
                            stringResource(id = R.string.read)
                        }

                        else -> ""
                    }
                    Text(
                        text = buttonText
                    )
                }
            }

        }
    }
}
