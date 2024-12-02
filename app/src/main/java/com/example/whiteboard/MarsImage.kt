package com.example.whiteboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.whiteboard.ui.theme.WhiteBoardTheme


@Composable
fun MarsImage(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<MainActivityViewModel>()
    val data by viewModel.data
    if (data.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(16.dp),
                color = Color(0xFFE76E1D),
                strokeWidth = 2.dp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row {
                    Surprise(name = "Images from Mars Rover, Made in")
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://cdn.pixabay.com/photo/2022/06/02/02/24/india-map-7236918_1280.jpg")
                            .crossfade(true).build(),
                        contentDescription = "Indian Flag",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(20.dp)
                    )
                }
            }
            items(data) { singleApiResponse ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp, brush = Brush.linearGradient(
                                listOf(Color(0xFF00BFFF), Color(0xFFFFA07A))
                            ), shape = RectangleShape
                        )
                        .padding(vertical = 2.dp)
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(singleApiResponse.imgSrc).crossfade(true).build(),
                        contentDescription = "Image from URL",
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                            .padding(vertical = 5.dp, horizontal = 5.dp),
                        contentScale = ContentScale.Crop,
                        loading = {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(16.dp)
                                    .wrapContentSize(),
                                color = Color(0xFFE76E1D),
                                strokeWidth = 2.dp
                            )
                        }
                    )
                }

            }
        }
    }
}

@Composable
fun Surprise(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "$name", modifier = modifier, color = Color(0xFFCC7722)
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