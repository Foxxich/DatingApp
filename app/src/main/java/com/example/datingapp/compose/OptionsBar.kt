package com.example.datingapp.compose

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.datingapp.AccountActivity
import com.example.datingapp.MainActivity
import com.example.datingapp.MessagesActivity
import com.example.datingapp.R
import com.example.datingapp.compose.ActiveIcon.*
import com.example.datingapp.ui.theme.whiteColor

class OptionsBar(
    private val context: Context,
    private val activeIcon: ActiveIcon
) {

    @Composable
    fun PrepareOptionsBar() {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 16.dp, bottom = 16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = whiteColor),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PrepareImage(
                painter = painterResource(id = R.drawable.heart),
                modifier = Modifier.weight(1f),
                isActiveIcon = activeIcon == HEART_ICON,
                activityToRun = MainActivity::class.java
            )
            Spacer(modifier = Modifier.width(16.dp))
            PrepareImage(
                painter = painterResource(id = R.drawable.heart),
                modifier = Modifier.weight(1f),
                isActiveIcon = activeIcon == MESSAGES_ICON,
                activityToRun = MessagesActivity::class.java
            )
            Spacer(modifier = Modifier.width(16.dp))
            PrepareImage(
                painter = painterResource(id = R.drawable.heart),
                modifier = Modifier.weight(1f),
                isActiveIcon = activeIcon == ACCOUNT_ICON,
                activityToRun = AccountActivity::class.java
            )
        }
    }

    @Composable
    fun <T> PrepareImage(
        painter: Painter,
        modifier: Modifier,
        isActiveIcon: Boolean,
        activityToRun: Class<T>
    ) {
        val contentScale = ContentScale.FillHeight
        Surface(
            modifier = modifier,
            elevation = 4.dp
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        enabled = !isActiveIcon,
                        onClick = {
                            if (!isActiveIcon) {
                                val intent = Intent(context, activityToRun)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                context.startActivity(intent)
                            }
                        }
                    ),
                contentScale = contentScale,
            )
        }
    }
}
