package com.example.datingapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.ui.theme.whiteColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = backgroundColor
                ) {
                    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

                    Column {
                        GalleryImagePicker(onImageSelected = { uri ->
                            selectedImageUri = uri
                        })

                        if (selectedImageUri != null) {
                            Image(
                                painter = rememberImagePainter(selectedImageUri),
                                contentDescription = "Selected Image"
                            )
                        }
                    }
//                    Column(modifier = Modifier
//                        .padding(10.dp)
//                        .background(whiteColor)) {
//
//                    }
                }
            }
        }
    }
}

@Composable
fun GalleryImagePicker(onImageSelected: (Uri) -> Unit) {
    val context = LocalContext.current
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