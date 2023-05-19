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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datingapp.R
import com.example.datingapp.connection.InternetCheckService
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.ui.theme.Shapes
import com.example.datingapp.ui.theme.Typography
import com.example.datingapp.ui.theme.additionalColor
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.ui.theme.otherUserChatColor
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

    private lateinit var mainImage: Uri

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
        val clickableInterests = remember { mutableStateOf(true) }
        Column(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth()
                .padding(10.dp)
                .clip(Shapes.large)
                .background(color = whiteColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
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
                                        if (clickableInterests.value) {
                                            clicked = if (!clicked) {
                                                chosenInterests.add(mapOfInterests[j])
                                                true
                                            } else {
                                                chosenInterests.remove(mapOfInterests[j])
                                                false
                                            }
                                        }
                                    },
                                    border = BorderStroke(
                                        1.dp,
                                        if (clicked) additionalColor else otherUserChatColor
                                    ),
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
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = {
                            Text(
                                text = "UserName",
                                style = Typography.body1
                            )
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(onClick = {
                        imageChosen.value = true
                        val galleryIntent = Intent(Intent.ACTION_PICK)
                        galleryIntent.type = "image/*"
                        imagePickerActivityResult.launch(galleryIntent)
                    }) {
                        Text(text = "Choose photo", style = Typography.button)
                    }

                    Button(onClick = {
                        if (!imageChosen.value) {
                            toastImageNotChosen()
                        }
                        userControllerImpl.uploadToDatabase(
                            userName,
                            chosenInterests,
                            mainImage
                        )
                        val intent =
                            Intent(applicationContext, VideoActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        applicationContext.startActivity(intent)
                    }) {
                        Text(text = "Finish", style = Typography.button)
                    }
                }
            }
        }
        if (chosenInterests.size >= 5) {
            clickableInterests.value = false
            Toast.makeText(context, "You can choose only 5 interests", Toast.LENGTH_SHORT).show()
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
                toastImageNotChosen()
            }
        }

    private fun toastImageNotChosen() {
        Toast.makeText(context, "There will be standard image!", Toast.LENGTH_SHORT)
            .show()
    }
}