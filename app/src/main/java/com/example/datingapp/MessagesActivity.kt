package com.example.datingapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.user.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MessagesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatingAppTheme {
                MessagingApp()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingApp() {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    var messageText by remember { mutableStateOf(TextFieldValue()) }

    val messages = remember { mutableStateListOf<Message>() }

//    db.collection("messages").document(currentUser!!.uid)
//        .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//            if (firebaseFirestoreException != null) {
//                return@addSnapshotListener
//            }
//            querySnapshot?.let { snapshot ->
//                messages.clear()
//                snapshot.documents.forEach { document ->
//                    val message = document.toObject(Message::class.java)
//                    message?.let {
//                        messages.add(it)
//                    }
//                }
//            }
//        }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Messaging App") }) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(messages) { message ->
                    Text("${message.senderId}: ${message.text}")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    label = { Text("Message") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        val timestamp = System.currentTimeMillis()
                        val message = Message(messageText.text, currentUser?.uid ?: "", timestamp)
//                        db.push().setValue(message)
                        messageText = TextFieldValue()
                    }
                ) {
                    Text("Send")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DatingAppTheme {
    }
}