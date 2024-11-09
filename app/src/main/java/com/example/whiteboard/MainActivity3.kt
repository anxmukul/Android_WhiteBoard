package com.example.whiteboard

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whiteboard.ui.theme.WhiteBoardTheme
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhiteBoardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StartAuthentication(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )

                }
            }
        }
    }
}

@Composable
fun StartAuthentication(modifier: Modifier = Modifier) {
    var context = LocalContext.current
    var isSignedIn by remember { mutableStateOf(false) }
    if (isSignedIn) {
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
        context.startActivity(
            Intent(
                context,
                MainActivity::class.java
            )
        )

    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GoogleSignInButton(context) { idToken ->
                firebaseAuthWithGoogle(idToken) { success ->
                    isSignedIn = success
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
        .requestIdToken("") // Use Firebase Web Client ID or your Google API client ID
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



