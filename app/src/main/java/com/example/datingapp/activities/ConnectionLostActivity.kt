package com.example.datingapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.datingapp.R
import com.example.datingapp.connection.InternetCheckService
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.ui.theme.Typography
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.utils.ClickableImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConnectionLostActivity : ComponentActivity() {

    @Inject
    lateinit var internetCheckService: InternetCheckService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = backgroundColor
                ) {
                    ConnectionColumn()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }

    @Composable
    fun ConnectionColumn() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.connection),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = Typography.h1
            )
            ClickableImage(
                image = painterResource(id = R.drawable.connection),
                contentDescription = stringResource(id = R.string.web_image_description),
                onClick = {
                    if (internetCheckService.isInternetConnected(applicationContext)) {
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "You need to check up connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }
}