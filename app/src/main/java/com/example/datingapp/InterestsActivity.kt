package com.example.datingapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.ui.theme.whiteColor
import com.example.datingapp.user.Interest
import com.example.datingapp.user.UserController
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
            val mapOfInterests = arrayListOf<String>()
            Interest.values().forEach {
                mapOfInterests.add(it.name)
            }
            DatingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = backgroundColor
                ) {
                    Column(modifier = Modifier
                        .padding(10.dp)
                        .background(whiteColor)) {
                        createRowsForEveryThreeElements(mapOfInterests)
                        Button(onClick = {
                            val intent = Intent(context, PhotoActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        }, modifier = Modifier.padding(16.dp)) {
                            Text(text = "Choose your photo ->")
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun createRowsForEveryThreeElements(elements: ArrayList<String>) {
    var i = 0
    Column(modifier = Modifier.padding(16.dp)) {
        while (i < elements.size) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                for (j in i until i + 3) {
                    if (j < elements.size) {
                        OutlinedButton(onClick = { /* Do something */ }) {
                            Text(text = elements[j], maxLines = 1,
                                overflow = TextOverflow.Clip, fontSize = 12.sp)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(38.dp))
                    }
                }
            }
            i += 3
        }
    }
}