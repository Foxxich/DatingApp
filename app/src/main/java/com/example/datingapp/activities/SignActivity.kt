package com.example.datingapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.datingapp.connection.InternetCheckService
import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseDataController
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.ui.theme.Typography
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.utils.CommonSettings
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
    lateinit var firebaseDataController: FirebaseDataController

    @Inject
    lateinit var firebaseAuthController: FirebaseAuthController

    @Inject
    lateinit var internetCheckService: InternetCheckService

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
                        Sign()
                    }
                }
            }
        }
        if (!internetCheckService.isInternetConnected(applicationContext)) {
            CommonSettings.showConnectionLost(applicationContext)
        }
    }


    @Composable
    fun Sign() {
        var password by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()

        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                verticalArrangement = Arrangement.Center
        ) {
            Text(
                    text = "Authentication",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = Typography.h1
            )

            OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    label = {
                        Text(
                                text = "Email",
                                style = Typography.body1
                        )
                    },
                    modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
            )

            val passwordVisibilityState = remember { mutableStateOf(false) }

            OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(
                                text = "Password",
                                style = Typography.body1
                        )
                    },
                    visualTransformation = if (passwordVisibilityState.value) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(
                                onClick = { passwordVisibilityState.value = !passwordVisibilityState.value }
                        ) {
                            val visibilityIcon = if (passwordVisibilityState.value) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            }
                            Icon(
                                    imageVector = visibilityIcon,
                                    contentDescription = "Toggle Password Visibility",
                                    modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
            )

            Spacer1(modifier = Modifier.height(16.dp))
            var showDialog by remember { mutableStateOf(false) }
            var showParametersDialog by remember { mutableStateOf(false) }

            Row(
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                        onClick = {
                            firebaseAuthController.logout()
                            coroutineScope.launch {
                                if (Patterns.EMAIL_ADDRESS.matcher(email)
                                                .matches() && password.length >= 6
                                ) {
                                    try {
                                        if (firebaseAuthController.emailExists(email)) {
                                            showDialog = true
                                        } else {
                                            firebaseDataController.createNewUser(
                                                email = email,
                                                password = password
                                            )
                                            val intent = Intent(context, SetUpActivity::class.java)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            context.startActivity(intent)
                                        }
                                    } catch (e: Exception) {
                                        Log.i("FIREBASE_AUTHENTICATION", "Error")
                                    }
                                } else {
                                    showParametersDialog = true
                                }
                            }
                        },
                        modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(end = 5.dp)
                ) {
                    androidx.compose.material.Text(
                            text = "Register",
                            style = Typography.button
                    )
                }
                Button(
                        onClick = {
                            coroutineScope.launch {
                                if (Patterns.EMAIL_ADDRESS.matcher(email)
                                                .matches() && password.length >= 6
                                ) {
                                    try {
                                        if (firebaseAuthController.isCurrentUserRegistered(
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
                                            showDialog = true
                                        }
                                    } catch (e: Exception) {
                                        showParametersDialog = true
                                        Log.e("FIREBASE_AUTHENTICATION", "Exception was thrown")
                                    }
                                } else {
                                    showParametersDialog = true
                                }
                            }
                        },
                        modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(start = 5.dp)
                ) {
                    androidx.compose.material.Text(
                            text = "Login",
                            style = Typography.button,
                    )
                }
            }

            if (showDialog) {
                AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("User already exists") },
                        text = { Text("Do you want to try more") },
                        confirmButton = {
                            Button(
                                    onClick = {
                                        showDialog = false
                                        password = ""
                                        email = ""
                                    },
                                    colors = ButtonDefaults.buttonColors()
                            ) {
                                Text("Try more")
                            }
                        },
                        dismissButton = {
                            Button(
                                    onClick = {
                                        finishAffinity()
                                    }
                            ) {
                                Text("Close app")
                            }
                        }
                )
            }

            if (showParametersDialog) {
                AlertDialog(
                        onDismissRequest = { showParametersDialog = false },
                        title = { Text("Incorrect email or password") },
                        text = { Text("Email contains @ and password length is at least 6 symbols") },
                        confirmButton = {
                            Button(
                                    onClick = {
                                        showParametersDialog = false
                                        password = ""
                                        email = ""
                                    },
                                    colors = ButtonDefaults.buttonColors()
                            ) {
                                Text("Try more")
                            }
                        },
                        dismissButton = {
                            Button(
                                    onClick = {
                                        finishAffinity()
                                    }
                            ) {
                                Text("Close app")
                            }
                        }
                )
            }
        }
    }
}