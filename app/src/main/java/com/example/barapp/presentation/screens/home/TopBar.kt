package com.example.barapp.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.surface,
    elevation: Dp = 6.dp,
    navigationIcon: @Composable ()->Unit,
    title: @Composable ()->Unit,
    menu: @Composable ()->Unit = {}
){
    Card(
        modifier = modifier
            .background(backgroundColor),
        elevation = elevation){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                Modifier
                    .wrapContentSize()
                    .weight(15f)
            ){
                navigationIcon()
            }
            Box(
                Modifier
                    .wrapContentSize()
                    .weight(70f)
            ){
                title()
            }
            Box(
                Modifier
                    .wrapContentSize()
                    .weight(15f)
            ){
                menu()
            }
        }
    }
}