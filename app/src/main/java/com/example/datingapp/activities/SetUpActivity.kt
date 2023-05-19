package com.example.datingapp.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datingapp.R
import com.example.datingapp.connection.InternetCheckService
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.ui.theme.whiteColor
import com.example.datingapp.user.Interest
import com.example.datingapp.user.UserController
import com.example.datingapp.utils.CommonSettings
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class SetUpActivity : ComponentActivity() {

    @Inject
    lateinit var userControllerImpl: UserController

    @Inject
    lateinit var internetCheckService: InternetCheckService

    @ApplicationContext
    @Inject
    lateinit var context: Context

    lateinit var mainImage: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mapOfInterests = arrayListOf<Interest>()
            Interest.values().forEach {
                mapOfInterests.add(it)
            }
            DatingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = backgroundColor
                ) {
                    InterestsColumn(mapOfInterests)
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
        if (!internetCheckService.isInternetConnected(applicationContext)) {
            CommonSettings.showConnectionLost(applicationContext)
        }
    }

    @Composable
    private fun InterestsColumn(mapOfInterests: ArrayList<Interest>) {
        mainImage = Uri.parse("android.resource://${packageName}/${R.raw.user_image}")
        val chosenInterests = remember { mutableStateListOf<Interest>() }
        var userName by remember { mutableStateOf("") }
        val imageChosen = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .padding(10.dp)
                .background(whiteColor)
        ) {
            var i = 0
            Column(modifier = Modifier.padding(16.dp)) {
                while (i < mapOfInterests.size) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (j in i until i + 3) {
                            if (j < mapOfInterests.size) {
                                var clicked by remember { mutableStateOf(false) }
                                OutlinedButton(
                                    onClick = {
                                        chosenInterests.add(mapOfInterests[j])
                                        clicked = true
                                    },
                                    border = BorderStroke(
                                        2.dp,
                                        if (clicked) Color.Red else Color.Blue
                                    )
                                ) {
                                    Text(
                                        text = mapOfInterests[j].name, maxLines = 1,
                                        overflow = TextOverflow.Clip, fontSize = 12.sp
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.width(38.dp))
                            }
                        }
                    }
                    i += 3
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {

                OutlinedTextField(
                    value = userName,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        userName = it
                    },
                    label = { Text("UserName") },
                    isError = userName.isEmpty()
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        imageChosen.value = true
                        val galleryIntent = Intent(Intent.ACTION_PICK)
                        galleryIntent.type = "image/*"
                        imagePickerActivityResult.launch(galleryIntent)
                    }) {
                        Text(text = "Choose photo")
                    }

                    Button(onClick = {
                        if (!imageChosen.value) {
                            Toast.makeText(context, "You did not choose image!", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            userControllerImpl.uploadToDatabase(
                                userName,
                                chosenInterests,
                                mainImage
                            )
                            val intent =
                                Intent(applicationContext, VideoActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            applicationContext.startActivity(intent)
                        }
                    }) {
                        Text(text = "Finish")
                    }
                }
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                val imageUri: Uri? = result.data?.data
                if (imageUri?.path != null) {
                    this.mainImage = imageUri
                }
            } catch (e: Exception) {
                Toast.makeText(context, "You did not choose image!", Toast.LENGTH_SHORT).show()
            }
        }
}