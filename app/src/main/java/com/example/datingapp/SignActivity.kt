package com.example.datingapp

import android.content.Context
import android.content.Intent
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.datingapp.firebase.FirebaseController
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.ui.theme.backgroundColor
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.foundation.layout.Spacer as Spacer1

@AndroidEntryPoint
class SignActivity : ComponentActivity() {

    @ApplicationContext
    @Inject
    lateinit var context: Context

    @Inject
    lateinit var firebaseAuthService: FirebaseController

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
                        Sign(context, firebaseAuthService)
                    }
                }
            }
        }
    }
}

@Composable
fun Sign(
    context: Context,
    firebaseAuthService: FirebaseController
) {
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        //TODO: Mati - design and if you want - check up points used to collect data
        Text(text = "Authentication")

        OutlinedTextField(
            value = password,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                password = it
            },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            isError = password.isEmpty() && password.length < 6
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            isError = email.isEmpty() && !isValidEmail(email)
        )

        Spacer1(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                firebaseAuthService.logout()
                coroutineScope.launch {
                    try {
                        firebaseAuthService.isCurrentUserRegistered(
                            email = email,
                            password = password
                        )
                    } catch (e: Exception) {
                        Log.i("FIREBASE_AUTHENTICATION", "Success, user do not exist")
                        firebaseAuthService.createNewUser(email = email, password = password)
                        val intent = Intent(context, InterestsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Register")
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        if (firebaseAuthService.isCurrentUserRegistered(
                                email = email,
                                password = password
                            )?.user != null
                        ) {
                            Log.e("FIREBASE_AUTHENTICATION", "Success")
                            val intent = Intent(context, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            context.startActivity(intent)
                        } else {
                            Log.e("FIREBASE_AUTHENTICATION", "Fail")
                        }
                    } catch (e: Exception) {
                        Log.e("FIREBASE_AUTHENTICATION", "Exception was thrown")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }
    }
}

private fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}")
    return emailRegex.matches(email)
}
