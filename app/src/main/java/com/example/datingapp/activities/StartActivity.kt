package com.example.datingapp.activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.NotificationManagerCompat
import com.example.datingapp.R
import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseDataController
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.ui.theme.Typography
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.utils.InternetCheckService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseDataController: FirebaseDataController

    @Inject
    lateinit var firebaseAuthController: FirebaseAuthController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startForegroundService(Intent(applicationContext, InternetCheckService::class.java))
        setContent {
            DatingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = backgroundColor
                ) {
                    val notificationManager = NotificationManagerCompat.from(applicationContext)
                    val areNotificationsEnabled = notificationManager.areNotificationsEnabled()

                    if (!areNotificationsEnabled) {
                        // Notifications are not enabled
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, applicationContext.packageName)
                        startActivity(intent)
                    }
                    MainContentColumn()
                }
            }
        }
    }

    @Composable
    private fun MainContentColumn() {
        val coroutineScope = rememberCoroutineScope()
        val startAppTexts = resources.getStringArray(R.array.start_app_text_array)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = startAppTexts[(startAppTexts.indices).random()],
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = Typography.h1
            )
            ClickableImage(
                image = painterResource(id = R.drawable.heart_icon),
                contentDescription = stringResource(id = R.string.heart_image_description),
                onClick = {
                    coroutineScope.launch {
                        if (firebaseAuthController.isCurrentUserSigned()) {
                            if (firebaseDataController.isProfileSetUp()) {
                                val intent =
                                    Intent(applicationContext, MainActivity::class.java)
                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                                intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
                                applicationContext.startActivity(intent)
                            } else {
                                val intent =
                                    Intent(applicationContext, SetUpActivity::class.java)
                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                                intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
                                applicationContext.startActivity(intent)
                            }
                        } else {
                            val intent =
                                Intent(applicationContext, SignActivity::class.java)
                            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                            applicationContext.startActivity(intent)
                        }
                    }
                }
            )
        }
    }

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
}