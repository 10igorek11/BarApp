package com.example.barapp.presentation.screens.users

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.barapp.R
import com.example.barapp.common.ADMIN_ROLE
import com.example.barapp.domain.models.UserDto
import com.example.barapp.presentation.navigation.DetailsScreen
import com.example.barapp.presentation.theme.BarAppTheme
import com.example.barapp.presentation.theme.GrayColor30
import com.example.barapp.presentation.theme.GrayColor40

@Composable
fun UsersScreen(
    navController: NavHostController,
    logout: () -> Unit
) {
    val viewModel = hiltViewModel<UsersScreenViewModel>()
    val state = viewModel.state.collectAsState()
    val loggedInUser = viewModel.getLoggedInUser()
    if (loggedInUser != null) {
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
                        viewModel.onEvent(UsersScreenEvent.OnLoad)
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
        viewModel.onEvent(UsersScreenEvent.OnLoad)
        UsersScreenContent(
            state = state,
            onRefresh = {
                viewModel.onEvent(UsersScreenEvent.OnLoad)
            },
            onItemClick = { id ->
                viewModel.onEvent(UsersScreenEvent.OnClear)
                navController.navigate(
                    DetailsScreen.UserScreen.route.replace(
                        "{id}",
                        id.toString()
                    )
                )
            },
            viewModel = viewModel,
            onAddBtnClick = {
                viewModel.onEvent(UsersScreenEvent.OnClear)
                navController.navigate(
                    DetailsScreen.UserScreen.route.replace(
                        "{id}",
                        "0"
                    )
                )
            }
        )
        LaunchedEffect(key1 = LocalContext.current) {
            viewModel.deleteEvents.collect {
                when (it) {
                    UserDeleteEvent.CurrentUserDeleted -> {
                        logout()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun UsersScreenContent(
    state: State<UsersScreenState>,
    viewModel: UsersScreenViewModel,
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
            item {
                Spacer(Modifier.height(0.dp))
            }
            items(state.value.users) { user ->
                val isCurrentUser = viewModel.getLoggedInUser()?.id == user.id
                UserItemPanel(user, isCurrentUser, onItemClick) {
                    viewModel.onEvent(UsersScreenEvent.OnDelete(it))
                }
            }
            item {
                Spacer(Modifier.height(60.dp))
            }
        }
        if (state.value.users.isEmpty()) {
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

        PullRefreshIndicator(
            refreshing = state.value.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
        FloatingActionButton(
            modifier = Modifier
                .padding(15.dp)
                .size(60.dp)
                .align(Alignment.BottomEnd)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun UserItemPanel(
    user: UserDto,
    isCurrentUser: Boolean,
    onItemClick: (id: Int) -> Unit,
    onDelete: (id: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 13.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, GrayColor40),
        backgroundColor = MaterialTheme.colors.surface,
        onClick = { onItemClick(user.id) }
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
                    .padding(start = 15.dp)
                    .weight(15f),
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = if (user.roleId == ADMIN_ROLE) MaterialTheme.colors.primary else GrayColor30
            )
            Row(
                modifier = Modifier.weight(65f, false)
            ) {
                Column {
                    val nameTextStyle = MaterialTheme.typography.h6
                    Text(
                        text = "${user.lastName} ${user.firstName} ${user.middleName?:""} " + if (isCurrentUser) stringResource(
                            id = R.string.you
                        ) else "",
                        style = if (user.roleId == ADMIN_ROLE) nameTextStyle.copy(textDecoration = TextDecoration.Underline) else nameTextStyle
                    )
                    for (passedTest in user.passedTests) {
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = "✔ " + passedTest.name,
                            style = nameTextStyle.copy(
                                color = GrayColor40,
                                fontSize = 12.sp,
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            IconButton(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .weight(15f),
                onClick = {
                    onDelete(user.id)
                }) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewUsers() {
    BarAppTheme {
    }
}
