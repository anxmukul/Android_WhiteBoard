package com.example.whiteboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.whiteboard.ui.theme.WhiteBoardTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhiteBoardTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
//                    Column (modifier = Modifier.fillMaxSize().background(Color.Yellow), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
//                        Greeting(
//                            name = "People",
//                            modifier = Modifier.padding(innerPadding).background(Color.Magenta)
//                        )
//                    }
//                    LazyColumn (modifier = Modifier.fillMaxSize().background(Color.Yellow), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
//                        items(count = 200){
//                            Greeting( "$it People")
//
//                        }
//                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Yellow),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier.weight(0.5f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.my_photo),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(
                                        CircleShape
                                    ),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.5f)
                                .background(color = Color.Magenta),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column(modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            MainActivity2::class.java
                                        )
                                    )
                                }) {
                                Greeting("Click Me")
                            }
                            Column(modifier = Modifier.padding(16.dp)) {
                                Greeting("Touch Me")
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.5f)
                                .background(color = Color.Green)
                                .clickable {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        snackbarHostState.showSnackbar(message = "Looking Sexy")
                                    }
                                    Log.e(
                                        "TAG",
                                        "GreetingPreview: Clicked",

                                        )
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) { Greeting("Brothers") }

                    }
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WhiteBoardTheme {
//        LazyColumn (modifier = Modifier.fillMaxSize().background(Color.Yellow), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
//            items(count = 200){
//                Greeting("People")
//            }
////            Greeting("People")
////            Greeting("Ladies")
//        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.weight(0.5f), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.my_photo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(
                            CircleShape
                        ),
                    contentScale = ContentScale.Crop
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.5f)
                    .background(color = Color.Magenta),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Greeting("Click Me")
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    Greeting("Touch Me")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.5f)
                    .background(color = Color.Green)
                    .clickable {
                        Log.e(
                            "TAG",
                            "GreetingPreview: Clicked",

                            )
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) { Greeting("Brothers") }

        }
    }
}