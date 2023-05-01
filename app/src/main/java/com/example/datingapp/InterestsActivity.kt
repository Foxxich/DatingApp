package com.example.datingapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.ui.theme.whiteColor
import com.example.datingapp.user.Interest
import com.example.datingapp.user.UserController
import com.example.datingapp.utils.CommonSettings.TAG
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class InterestsActivity : ComponentActivity() {

    @Inject
    lateinit var userControllerImpl: UserController

    @ApplicationContext
    @Inject
    lateinit var context: Context

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
    }

    @Composable
    private fun InterestsColumn(mapOfInterests: ArrayList<Interest>) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .background(whiteColor)
        ) {
            var userName by remember { mutableStateOf("") }
            val chosenInterests = mutableListOf<Interest>()
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
                                OutlinedButton(onClick = {
                                    chosenInterests.add(mapOfInterests[j])
                                    clicked = true
                                },
                                    border = BorderStroke(2.dp, if (clicked) Color.Red else Color.Blue)
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
                    var selectedImageUri: Uri

                    GalleryImagePicker(onImageSelected = { uri ->
                        selectedImageUri = uri
                    })

                    Button(onClick = {
                        Log.e(TAG,  chosenInterests.size.toString())
                        userControllerImpl.addUserName(userName)
                        userControllerImpl.addUserInterests(chosenInterests)
                        userControllerImpl.uploadToDatabase()
                    }) {
                        Text(text = "Finish")
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryImagePicker(onImageSelected: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                onImageSelected(it)
            }
        }
    )
    Button(onClick = { launcher.launch("image/*") }) {
        Text("Select Image")
    }
}