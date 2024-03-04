package com.example.barapp.presentation.screens.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.barapp.R
import com.example.barapp.presentation.theme.GrayColor40

@Composable
fun SaveQuitDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
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
                            text = stringResource(
                                id = R.string.save_changes_and_quit
                            ),
                            style = MaterialTheme.typography.caption.copy(
                                color = MaterialTheme.colors.onBackground,
                                fontSize = 16.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(horizontal = 10.dp),
                            onClick = {
                                onConfirm()
                                onDismiss()
                            }) {
                            Text(
                                text = stringResource(id = R.string.yes),
                                color = MaterialTheme.colors.primary
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(horizontal = 10.dp),
                            onClick = {
                                onDismiss()
                            }) {
                            Text(
                                text = stringResource(id = R.string.no),
                                color = MaterialTheme.colors.onBackground
                            )
                        }
                    }
                }
            }
        }
    }
}