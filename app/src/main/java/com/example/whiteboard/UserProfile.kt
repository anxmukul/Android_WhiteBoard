package com.example.whiteboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun UserProfile(modifier: Modifier = Modifier, user: FirebaseUser) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_mars_background),
                contentDescription = "Some background image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.photoUrl).crossfade(true).build(),
                contentDescription = "User Profile Image",
                modifier = Modifier
                    .padding(8.dp)
                    .size(150.dp)
                    .clip(
                        CircleShape
                    ),
                contentScale = ContentScale.Crop,
            )
            Text(text = "${user.displayName}", fontSize = 18.sp)
            Text(text = "${user.email}")
        }
        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
        }, modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 8.dp)) {
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