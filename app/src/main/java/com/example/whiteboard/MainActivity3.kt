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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val user = FirebaseAuth.getInstance().currentUser
//        Column {
//            user?.let {
//                Text("Hello, ${it.displayName}")
//                Text("Email: ${it.email}")
//                Button(onClick = {
//                    FirebaseAuth.getInstance().signOut()
//                }) {
//                    Text("Sign out")
//                }
//            }
//        }
//    context.startActivity(
//        Intent(
//            context,
//            MainActivity::class.java
//        )
//    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GoogleSignInButton(context) { idToken ->
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
fun GoogleSignInButton(context: Context, onSignInSuccess: (String) -> Unit) {
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

    Button(
        onClick = { launcher.launch(googleSignInClient.signInIntent) },
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Sign in with Google")
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


//fun funcA (doSomething: (Int, String) -> Unit) : Unit {
//    Log.i("TAG", "Calling doSomething func")
//    doSomething(10, "Hare Ram")
//}
//
//fun funcB () : Unit {
//    funcA() { doSomethingInt, doSomethingString ->
//        Log.i("TAG", "got $doSomethingInt, $doSomethingString")
//    }
//}



