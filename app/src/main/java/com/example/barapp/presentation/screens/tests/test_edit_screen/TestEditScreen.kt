package com.example.barapp.presentation.screens.tests.test_edit_screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.barapp.R
import com.example.barapp.common.presentation.ImageUtil
import com.example.barapp.presentation.screens.dialogs.CreateRenameDialog
import com.example.barapp.presentation.screens.dialogs.SaveQuitDialog
import com.example.barapp.presentation.screens.home.TopBar
import com.example.barapp.presentation.screens.tests.test_edit_screen.states.AnswerState
import com.example.barapp.presentation.screens.tests.test_edit_screen.states.QuestionState
import com.example.barapp.presentation.theme.GrayColor20
import com.example.barapp.presentation.theme.GrayColor40
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TestEditScreen(navController: NavController, testId: Int) {
    val viewModel = hiltViewModel<TestEditScreenViewModel>()
    val state = viewModel.state.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var isLoaded by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(context) {
        viewModel.screenEvents.collect {
            when (it) {
                ScreenEvent.LOADED -> {
                    isLoaded = true
                }

                ScreenEvent.SAVED -> {
                    navController.popBackStack()
                }

                ScreenEvent.DELETED -> {
                    navController.popBackStack()
                }
            }
        }
    }
    var renameDialogShowed by remember {
        mutableStateOf(false)
    }
    var saveQuitDialogShowed by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        val lazyListState = rememberLazyListState()
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBar(
                navigationIcon = {
                    IconButton(onClick = {
                        if(state.value.isModified){
                            saveQuitDialogShowed = true
                        }
                        else{
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
                        text = state.value.test.name,
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
                            Icon(imageVector = Icons.Rounded.MoreHoriz, contentDescription = null)
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
                                DropdownMenuItem(
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
                                DropdownMenuItem(
                                    onClick = {
                                        viewModel.onEvent(TestEditScreenEvent.OnConfirm(context))
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
                                DropdownMenuItem(
                                    onClick = {
                                        viewModel.onEvent(TestEditScreenEvent.OnDelete)
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
            if (isLoaded) {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .padding(horizontal = 13.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(13.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Spacer(Modifier)
                    }
                    items(state.value.questions.size) { index ->
                        if (!state.value.questions[index].isDeleted) {
                            QuestionContent(index, state, viewModel)
                        }
                    }
                    item {
                        state.value.test.testError?.let { errorText ->
                            Text(
                                text = errorText,
                                color = MaterialTheme.colors.error,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                fontSize = 14.sp
                            )
                        }
                    }
                    item {
                        FloatingActionButton(
                            modifier = Modifier
                                .padding(10.dp)
                                .size(60.dp)
                                .border(1.dp, GrayColor40, CircleShape),
                            backgroundColor = MaterialTheme.colors.surface,
                            elevation = FloatingActionButtonDefaults.elevation(8.dp),
                            onClick = {
                                if (state.value.questions.size < 100) {
                                    viewModel.onEvent(
                                        TestEditScreenEvent.AddQuestion(
                                            QuestionState(
                                                id = minOf(0, state.value.questions.minOf { it.id }) -1,
                                                testId = state.value.test.id,
                                                answers = listOf(
                                                    AnswerState(
                                                        text = context.getString(
                                                            R.string.answer,
                                                            1
                                                        ),
                                                        questionId = 0
                                                    )
                                                )
                                            )
                                        )
                                    )
                                }
                            }) {
                            Icon(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(5.dp),
                                imageVector = Icons.Rounded.Add,
                                contentDescription = null,
                                tint = GrayColor40
                            )
                        }
                    }
                    item {
                        Spacer(Modifier)
                    }
                }
            } else {
                viewModel.onEvent(TestEditScreenEvent.OnLoad(testId, context))
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(15.dp)
                .size(60.dp)
                .align(Alignment.BottomEnd)
                .border(1.dp, MaterialTheme.colors.background, CircleShape),
            backgroundColor = MaterialTheme.colors.secondary,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            onClick = {
                viewModel.onEvent(TestEditScreenEvent.OnConfirm(context))
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

    if (renameDialogShowed) {
        CreateRenameDialog(
            name = state.value.test.name,
            onDismiss = {
                renameDialogShowed = false
            },
            onConfirm = {
                viewModel.onEvent(TestEditScreenEvent.RenameTest(it))
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
                viewModel.onEvent(TestEditScreenEvent.OnConfirm(context))
            }
        )
    }
}

@Composable
fun QuestionContent(
    index: Int,
    state: State<TestEditScreenState>,
    viewModel: TestEditScreenViewModel
) {
    val isErrorQuestion = (state.value.questions[index].textError != null
            || state.value.questions[index].answerError != null
            || state.value.questions[index].answers.any { it.textError != null })
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        border = if (isErrorQuestion) BorderStroke(
            2.dp,
            MaterialTheme.colors.error
        ) else BorderStroke(1.dp, MaterialTheme.colors.onSurface),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        val context = LocalContext.current
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = stringResource(R.string.question_num, (index + 1)),
                style = MaterialTheme.typography.h6.copy(color = GrayColor40)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(IntrinsicSize.Min),
                label = {
                    Text(text = stringResource(id = R.string.question_lbl))
                },
                shape = RoundedCornerShape(10.dp),
                value = state.value.questions[index].text,
                isError = state.value.questions[index].textError != null,
                onValueChange = { newValue ->
                    if (newValue.length <= 100) {
                        viewModel.onEvent(
                            TestEditScreenEvent.UpdateQuestion(
                                state.value.questions[index].copy(
                                    text = newValue
                                )
                            )
                        )
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next
                )
            )
            state.value.questions[index].textError?.let { errorText ->
                Text(
                    text = errorText,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .align(Alignment.Start)
                        .height(16.dp),
                    fontSize = 12.sp
                )
            } ?: Spacer(modifier = Modifier.height(14.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                state.value.questions[index].answers.forEach { answer ->
                    val modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                    Row(
                        modifier = modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = answer.isCorrect,
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colors.primary,
                                uncheckedColor = MaterialTheme.colors.primary
                            ),
                            onCheckedChange = {
                                viewModel.onEvent(TestEditScreenEvent.UpdateQuestion(state.value.questions[index].copy(
                                    answers = state.value.questions[index].answers.map { qAnswer ->
                                        if (qAnswer.id == answer.id) qAnswer.copy(isCorrect = it) else qAnswer
                                    }
                                )))
                            })
                        Spacer(Modifier.width(5.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min),
                                shape = RoundedCornerShape(10.dp),
                                value = answer.text,
                                isError = answer.textError != null,
                                onValueChange = { newValue ->
                                    if (newValue.length <= 100) {
                                        viewModel.onEvent(TestEditScreenEvent.UpdateQuestion(state.value.questions[index].copy(
                                            answers = state.value.questions[index].answers.map { qAnswer ->
                                                if (qAnswer.id == answer.id) qAnswer.copy(
                                                    text = newValue
                                                ) else qAnswer
                                            }
                                        )))
                                    }
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = MaterialTheme.colors.surface
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    capitalization = KeyboardCapitalization.Sentences,
                                    imeAction = ImeAction.Next
                                ),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        if (state.value.questions[index].answers.size >1) {
                                            viewModel.onEvent(
                                                TestEditScreenEvent.UpdateQuestion(
                                                    state.value.questions[index].copy(
                                                        answers = state.value.questions[index].answers.minus(
                                                            answer
                                                        )
                                                    )
                                                )
                                            )
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Delete,
                                            contentDescription = null,
                                            tint = MaterialTheme.colors.secondary
                                        )
                                    }
                                }
                            )
                            answer.textError?.let { errorText ->
                                Text(
                                    text = errorText,
                                    color = MaterialTheme.colors.error,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                        .align(Alignment.Start)
                                        .height(16.dp),
                                    fontSize = 12.sp
                                )
                            } ?: Spacer(modifier = Modifier.height(14.dp))
                        }
                    }
                }
                state.value.questions[index].answerError?.let { errorText ->
                    Text(
                        text = errorText,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .align(Alignment.CenterHorizontally)
                            .height(16.dp),
                        fontSize = 12.sp
                    )
                } ?: Spacer(modifier = Modifier.height(14.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 5.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    ),
                    shape = RoundedCornerShape(25.dp),
                    onClick = {
                        if (state.value.questions[index].answers.size < 10) {
                            viewModel.onEvent(
                                TestEditScreenEvent.UpdateQuestion(
                                    state.value.questions[index].copy(
                                        answers = state.value.questions[index].answers.plus(
                                            AnswerState(
                                                id = minOf(0, state.value.questions[index].answers.minOf { it.id }) -1,
                                                text = context.getString(
                                                    R.string.answer,
                                                    state.value.questions[index].answers.size.plus(1)
                                                ),
                                                questionId = index
                                            )
                                        )
                                    )
                                )
                            )
                        }
                    }) {
                    Text(
                        text = stringResource(id = R.string.add_answer)
                    )
                }
            }
            val imageData = remember { mutableStateOf<Uri?>(null) }
            val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
                imageData.value = it
                it?.let {
                    viewModel.onEvent(TestEditScreenEvent.UpdateQuestion(state.value.questions[index].copy(
                        photo = ImageUtil.convert(context, it)
                    )))
                }
            }
            val imgModifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .sizeIn(maxHeight = 200.dp)
                .clickable {
                    launcher.launch(
                        "image/*"
                    )
                }
            if (state.value.questions[index].photo == null && imageData.value == null) {
                Image(
                    painter = painterResource(R.drawable.add_image),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = imgModifier
                        .background(
                            color = GrayColor20,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, GrayColor40, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
                val model = if (imageData.value != null) ImageRequest.Builder(LocalContext.current)
                    .data(imageData.value)
                    .crossfade(true)
                    .build()
                else ImageRequest.Builder(LocalContext.current)
                    .data(state.value.questions[index].photo)
                    .crossfade(true)
                    .build()
                AsyncImage(
                    model = model,
                    placeholder = painterResource(R.drawable.placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = imgModifier
                        .background(
                            color = GrayColor20,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, GrayColor40, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                )
            }
            Spacer(
                Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.onBackground)
            )
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                border = ButtonDefaults.outlinedBorder.copy(width = 0.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.secondary
                ),
                elevation = ButtonDefaults.elevation(0.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    viewModel.onEvent(TestEditScreenEvent.DeleteQuestion(state.value.questions[index]))
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colors.secondary
                    )
                    Text(
                        text = stringResource(id = R.string.delete),
                        style = MaterialTheme.typography.button.copy(color = MaterialTheme.colors.secondary)
                    )
                }
            }
        }
    }
}
