package com.example.datingapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.user.UserController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Spacer as Spacer1

@AndroidEntryPoint
class SignActivity : ComponentActivity() {

    @Inject
    lateinit var userControllerImpl: UserController

    @ApplicationContext
    @Inject
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatingAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        RegistrationScreen(context, userControllerImpl)
                    }
                }
            }
        }
    }
}

@Composable
fun RegistrationScreen(context: Context, userControllerImpl: UserController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        //TODO: Mati - design and if you want - check up points used to collect data
        var password: String = ""

        Text(
            text = "Create account")

        OutlinedTextField(
            value = "name",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                userControllerImpl.addData(it)
            },
            label = { Text("Name") }
        )

        OutlinedTextField(
            value = "password",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                password = it
            },
            label = { Text("Password") }
        )

        OutlinedTextField(
            value = "email",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {  },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )

        Spacer1(modifier = Modifier.height(16.dp))

        Spacer1(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Create account")
        }
    }
}
