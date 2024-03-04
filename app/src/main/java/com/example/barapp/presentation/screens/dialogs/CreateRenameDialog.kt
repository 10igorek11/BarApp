package com.example.barapp.presentation.screens.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.barapp.R
import com.example.barapp.presentation.theme.GrayColor40

@Composable
fun CreateRenameDialog(
    name: String = "",
    onDismiss: () -> Unit,
    onConfirm: (text: String) -> Unit
) {
    Dialog(onDismissRequest = {
        onDismiss()
    })
    {
        val context = LocalContext.current
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
                            text = if(name.isEmpty()) stringResource(id = R.string.create) else stringResource(
                                id = R.string.rename
                            ),
                            style = MaterialTheme.typography.h5
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    val state = remember {
                        mutableStateOf(TextFieldValue(name))
                    }
                    var textError by remember { mutableStateOf<String?>(null) }
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(10.dp, 4.dp)
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    val text = state.value.text
                                    state.value = state.value.copy(
                                        selection = TextRange(0, text.length)
                                    )
                                }
                            },
                        label = {
                            Text(text = stringResource(id = R.string.input_name))
                        },
                        shape = RoundedCornerShape(10.dp),
                        value = state.value,
                        isError = textError != null,
                        onValueChange = { newValue ->
                            if (newValue.text.length <= 100) {
                                textError = null
                                state.value = newValue
                            }
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Sentences,
                        )
                    )
                    textError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier
                                .height(14.dp)
                                .padding(horizontal=10.dp),
                            fontSize = 12.sp
                        )
                    } ?: Spacer(Modifier.height(14.dp))

                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.secondary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                if (state.value.text.trim().isBlank()) {
                                    textError = context.getString(R.string.required_field_error)
                                    return@Button
                                }
                                onConfirm(state.value.text.trim())
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