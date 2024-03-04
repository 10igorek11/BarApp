package com.example.barapp.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.barapp.R
import com.example.barapp.presentation.theme.BarAppTheme

@Composable
fun LoginScreen(goHome: () -> Unit) {
    val viewModel = hiltViewModel<LoginScreenViewModel>()
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = context){
        viewModel.loggingInEvents.collect{
            when(it){
                LoggingInEvent.SUCCESS -> {
                    goHome()
                }
            }
        }
    }
    Box(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(35.dp)
        ) {
            Text(
                text = stringResource(id = R.string.enter_to_the_system),
                modifier = Modifier.padding(14.dp, 10.dp),
                style = MaterialTheme.typography.h4
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(10.dp, 4.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                label = {
                    Text(text = stringResource(id = R.string.phone_lbl))
                },
                shape = RoundedCornerShape(10.dp),
                value =  state.value.phone,
                isError = state.value.phoneError != null,
                onValueChange = { newValue ->
                    if(newValue.length<=12){
                        viewModel.onEvent(LoginScreenEvent.PhoneNumberChanged(newValue))
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                )
            )
            state.value.phoneError?.let{
                Text(
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    text = it,
                    style = MaterialTheme.typography.caption.copy(
                        color = MaterialTheme.colors.error,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start
                    )
                )
            }

            var passwordVisible by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                label = {
                    Text(text = stringResource(id = R.string.password_lbl))
                },
                modifier = Modifier
                    .padding(10.dp, 4.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                value = state.value.password,
                isError = state.value.passwordError != null,
                onValueChange = { newValue ->
                    if(newValue.length<=100) {
                        viewModel.onEvent(LoginScreenEvent.PasswordChanged(newValue))
                    }
                },
                shape = RoundedCornerShape(10.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                trailingIcon = {
                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, null)
                    }
                }
            )
            state.value.passwordError?.let{
                Text(
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    text = it,
                    style = MaterialTheme.typography.caption.copy(
                        color = MaterialTheme.colors.error,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start
                    )
                )
            }

        }
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(25.dp),
            onClick = {
                viewModel.onEvent(LoginScreenEvent.OnConfirm(context))
            },
            colors =  ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary
            ),
            shape = RoundedCornerShape(15.dp),
            enabled = !state.value.isLoading
        ) {
            Text(
                text = stringResource(id = R.string.log_in),
                style = MaterialTheme.typography.button.copy(fontSize = 16.sp),
                modifier = Modifier.padding(horizontal = 40.dp, vertical = 5.dp)
            )

        }
    }
}


@Preview
@Composable
fun LoginScreenPreview(){
    BarAppTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
        }
    }
}