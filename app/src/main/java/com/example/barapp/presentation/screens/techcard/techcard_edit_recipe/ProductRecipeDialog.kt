package com.example.barapp.presentation.screens.techcard.techcard_edit_recipe

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import com.example.barapp.R
import com.example.barapp.domain.models.Recipe
import com.example.barapp.presentation.theme.GrayColor40

@Composable
fun ProductRecipeDialog(
    recipe: Recipe = Recipe(),
    onDismiss: () -> Unit,
    onConfirm: (recipe: Recipe) -> Unit
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
                            text = if(recipe.id <=0) stringResource(id = R.string.adding) else stringResource(
                                id = R.string.edit2
                            ),
                            style = MaterialTheme.typography.h5
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    var productText by remember {
                        mutableStateOf(recipe.product)
                    }
                    var productTextError by remember { mutableStateOf<String?>(null) }
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        label = {
                            Text(text = stringResource(id = R.string.input_product_name))
                        },
                        shape = RoundedCornerShape(10.dp),
                        value = productText,
                        isError = productTextError != null,
                        onValueChange = { newValue ->
                            if (newValue.length <= 100) {
                                productTextError = null
                                productText = newValue
                            }
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences,
                        )
                    )
                    productTextError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier
                                .height(14.dp)
                                .padding(horizontal=10.dp),
                            fontSize = 12.sp
                        )
                    } ?: Spacer(Modifier.height(14.dp))

                    var consumptionBruttoText by remember {
                        mutableStateOf(recipe.consumptionBrutto1g)
                    }
                    var consumptionBruttoTextError by remember { mutableStateOf<String?>(null) }
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        label = {
                            Text(text = stringResource(id = R.string.consumption_brutto_short))
                        },
                        shape = RoundedCornerShape(10.dp),
                        value = consumptionBruttoText,
                        isError = consumptionBruttoTextError != null,
                        onValueChange = { newValue ->
                            if (newValue.length <= 9) {
                                consumptionBruttoTextError = null
                                consumptionBruttoText = newValue
                            }
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next,
                        ),
                        )
                    consumptionBruttoTextError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier
                                .height(14.dp)
                                .padding(horizontal=10.dp),
                            fontSize = 12.sp
                        )
                    } ?: Spacer(Modifier.height(14.dp))

                    var consumptionNettoText by remember {
                        mutableStateOf(recipe.consumptionNetto1g)
                    }
                    var consumptionNettoTextError by remember { mutableStateOf<String?>(null) }
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        label = {
                            Text(text = stringResource(id = R.string.consumption_netto_short))
                        },
                        shape = RoundedCornerShape(10.dp),
                        value = consumptionNettoText,
                        isError = consumptionNettoTextError != null,
                        onValueChange = { newValue ->
                            if (newValue.length <= 9) {
                                consumptionNettoTextError = null
                                consumptionNettoText = newValue
                            }
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next,
                        ),
                        )
                    consumptionNettoTextError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier
                                .height(14.dp)
                                .padding(horizontal=10.dp),
                            fontSize = 12.sp
                        )
                    } ?: Spacer(Modifier.height(14.dp))

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
                                var isError = false
                                if (productText.trim().isBlank()) {
                                    productTextError = context.getString(R.string.required_field_error)
                                    isError = true
                                }
                                if (consumptionBruttoText.trim().isBlank()) {
                                    consumptionBruttoTextError = context.getString(R.string.required_field_error)
                                    isError = true
                                }
                                if (consumptionNettoText.trim().isBlank()) {
                                    consumptionNettoTextError = context.getString(R.string.required_field_error)
                                    isError = true
                                }
                                if(isError) return@Button
                                if(!consumptionBruttoText.trim().isDigitsOnly()){
                                    consumptionBruttoTextError = context.getString(R.string.invalid_format)
                                    isError = true
                                }
                                if(!consumptionNettoText.trim().isDigitsOnly()){
                                    consumptionNettoTextError = context.getString(R.string.invalid_format)
                                    isError = true
                                }
                                if(isError) return@Button
                                onConfirm(recipe.copy(
                                    product = productText,
                                    consumptionBrutto1g = consumptionBruttoText,
                                    consumptionNetto1g = consumptionNettoText
                                ))
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