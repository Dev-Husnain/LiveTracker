package com.location.livetracker.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorLoading(
    modifier: Modifier = Modifier,
    showLoading: Boolean = true,
    error: String = "",
    screenColor: Color = Color.Transparent
) {
    val bgColor by remember {
        mutableStateOf(if (error.isBlank()) screenColor else Color.White)
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = error.isNotBlank()) {
            Text(
                text = error,
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(20.dp)
            )
        }
        AnimatedVisibility(visible = showLoading && error.isBlank()) {
            CircularProgressIndicator(modifier = Modifier.wrapContentHeight(), color = Color.Blue)
        }
    }
}