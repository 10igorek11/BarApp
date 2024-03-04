package com.example.barapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.barapp.presentation.theme.GrayColor40

@Composable
fun ImageViewer(modifier: Modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)), showImage: ImageRequest?,  deleteAllowed:Boolean = false, onDismiss:()->Unit,onDelete: ()->Unit = {}) {
    Box(
        modifier = modifier
    ) {
        var scale by remember { mutableFloatStateOf(1f) }
        var xOffset by remember { mutableFloatStateOf(0f) }
        var yOffset by remember { mutableFloatStateOf(0f) }
        Box(
            modifier = Modifier
                .clip(RectangleShape)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = maxOf(1f, minOf(scale * zoom, 4f))
                        val maxX = (size.width * (scale - 1)) / 2
                        val minX = -maxX
                        xOffset = maxOf(minX, minOf(maxX, xOffset + pan.x))
                        val maxY = (size.height * (scale - 1)) / 2
                        val minY = -maxY
                        yOffset = maxOf(minY, minOf(maxY, yOffset + pan.y))
                    }
                }
        ) {
            AsyncImage(
                model = showImage,
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = xOffset,
                        translationY = yOffset,
                    ),
                contentDescription = null
            )
        }
        IconButton(
            onClick = {
                onDismiss()
            },
            modifier = Modifier
                .padding(15.dp)
                .size(45.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                tint = MaterialTheme.colors.primary
            )

        }
        if(deleteAllowed){
            IconButton(
                onClick = {
                    onDelete()
                    onDismiss()
                },
                modifier = Modifier
                    .padding(15.dp)
                    .size(60.dp)
                    .align(Alignment.BottomEnd)
            ){
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    tint = MaterialTheme.colors.secondary
                )
            }
        }
    }
}