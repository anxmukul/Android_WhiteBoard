package com.example.whiteboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.whiteboard.ui.theme.WhiteBoardTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = hiltViewModel<MainActivity3ViewModel>()
            val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()

            WhiteBoardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (isLoggedIn) {
                        true -> {
                            startActivity(
                                Intent(
                                    this@MainActivity3,
                                    MainActivity::class.java
                                )
                            )
                            this.finish()
                        }

                        false -> {
                            StartAuthentication(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding), onSignInSuccess = {
                                    viewModel.saveIsSignedIn()
                                    this.finish()
                                }
                            )
                        }

                        null -> {
                            StartAuthentication(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding), onSignInSuccess = {
                                    viewModel.saveIsSignedIn()
                                    this.finish()
                                }
                            )
                        }
                    }


                }
            }
        }
    }
}

@Composable
fun StartAuthentication(modifier: Modifier = Modifier, onSignInSuccess: () -> Unit) {
    var context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Carousel(
                listOf(
                    R.drawable.ic_login_background,
                    R.drawable.ic_login_background_2,
                    R.drawable.ic_login_background_3
                )
            )
            Text(
                text = "Welcome to the Red \n Planet",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Text(
                text = "Explore images from the Mars Rover and learn \n about the Red Planet",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
            )
        }
        GoogleSignInButton(
            context = context,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        ) { idToken ->
            firebaseAuthWithGoogle(idToken) { success ->
                if (success) {
                    onSignInSuccess()
                    context.startActivity(
                        Intent(
                            context,
                            MainActivity::class.java
                        )
                    )

                }

            }
        }
    }
}

@Composable
fun Carousel(items: List<Int>) {
    val pagerState = rememberPagerState(0, pageCount = { items.size })
    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % items.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(items[page]),
                    contentDescription = "Some background image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(bottom = 10.dp)
                )
            }
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color(0xFFFAD02C) else Color(0xFFFAD02C).copy(
                        alpha = 0.4f
                    )
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(4.dp)
                )
            }
        }

    }
}


@Composable
fun GoogleSignInButton(
    context: Context,
    modifier: Modifier = Modifier,
    onSignInSuccess: (String) -> Unit
) {
    val googleSignInClient = getGoogleSignInClient(context)

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)

                    // Successfully signed in
                    account?.idToken?.let { idToken ->
                        Log.e("TAG", "GoogleSignInButton, got Id token: , $idToken")
                        onSignInSuccess(idToken) // Pass the ID token to your auth logic
                    } ?: {
                        Log.e("TAG", "GoogleSignInButton: Empty Id token")
                    }
                } catch (e: ApiException) {
                    // Handle error
                    Log.e("TAG", "GoogleSignInButton Exception: $e")
                    e.printStackTrace()
                }
            }
        }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { launcher.launch(googleSignInClient.signInIntent) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Sign in with Google")
        }
        Text(
            text = "By signing in, you agree to our Terms of Service and \n Privacy Policy.",
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 10.sp,
            lineHeight = 12.sp,
        )
        Button(
            onClick = {

            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Skip for now")
        }
    }


}

fun getGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("258662807147-nvou1dbn1g98j4r6r9tmvvt37pdbunh4.apps.googleusercontent.com") // Use Firebase Web Client ID or your Google API client ID
        .requestEmail()
        .build()
    return GoogleSignIn.getClient(context, gso)
}

fun firebaseAuthWithGoogle(idToken: String, onResult: (Boolean) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            onResult(task.isSuccessful)
        }
}


@Preview(showBackground = true)
@Composable
fun StartAuthenticationPreview(modifier: Modifier = Modifier) {

}




