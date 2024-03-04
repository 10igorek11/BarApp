package com.example.barapp.presentation.screens.learning.material_screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.barapp.R
import com.example.barapp.common.presentation.LinkifyText
import com.example.barapp.presentation.screens.ImageViewer
import com.example.barapp.presentation.screens.home.TopBar
import com.example.barapp.presentation.screens.learning.ScreenEvent
import com.example.barapp.presentation.theme.GrayColor20
import com.example.barapp.presentation.theme.GrayColor40
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun LearningMaterialScreen(navController: NavController, materialId: Int) {
    val viewModel = hiltViewModel<LearningMaterialScreenViewModel>()
    val state = viewModel.state.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var isLoaded by remember {
        mutableStateOf(false)
    }
    if (!isLoaded) {
        viewModel.onEvent(LearningMaterialScreenEvent.OnLoad(materialId))
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
                    Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()){
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
                            navController.popBackStack()
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

                    },
                    backgroundColor = MaterialTheme.colors.surface
                )
            },

            ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                if (isLoaded) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        item{
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 13.dp, vertical = 10.dp)
                                    .fillMaxWidth(),
                                border = BorderStroke(1.dp, GrayColor40),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp)
                                ) {
                                    state.value.material.preview?.let {
                                        val model = ImageRequest.Builder(LocalContext.current)
                                            .data(it)
                                            .crossfade(true)
                                            .build()
                                        AsyncImage(
                                            model = model,
                                            placeholder = painterResource(R.drawable.placeholder),
                                            contentDescription = null,
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier
                                                .clickable {
                                                    showImage = model
                                                }
                                                .fillMaxWidth()
                                                .sizeIn(maxHeight = 200.dp)
                                                .background(
                                                    color = GrayColor20,
                                                    shape = RoundedCornerShape(10.dp)
                                                )
                                                .border(1.dp, GrayColor40, RoundedCornerShape(10.dp))
                                                .clip(RoundedCornerShape(10.dp))
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                    state.value.material.text?.let{
                                        LinkifyText(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = it,
                                            style = MaterialTheme.typography.body2
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                    if(state.value.material.contents.isNotEmpty()){
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = stringResource(id = R.string.album),
                                            style = MaterialTheme.typography.h6.copy(
                                                textAlign = TextAlign.Center,
                                                fontSize = 16.sp
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        val pages = state.value.material.contents.size
                                        val pagerState = androidx.compose.foundation.pager.rememberPagerState(
                                            pageCount = { pages }
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()){
                                            androidx.compose.foundation.pager.HorizontalPager(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(200.dp),
                                                state = pagerState
                                            ) {page->
                                                val contentModel = ImageRequest.Builder(LocalContext.current)
                                                    .data(state.value.material.contents[page].photo)
                                                    .crossfade(true)
                                                    .build()
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
                                            }
                                            Row(
                                                Modifier
                                                    .wrapContentWidth()
                                                    .padding(bottom = 2.dp)
                                                    .background(MaterialTheme.colors.surface, RoundedCornerShape(5.dp))
                                                    .padding(vertical = 2.dp, horizontal = 6.dp)
                                                    .align(Alignment.BottomCenter),
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                repeat(pagerState.pageCount) { iteration ->
                                                    val color = if (pagerState.currentPage == iteration) MaterialTheme.colors.primary else Color.LightGray
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
                            }
                        }
                    }
                }
            }
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
