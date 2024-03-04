package com.example.barapp.presentation.screens.tests.test_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Snackbar
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.barapp.R
import com.example.barapp.presentation.screens.ImageViewer
import com.example.barapp.presentation.screens.home.TopBar
import com.example.barapp.presentation.screens.tests.LoadingEvent
import com.example.barapp.presentation.theme.CorrectAnswerColor
import com.example.barapp.presentation.theme.GrayColor20
import com.example.barapp.presentation.theme.GrayColor40
import com.example.barapp.presentation.theme.OnCorrectAnswerColor
import com.example.barapp.presentation.theme.OnWrongAnswerColor
import com.example.barapp.presentation.theme.WrongAnswerColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TestScreen(navController: NavController, testId: Int) {
    val viewModel = hiltViewModel<TestScreenViewModel>()
    val state = viewModel.state.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var isLoaded by remember {
        mutableStateOf(false)
    }
    var answersLoaded by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(context) {
        viewModel.onEvent(TestScreenEvent.OnLoad(testId))
        viewModel.loadingEvents.collect {
            when (it) {
                LoadingEvent.SUCCESS -> {
                    isLoaded = true
                }

                LoadingEvent.DEFAULT -> {
                    isLoaded = false
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        if (isLoaded) {
            val actualAnswersList = remember {
                mutableStateListOf<List<Boolean>>()
            }
            val userAnswersList = remember {
                mutableStateListOf<List<Boolean>>()
            }
            var showImage by remember {
                mutableStateOf<ImageRequest?>(null)
            }
            LaunchedEffect(Unit) {
                for (question in state.value.test.questions.indices) {
                    actualAnswersList.add(state.value.test.questions[question].answers.map { it.isCorrect })
                }
                for (question in actualAnswersList.indices) {
                    userAnswersList.add(actualAnswersList[question].map { false })
                }
                answersLoaded = true
            }
            if (answersLoaded) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
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
                                text = state.value.test.name,
                                style = MaterialTheme.typography.h5.copy(
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            )
                        },
                        menu = {

                        },
                        backgroundColor = MaterialTheme.colors.surface,
                        elevation = 0.dp
                    )
                    val tabPagerState = com.google.accompanist.pager.rememberPagerState(0)

                    if (isLoaded) {
                        if (!state.value.confirmed) {
                            Box {
                                com.google.accompanist.pager.HorizontalPager(
                                    modifier = Modifier
                                        .padding(top = 60.dp)
                                        .align(Alignment.TopCenter),
                                    state = tabPagerState,
                                    count = state.value.test.questions.size
                                ) { page ->
                                    Box(
                                        Modifier.fillMaxSize()
                                    ) {
                                        Card(
                                            modifier = Modifier
                                                .padding(13.dp)
                                                .align(Alignment.TopCenter)
                                                .fillMaxWidth(),
                                            border = BorderStroke(1.dp, GrayColor40),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Column(
                                            ) {
                                                Text(
                                                    modifier = Modifier.padding(10.dp),
                                                    text = state.value.test.questions[page].text,
                                                    style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.onBackground)
                                                )
                                                state.value.test.questions[page].photo?.let {
                                                    val model =
                                                        ImageRequest.Builder(LocalContext.current)
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
                                                            .padding(horizontal = 10.dp)
                                                            .fillMaxWidth()
                                                            .sizeIn(maxHeight = 200.dp)
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
                                                LazyColumn(
                                                ) {
                                                    items(state.value.test.questions[page].answers.size) { id ->
                                                        Row(
                                                            modifier = Modifier
                                                                .padding(5.dp)
                                                                .fillMaxWidth()
                                                                .clickable {
                                                                    val answers =
                                                                        userAnswersList[page].mapIndexed { index, value ->
                                                                            if (index == id) !value else value
                                                                        }
                                                                    userAnswersList[page] = answers

                                                                },
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Checkbox(
                                                                checked = userAnswersList[page][id],
                                                                onCheckedChange = {
                                                                    userAnswersList[page] =
                                                                        userAnswersList[page].mapIndexed { index, value ->
                                                                            if (index == id) it else value
                                                                        }
                                                                })
                                                            Spacer(Modifier.width(5.dp))
                                                            Text(
                                                                text = state.value.test.questions[page].answers[id].text
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                ScrollableTabRow(
                                    contentColor = MaterialTheme.colors.secondary,
                                    indicator = { tabPositions ->
                                        TabRowDefaults.Indicator(
                                            modifier = Modifier.pagerTabIndicatorOffset(
                                                tabPagerState,
                                                tabPositions
                                            ),
                                            color = MaterialTheme.colors.secondary
                                        )
                                    },
                                    modifier = Modifier.height(60.dp),
                                    selectedTabIndex = tabPagerState.currentPage,
                                    backgroundColor = MaterialTheme.colors.surface
                                ) {
                                    state.value.test.questions.forEachIndexed { index, _ ->
                                        Tab(
                                            text = {
                                                Text(
                                                    text = index.plus(1).toString(),
                                                    color = if (tabPagerState.currentPage == index) MaterialTheme.colors.secondary else GrayColor40
                                                )
                                            },
                                            selected = tabPagerState.currentPage == index,
                                            onClick = {
                                                scope.launch {
                                                    tabPagerState.animateScrollToPage(index)
                                                }
                                            },
                                            selectedContentColor = MaterialTheme.colors.secondary,
                                            unselectedContentColor = GrayColor40
                                        )
                                    }
                                }
                            }
                        } else {
                            Box {
                                com.google.accompanist.pager.HorizontalPager(
                                    modifier = Modifier
                                        .padding(top = 60.dp)
                                        .align(Alignment.TopCenter),
                                    state = tabPagerState,
                                    count = state.value.test.questions.size
                                ) { page ->
                                    Box(
                                        Modifier.fillMaxSize()
                                    ) {
                                        Card(
                                            modifier = Modifier
                                                .padding(13.dp)
                                                .align(Alignment.TopCenter)
                                                .fillMaxWidth(),
                                            border = BorderStroke(1.dp, GrayColor40),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Column(
                                            ) {
                                                Text(
                                                    modifier = Modifier.padding(10.dp),
                                                    text = state.value.test.questions[page].text,
                                                    style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.onBackground)
                                                )
                                                state.value.test.questions[page].photo?.let {
                                                    val model =
                                                        ImageRequest.Builder(LocalContext.current)
                                                            .data(it)
                                                            .crossfade(true)
                                                            .build()
                                                    AsyncImage(
                                                        model = model,
                                                        placeholder = painterResource(R.drawable.placeholder),
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Fit,
                                                        modifier = Modifier
                                                            .padding(horizontal = 10.dp)
                                                            .fillMaxWidth()
                                                            .sizeIn(maxHeight = 200.dp)
                                                            .clip(RoundedCornerShape(10.dp))
                                                    )
                                                }
                                                LazyColumn(
                                                ) {
                                                    items(state.value.test.questions[page].answers.size) { id ->
                                                        val modifier = Modifier
                                                            .padding(5.dp)
                                                            .fillMaxWidth()
                                                        Row(
                                                            modifier = if (actualAnswersList[page][id])
                                                                modifier.background(
                                                                    CorrectAnswerColor
                                                                )
                                                            else if (userAnswersList[page][id]) modifier
                                                                .background(WrongAnswerColor)
                                                            else modifier,
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Checkbox(
                                                                checked = userAnswersList[page][id],
                                                                onCheckedChange = {

                                                                },
                                                                colors = CheckboxDefaults.colors(
                                                                    checkedColor = if (userAnswersList[page][id] && actualAnswersList[page][id]) OnCorrectAnswerColor else OnWrongAnswerColor,
                                                                )
                                                            )
                                                            Spacer(Modifier.width(5.dp))
                                                            Text(
                                                                text = state.value.test.questions[page].answers[id].text
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                ScrollableTabRow(
                                    indicator = { tabPositions ->
                                        TabRowDefaults.Indicator(
                                            modifier = Modifier.pagerTabIndicatorOffset(
                                                tabPagerState,
                                                tabPositions
                                            ),
                                            color = MaterialTheme.colors.secondary
                                        )
                                    },
                                    modifier = Modifier.height(60.dp),
                                    selectedTabIndex = tabPagerState.currentPage,
                                    backgroundColor = MaterialTheme.colors.surface
                                ) {
                                    state.value.test.questions.forEachIndexed { index, _ ->
                                        Tab(
                                            text = {
                                                Text(
                                                    text = index.plus(1).toString(),
                                                    color = if (userAnswersList[index].areListsEqual(
                                                            actualAnswersList[index]
                                                        )
                                                    ) CorrectAnswerColor else WrongAnswerColor,
                                                )
                                            },
                                            selected = tabPagerState.currentPage == index,
                                            onClick = {
                                                scope.launch {
                                                    tabPagerState.animateScrollToPage(index)
                                                }
                                            },
                                            selectedContentColor = if (userAnswersList[index].areListsEqual(
                                                    actualAnswersList[index]
                                                )
                                            ) CorrectAnswerColor else WrongAnswerColor,
                                            unselectedContentColor = if (userAnswersList[index].areListsEqual(
                                                    actualAnswersList[index]
                                                )
                                            ) CorrectAnswerColor else WrongAnswerColor
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                if (state.value.passedQuestions >= 0) {
                    Snackbar(
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(15.dp)
                            .wrapContentWidth()
                            .border(1.dp, GrayColor40, RoundedCornerShape(10.dp)),
                        backgroundColor = MaterialTheme.colors.surface,
                        action = {
                            TextButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Text(
                                    text = stringResource(id = R.string.exit)
                                )
                            }
                        }
                    ) {
                        val isPassed =
                            (state.value.passedQuestions == state.value.test.questions.size)
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .wrapContentWidth(),
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = if (isPassed) MaterialTheme.colors.primary else MaterialTheme.colors.secondary)) {
                                    append(
                                        if (isPassed) stringResource(R.string.excellent) else stringResource(
                                            R.string.failed
                                        )
                                    )
                                }
                                append(" ")
                                append(
                                    stringResource(
                                        id = R.string.passed,
                                        state.value.passedQuestions,
                                        state.value.test.questions.size
                                    )
                                )
                            }
                        )
                    }
                } else {
                    FloatingActionButton(
                        modifier = Modifier
                            .padding(15.dp)
                            .size(60.dp)
                            .align(Alignment.BottomEnd)
                            .border(1.dp, MaterialTheme.colors.background, CircleShape),
                        backgroundColor = MaterialTheme.colors.secondary,
                        elevation = FloatingActionButtonDefaults.elevation(8.dp),
                        onClick = {
                            if (state.value.confirmed) {
                                navController.popBackStack()
                            } else {
                                val passingQuestionsCount = userAnswersList.count {
                                    val index = userAnswersList.indexOf(it)
                                    it.areListsEqual(actualAnswersList[index])
                                }
                                viewModel.onEvent(TestScreenEvent.OnConfirm(passingQuestionsCount))
                            }
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
    }
}

private fun <E> List<E>.areListsEqual(list2: List<E>): Boolean {
    return this.size == list2.size && this.zip(list2).all { it.first == it.second }
}
