package com.example.datingapp.activities

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseDataController
import com.example.datingapp.navigation.BottomNav
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.user.UserController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userController: UserController

    @Inject
    lateinit var firebaseDataController: FirebaseDataController

    @Inject
    lateinit var firebaseAuthController: FirebaseAuthController

    @ApplicationContext
    @Inject
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })

        userController.setNecessaryData()
        setContent {
            DatingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    BottomNav(
                        userController,
                        firebaseDataController,
                        context,
                        firebaseAuthController
                    )
                }
            }
        }
    }

}