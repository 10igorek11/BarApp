package com.example.barapp.presentation.screens.tests

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.barapp.R
import com.example.barapp.common.ADMIN_ROLE
import com.example.barapp.common.EMPLOYEE_ROLE
import com.example.barapp.presentation.navigation.DetailsScreen
import com.example.barapp.presentation.theme.GrayColor30
import com.example.barapp.presentation.theme.GrayColor40

@Composable
fun TestsScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<TestsScreenViewModel>()
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current
    var isLoaded by remember {
        mutableStateOf(false)
    }
    val loggedInUser = viewModel.getLoggedInUser()
    if (loggedInUser != null) {
        if (!isLoaded) {
            viewModel.loadTests()
        }
        LaunchedEffect(context) {
            viewModel.loadingEvents.collect {
                isLoaded = when (it) {
                    LoadingEvent.SUCCESS -> {
                        true
                    }

                    LoadingEvent.DEFAULT -> {
                        false
                    }
                }
            }
        }
        if(state.value.errorStatus){
            Snackbar(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .border(1.dp, GrayColor40, RoundedCornerShape(10.dp)),
                backgroundColor = MaterialTheme.colors.surface,
                action = {
                    TextButton(onClick = {
                        viewModel.loadTests()
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
        TestsScreenContent(
            state = state,
            role = loggedInUser.roleId,
            onRefresh = {
                viewModel.loadTests()
            },
            onItemClick = { id ->
                when (loggedInUser.roleId) {
                    ADMIN_ROLE -> {
                        navController.navigate(
                            DetailsScreen.TestEditScreen.route.replace(
                                "{id}",
                                id.toString()
                            )
                        )
                    }
                    EMPLOYEE_ROLE -> {
                        navController.navigate(
                            DetailsScreen.TestScreen.route.replace(
                                "{id}",
                                id.toString()
                            )
                        )
                    }

                    else -> {}
                }
            },
            onAddBtnClick = {
                viewModel.refreshList()
                navController.navigate(DetailsScreen.TestEditScreen.route.replace("{id}", "0"))
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TestsScreenContent(
    state: State<TestsScreenState>,
    role: Int,
    onItemClick: (id: Int) -> Unit,
    onAddBtnClick: () -> Unit,
    onRefresh: () -> Unit
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            item{
                Spacer(Modifier.height(0.dp))
            }
            items(state.value.tests) { test ->
                TestItemPanel(test, onItemClick, role)
            }
            item{
                Spacer(Modifier.height(60.dp))
            }
        }
        if(state.value.tests.isEmpty()){
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                text = stringResource(id = R.string.no_elements),
                style = MaterialTheme.typography.h6.copy(color = GrayColor40, textAlign = TextAlign.Center)
            )
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
private fun TestItemPanel(test: TestItem, onItemClick: (id: Int) -> Unit, role: Int) {
    Card(
        modifier = Modifier
            .padding(horizontal = 13.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, GrayColor40),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(10f),
                painter = painterResource(id = R.drawable.check_ic),
                contentDescription = null,
                tint = if (test.isPassed && role == EMPLOYEE_ROLE) MaterialTheme.colors.primary else GrayColor30
            )
            Row(
                modifier = Modifier.weight(45f, false)
            ) {
                val nameTextStyle = MaterialTheme.typography.h6
                Text(
                    text = test.name,
                    style = if (test.isPassed && role == EMPLOYEE_ROLE) nameTextStyle.copy(textDecoration = TextDecoration.LineThrough) else nameTextStyle
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Button(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .weight(35f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary
                ),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    onItemClick(test.id)
                }) {
                val buttonText = when (role) {
                    ADMIN_ROLE -> {
                        stringResource(id = R.string.edit)
                    }

                    EMPLOYEE_ROLE -> {
                        stringResource(id = R.string.open_test)
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
