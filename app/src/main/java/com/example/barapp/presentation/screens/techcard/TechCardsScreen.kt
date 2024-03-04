package com.example.barapp.presentation.screens.techcard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.barapp.R
import com.example.barapp.common.ADMIN_ROLE
import com.example.barapp.common.EMPLOYEE_ROLE
import com.example.barapp.domain.models.TechCardDto
import com.example.barapp.presentation.navigation.DetailsScreen
import com.example.barapp.presentation.theme.GrayColor40

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TechCardsScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<TechCardsScreenViewModel>()
    val state = viewModel.state.collectAsState()
    var isTypeDialogShowed by remember {
        mutableStateOf(false)
    }
    viewModel.onEvent(TechCardsScreenEvent.LoadData)
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
                        viewModel.onEvent(TechCardsScreenEvent.LoadData)
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
        TechCardsScreenContent(
            state = state,
            onRefresh = {
              viewModel.onEvent(TechCardsScreenEvent.LoadData)
            },
            role = loggedInUser.roleId,
            filterBar = {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 13.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(80f)
                            .padding(end = 10.dp)
                            .height(IntrinsicSize.Min),
                        placeholder = {
                            Text(text = stringResource(id = R.string.search_lbl))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        value = state.value.searchText,
                        onValueChange = { newValue ->
                            if (newValue.length <= 50) {
                                viewModel.onEvent(TechCardsScreenEvent.SearchTextChanged(newValue))
                            }
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface,
                            unfocusedIndicatorColor = MaterialTheme.colors.onBackground
                        )
                    )
                    Box(
                        modifier = Modifier
                            .weight(20f)
                    ) {
                        IconButton(
                            modifier = Modifier
                                .wrapContentSize()
                                .background(
                                    color = if (state.value.selectedType == null) MaterialTheme.colors.surface else MaterialTheme.colors.primary,
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .align(Alignment.Center)
                                .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
                                .clip(RoundedCornerShape(15.dp)),
                            onClick = {
                                isTypeDialogShowed = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.FilterAlt,
                                contentDescription = null,
                                tint = if (state.value.selectedType == null) MaterialTheme.colors.primary else MaterialTheme.colors.surface
                            )
                        }
                    }
                }
            },
            onItemClick = { id ->
                when (loggedInUser.roleId) {
                    ADMIN_ROLE -> {
                        navController.navigate(
                            DetailsScreen.TechCardEditScreen.route.replace(
                                "{id}",
                                id.toString()
                            )
                        )
                    }

                    EMPLOYEE_ROLE -> {
                        navController.navigate(
                            DetailsScreen.TechCardScreen.route.replace(
                                "{id}",
                                id.toString()
                            )
                        )
                    }
                    //todo opening

                    else -> {}
                }
            },
            onAddBtnClick = {
                navController.navigate(DetailsScreen.TechCardEditScreen.route.replace("{id}", "0"))
            }
        )
        if (isTypeDialogShowed) {
            SelectTypeDialog(
                roleId = loggedInUser.roleId,
                onDismiss = { isTypeDialogShowed = false },
                state = state,
                viewModel = viewModel
            ) {
                navController.navigate(
                    DetailsScreen.TypesScreen.route
                )
            }
        }
    }

}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun SelectTypeDialog(
    onDismiss: () -> Unit,
    state: State<TechCardsScreenState>,
    viewModel: TechCardsScreenViewModel,
    roleId: Int,
    onGoTypesScreenClick: () -> Unit
) {
    Dialog(onDismissRequest = {
        onDismiss()
    })
    {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp)
                .shadow(
                    10.dp,
                    RoundedCornerShape(15.dp),
                    ambientColor = Color.Black,
                    spotColor = Color.Gray
                ),
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(2.dp, GrayColor40)
        ) {
            Box(
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(horizontal = 15.dp, vertical = 12.dp),
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = stringResource(id = R.string.filter),
                            style = MaterialTheme.typography.h5
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(id = R.string.cocktail_type),
                        style = MaterialTheme.typography.h6
                    )
                    var expanded by remember { mutableStateOf(false) }
                    var selectedType by remember {
                        mutableStateOf(state.value.selectedType)
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedType?.name ?: stringResource(id = R.string.no_chosen),
                            onValueChange = {},
                            readOnly = true,
                            textStyle = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.Medium,
                                color = GrayColor40
                            ),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            state.value.cocktailTypes.forEach{type ->
                                DropdownMenuItem(
                                    content = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ){
                                            Text(
                                                modifier = Modifier.weight(1f),
                                                text = type.name,
                                                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Medium),
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedType = if(type.id==0) null else type
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    if(roleId== ADMIN_ROLE){
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp, vertical = 5.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary
                            ),
                            shape = RoundedCornerShape(25.dp),
                            onClick = {
                                onGoTypesScreenClick()
                            }) {
                            Text(
                                text = stringResource(id = R.string.add_cocktail_type)
                            )
                        }
                    }
                    Box (
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.secondary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                viewModel.onEvent(
                                    TechCardsScreenEvent.SelectedTypeChanged(selectedType)
                                )
                                onDismiss()
                            }) {
                            Text(
                                text = stringResource(id = R.string.confirm)
                            )
                        }
                    }
                }
                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = {
                        onDismiss()
                    }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TechCardsScreenContent(
    state: State<TechCardsScreenState>,
    filterBar: @Composable () -> Unit,
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
            item {
                Spacer(Modifier.height(0.dp))
            }
            item {
                filterBar()
                if (state.value.techCards.isEmpty()) {
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
            }
            items(state.value.techCards) { techCard ->
                TechCardsItemPanel(techCard, onItemClick)
            }
            item {
                Spacer(Modifier.height(60.dp))
            }
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TechCardsItemPanel(techCard: TechCardDto, onItemClick: (id: Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 13.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, GrayColor40),
        backgroundColor = MaterialTheme.colors.surface,
        onClick = {
            onItemClick(techCard.id)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(15f),
                painter = painterResource(id = R.drawable.cocktail_ic),
                contentDescription = null,
                tint = GrayColor40
            )
            Row(
                modifier = Modifier.weight(75f, false)
            ) {
                Text(
                    text = techCard.name,
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
