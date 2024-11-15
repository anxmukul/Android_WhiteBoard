package com.example.whiteboard

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.whiteboard.data.MarsFact
import com.example.whiteboard.data.mockMarsFact
import com.example.whiteboard.ui.theme.WhiteBoardTheme
import com.example.whiteboard.ui.theme.customFontFamily
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhiteBoardTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                var currentRoute by remember { mutableStateOf("home") }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Black),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        BottomNavigationBar(
                            items = bottomNavItems,
                            currentRoute = currentRoute,
                            onItemSelected = { selectedItem ->
                                currentRoute = selectedItem.route
                            }
                        )
                    }

                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        when (currentRoute) {
                            "home" -> HomeScreen(onShowMarsImages = {
                                currentRoute = "search"
                            })

                            "search" -> {
                                MarsImage()
                            }

                            "profile" -> {}
                        }
                    }

                }

            }
        }
    }
}

@Composable
fun MarsText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = "$text!",
        modifier = modifier,
        color = Color.Yellow,
        textAlign = TextAlign.Center,
        style = TextStyle(fontFamily = customFontFamily, fontSize = 10.sp)
    )
}

@Composable
fun Greeting(text: String, modifier: Modifier = Modifier) {
    Text(
        text = "$text!",
        modifier = modifier,
        color = Color.Yellow
    )
}

@Composable
fun HomeScreen(onShowMarsImages: () -> Unit) {
    val viewModel = hiltViewModel<MainActivity2ViewModel>()
    val data by viewModel.data
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_mars_home),
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
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MarsText(
                    text = "Mars is the fourth planet from the Sun in our solar system, often called the \"Red Planet\" due to its reddish appearance, which is caused by iron oxide (rust) on its surface. It has a thin atmosphere, primarily composed of carbon dioxide, and experiences extreme temperature variations, ranging from about -125°C (-195°F) at the poles to 20°C (68°F) at the equator during the summer months.\n" +
                            "\n" +
                            "Mars is about half the size of Earth, with a day length of 24.6 hours and a year that lasts 687 Earth days. T",
                    modifier = Modifier
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Log.e("TAG", "data: $data")
            data?.let { it ->
                MarsFactCard(it)
            }

        }
//        Row(
//            modifier = Modifier
//                .fillMaxSize()
//                .weight(0.3f)
//                .padding(16.dp)
//                .clickable {
//                    onShowMarsImages()
//                },
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Greeting("Show Mars Images")
//        }
    }
}

@Composable
fun MarsFactCard(data: MarsFact, modifier: Modifier = Modifier) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black)
            .padding(horizontal = 6.dp)
    ) {
        Text(
            text = "Mars Fact",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black,
                contentColor = Color(0xFFb69a6d)
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Column {
                        Text(
                            text = "No of Moons",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        ) // title
                        Text(
                            text = "${data.moons?.size}",
                            color = Color(0xFF908977),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )      // value
                    }
                    Column {
                        Text(
                            text = "Mass",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        ) // title
                        Text(
                            text = "${"%.2f".format(data.mass?.massValue ?: 0.0)} x 10^23 kg",
                            color = Color(0xFF908977),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )      // value
                    }
                    Column {
                        Text(
                            text = "Gravity",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        ) // title
                        Text(
                            text = "${data?.gravity} m/s²",
                            color = Color(0xFF908977),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )      // value
                    }
                }
                Column {
                    Column {
                        Text(
                            text = "Escape Velocity",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        ) // title
                        Text(
                            text = "${(data?.escape ?: 0) / 1000} x 10³ m/s",
                            color = Color(0xFF908977),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )      // value
                    }
                    Column {
                        Text(
                            text = "Mean Radius",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        ) // title
                        Text(
                            text = "${data?.meanRadius} km",
                            color = Color(0xFF908977),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )      // value
                    }
                    Column {
                        Text(
                            text = "AvgTemp",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        ) // title
                        Text(
                            text = "${data?.avgTemp} °C",
                            color = Color(0xFF908977),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )      // value
                    }
                }

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun MarsFactCardPreview() {
    val mockData = mockMarsFact()
    MarsFactCard(mockData)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WhiteBoardTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.weight(0.8f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_mars_home),
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
                    .weight(0.7f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    MarsText(
                        text = "Mars is the fourth planet from the Sun in our solar system, often called the \"Red Planet\" due to its reddish appearance, which is caused by iron oxide (rust) on its surface. It has a thin atmosphere, primarily composed of carbon dioxide, and experiences extreme temperature variations, ranging from about -125°C (-195°F) at the poles to 20°C (68°F) at the equator during the summer months.\n" +
                                "\n" +
                                "Mars is about half the size of Earth, with a day length of 24.6 hours and a year that lasts 687 Earth days. T",
                        modifier = Modifier
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.3f)
                    .padding(16.dp)
                    .clickable {},
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Greeting("Show Mars Images")
            }
        }
    }
}