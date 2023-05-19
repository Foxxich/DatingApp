package com.example.datingapp.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
fun ClickableImage(image: Painter, contentDescription: String, onClick: () -> Unit) {
    Image(
        painter = image,
        contentDescription = contentDescription,
        modifier = Modifier
            .noRippleClickable(onClick = onClick)
            .fillMaxWidth(),
        contentScale = ContentScale.FillWidth
    )
}

private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}