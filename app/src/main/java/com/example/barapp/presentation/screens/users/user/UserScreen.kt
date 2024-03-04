package com.example.barapp.presentation.screens.users.user

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.barapp.R
import com.example.barapp.domain.models.User
import com.example.barapp.presentation.screens.home.TopBar
import com.example.barapp.presentation.theme.BarAppTheme
import com.example.barapp.presentation.theme.GrayColor40

@Composable
fun UserScreen(
    navController: NavHostController,
    userId: Int
) {
    val viewModel = hiltViewModel<UserScreenViewModel>()
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current
    val loggedInUser = viewModel.getLoggedInUser()
    viewModel.onEvent(UserScreenEvent.OnLoad(userId))

    LaunchedEffect(key1 = context) {
        viewModel.serverEvents.collect {
            when (it) {
                ServerEvent.SUCCESS -> {
                    navController.popBackStack()
                }
                ServerEvent.ERROR -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.server_error),
                        Toast.LENGTH_LONG
                    ).show()
                    navController.popBackStack()
                }
            }
        }
    }
    if (loggedInUser != null) {
        UserScreenContent(
            state = state,
            loggedInUser = loggedInUser,
            viewModel = viewModel,
            onBackBtnClick = {
                navController.popBackStack()
            }

        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun UserScreenContent(
    viewModel: UserScreenViewModel,
    onBackBtnClick: () -> Unit,
    state: State<UserScreenState>,
    loggedInUser: User
) {
    Scaffold(
        topBar = {
            TopBar(
                navigationIcon = {
                    IconButton(onClick = {
                        onBackBtnClick()
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
                        text = if (state.value.userId == 0) stringResource(id = R.string.new_user) else stringResource(
                            id = R.string.user
                        ),
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
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .size(60.dp)
                    .border(1.dp, MaterialTheme.colors.background, CircleShape),
                backgroundColor = MaterialTheme.colors.secondary,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                onClick = {
                    viewModel.onEvent(UserScreenEvent.OnConfirm)
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
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colors.background)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(13.dp)
            ) {
                item {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        label = {
                            Text(text = stringResource(id = R.string.first_name))
                        },
                        shape = RoundedCornerShape(10.dp),
                        value = state.value.firstName,
                        isError = state.value.firstNameError != null,
                        onValueChange = { newValue ->
                            if (newValue.length <= 100) {
                                viewModel.onEvent(UserScreenEvent.FirstNameChanged(newValue))
                            }
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next
                        )
                    )
                }
                item {
                    state.value.firstNameError?.let { errorText ->
                        Text(
                            text = errorText,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier
                                .height(14.dp),
                            fontSize = 12.sp
                        )
                    }?: Spacer(modifier = Modifier.height(14.dp))
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        label = {
                            Text(text = stringResource(id = R.string.last_name))
                        },
                        shape = RoundedCornerShape(10.dp),
                        value = state.value.lastName,
                        isError = state.value.lastNameError != null,
                        onValueChange = { newValue ->
                            if (newValue.length <= 100) {
                                viewModel.onEvent(UserScreenEvent.LastNameChanged(newValue))
                            }
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next
                        )
                    )
                }
                item {
                    state.value.lastNameError?.let { errorText ->
                        Text(
                            text = errorText,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier
                                .height(14.dp),
                            fontSize = 12.sp
                        )
                    }?: Spacer(modifier = Modifier.height(14.dp))
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        label = {
                            Text(text = stringResource(id = R.string.middle_name))
                        },
                        shape = RoundedCornerShape(10.dp),
                        value = state.value.middleName ?: "",
                        isError = state.value.middleNameError != null,
                        onValueChange = { newValue ->
                            if (newValue.length <= 100) {
                                viewModel.onEvent(UserScreenEvent.MiddleNameChanged(newValue))
                            }
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next
                        )
                    )
                }
                item {
                    state.value.middleNameError?.let { errorText ->
                        Text(
                            text = errorText,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier
                                .height(14.dp),
                            fontSize = 12.sp
                        )
                    }?: Spacer(modifier = Modifier.height(14.dp))
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        label = {
                            Text(text = stringResource(id = R.string.phone_lbl))
                        },
                        shape = RoundedCornerShape(10.dp),
                        value = state.value.phone,
                        isError = state.value.phoneError != null,
                        onValueChange = { newValue ->
                            if (newValue.length <= 12) {
                                viewModel.onEvent(UserScreenEvent.PhoneChanged(newValue))
                            }
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                    )
                }
                item {
                    state.value.phoneError?.let { errorText ->
                        Text(
                            text = errorText,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier
                                .height(14.dp),
                            fontSize = 12.sp
                        )
                    }?: Spacer(modifier = Modifier.height(14.dp))
                }
                item {
                    var passwordVisible by rememberSaveable { mutableStateOf(false) }
                    OutlinedTextField(
                        label = {
                            Text(text = stringResource(id = R.string.password_lbl))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        value = state.value.password,
                        isError = state.value.passwordError != null,
                        onValueChange = { newValue ->
                            if (newValue.length <= 100) {
                                viewModel.onEvent(UserScreenEvent.PasswordChanged(newValue))
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    null
                                )
                            }
                        }
                    )
                }
                item {
                    state.value.passwordError?.let { errorText ->
                        Text(
                            text = errorText,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier
                                .height(14.dp),
                            fontSize = 12.sp
                        )
                    }?: Spacer(modifier = Modifier.height(14.dp))
                }
                item {
                    Text(
                        text = stringResource(id = R.string.role_lbl),
                        style = MaterialTheme.typography.h6
                    )
                    var expanded by remember { mutableStateOf(false) }
                    val isEnabled = loggedInUser.id != state.value.userId
                    Spacer(modifier = Modifier.height(5.dp))
                    ExposedDropdownMenuBox(
                        modifier = Modifier
                            .fillMaxWidth(),
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.value.roles.find { r -> r.id == state.value.roleId }?.name
                                ?: "",
                            onValueChange = {},
                            readOnly = true,
                            textStyle = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.Medium,
                                color = GrayColor40
                            ),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(if (isEnabled) expanded else false) },
                            shape = RoundedCornerShape(10.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                backgroundColor = MaterialTheme.colors.surface
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = if (isEnabled) expanded else false,
                            onDismissRequest = { expanded = false }
                        ) {
                            state.value.roles.forEach { role ->
                                DropdownMenuItem(
                                    content = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                modifier = Modifier.weight(1f),
                                                text = role.name,
                                                style = MaterialTheme.typography.body2.copy(
                                                    fontWeight = FontWeight.Medium
                                                ),
                                            )
                                        }
                                    },
                                    onClick = {
                                        viewModel.onEvent(UserScreenEvent.RoleChanged(role.id))
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewUser() {
    BarAppTheme {
    }
}
