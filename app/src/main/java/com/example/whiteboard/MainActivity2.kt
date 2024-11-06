package com.example.whiteboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.whiteboard.ui.theme.WhiteBoardTheme

class MainActivity2 : ComponentActivity() {
    val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhiteBoardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val data by viewModel.data

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Row {
                                Surprise(name = "Image from Mangalyaan")
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data("https://cdn.pixabay.com/photo/2022/06/02/02/24/india-map-7236918_1280.jpg")
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Indian Flag",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                        .size(20.dp)
                                )
                            }
                        }
                        items(data) { singleApiResponse ->
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(singleApiResponse.imgSrc)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Image from URL",
                                modifier = Modifier
                                    .height(250.dp).fillMaxWidth()
                                    .padding(vertical = 10.dp, horizontal = 5.dp),
                                contentScale = ContentScale.Crop
                            )

                        }
                    }
                }


            }
        }
    }
}

@Composable
fun Surprise(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "$name",
        modifier = modifier,
        color = Color(0xFFCC7722)
    )
}

@Preview(showBackground = true)
@Composable
fun SurprisePreview() {
    WhiteBoardTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surprise("Sale")
        }
    }
}