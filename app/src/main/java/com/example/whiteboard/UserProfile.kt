package com.example.whiteboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun UserProfile(modifier: Modifier = Modifier, user: FirebaseUser) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.ic_mars_background),
            contentDescription = "Some background image"
        )
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.photoUrl).crossfade(true).build(),
            contentDescription = "User Profile Image",
            modifier = Modifier
                .size(200.dp)
                .clip(
                    CircleShape
                ),
            contentScale = ContentScale.Crop,
        )
        Text(text = "${user.displayName}")
        Text(text = "${user.email}")
        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
        }) {
            Text("Sign Out")
        }
    }
}

@Preview
@Composable
private fun UserProfilePreview() {
    val user = FirebaseAuth.getInstance().currentUser
    if (user != null) {
        UserProfile(user = user)
    }
}