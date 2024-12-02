package com.example.whiteboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<ChatViewModel>()
    val chats by viewModel.data.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope() // For launching scroll operations
    val nestedScrollConnection =
        remember {
            object : NestedScrollConnection {}
        }

    LaunchedEffect(chats.size) {
        coroutineScope.launch {
            if (chats.isNotEmpty()) {
                listState.animateScrollToItem(chats.size - 1)
            }
        }
    }
    Box(
        modifier =
        modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
    ) {
        LazyColumn(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp),
            state = listState,
        ) {
            item {
                Text(
                    text =
                    buildAnnotatedString {
                        append("Masubo")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Ai")
                        }
                        append(" : Ask about Mars")
                    },
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
            items(chats.size) { index ->
                MarsAiContentRow(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    chats[index],
                    onClick = {
                        viewModel.askAi(it)
                    },
                )
            }
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val textField = remember { mutableStateOf("") }
        val interactionSource = remember { MutableInteractionSource() }
        val isFocused by interactionSource.collectIsFocusedAsState()
        OutlinedTextField(
            value = textField.value,
            onValueChange = {
                textField.value = it
            },
            placeholder = {
                Text(text = "Ask Masubo a question")
            },
            modifier =
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            singleLine = true,
            keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            keyboardActions =
            KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    viewModel.askAi(textField.value)
                    textField.value = ""
                },
            ),
            colors =
            TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
        )
    }
}

@Composable
fun MarsAiContentRow(
    modifier: Modifier = Modifier,
    chatItem: Chat,
    onClick: (String) -> Unit,
) {
    val user = FirebaseAuth.getInstance().currentUser
    val successAnimation by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.loading),
    )
    val animationState by animateLottieCompositionAsState(
        composition = successAnimation,
        iterations = LottieConstants.IterateForever,
    )
    Column(modifier = modifier) {
        Row(modifier = Modifier, verticalAlignment = Alignment.Top) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        if (chatItem.isBot) {
                            R.drawable.chatbot
                        } else {
                            user?.photoUrl
                        }
                    ).crossfade(true).build(),
                contentDescription = "User Profile Image",
                modifier = Modifier
                    .size(24.dp)
                    .clip(
                        CircleShape
                    ),
                contentScale = ContentScale.Crop,
            )
            if (chatItem.isLoading) {
                LottieAnimation(
                    composition = successAnimation,
                    modifier =
                    Modifier
                        .height(24.dp)
                        .width((24 * 4).dp),
                    progress = {
                        animationState
                    },
                    contentScale = ContentScale.Crop,
                )
            } else {
                Text(
                    text = chatItem.message,
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
        }
        if (chatItem.isBot) {
            chatItem.suggestions.forEach {
                SuggestionContent(
                    suggestionCategory = it,
                    modifier = Modifier.padding(start = 36.dp, top = 12.dp),
                    onClick = onClick,
                )
            }
        }
    }
}

@Composable
fun SuggestionContent(
    modifier: Modifier = Modifier,
    suggestionCategory: SuggestionCategory,
    onClick: (String) -> Unit,
) {
    Column(modifier = modifier) {
        Text(suggestionCategory.title)
        suggestionCategory.suggestions.forEach {
            SuggestionItem(
                query = it.query,
                modifier = Modifier.padding(vertical = 4.dp),
                onClick = onClick,
            )
        }
    }
}

@Composable
fun SuggestionItem(
    modifier: Modifier = Modifier,
    query: String,
    onClick: (String) -> Unit,
) {
    Label(
        modifier =
        modifier
            .wrapContentSize()
            .padding(0.dp)
            .clickable {
                onClick(query)
            },
        title = {
            Text(
                text = query,
                modifier = Modifier,
            )
        },
        leadingIcon = {},
        colors =
        CardDefaults.cardColors(
            containerColor = Color.Yellow.copy(alpha = 0.3f),
        ),
        paddingValues = PaddingValues(vertical = 4.dp, horizontal = 8.dp),
    )
}

@Composable
fun Label(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(24.dp),
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    paddingValues: PaddingValues = PaddingValues(vertical = 2.dp, horizontal = 6.dp),
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(paddingValues),
        ) {
            leadingIcon?.let { leadingIcon ->
                leadingIcon()
            }
            title?.let { title ->
                title()
            }
        }
    }
}

data class Chat(
    val message: String,
    val suggestions: List<SuggestionCategory>,
    val isBot: Boolean,
    val isLoading: Boolean = false,
    val aiSuggestions: List<Suggestion> = emptyList(),
    val rawAiResponse: String = "",
)


val chat =
    listOf(
        Chat(
            message = "How can I help you today ?",
            suggestions =
            listOf(
                SuggestionCategory(
                    title = "Suggested Questions",
                    suggestions =
                    listOf(
                        Suggestion(query = "Tell me something about Mars."),
                        Suggestion(query = "Why Mars is so popular?"),
                        Suggestion(query = "How to book Ticket to Mars?"),
                    ),
                )
            ),
            isBot = true,
        ),
    )

data class SuggestionCategory(
    val title: String,
    val suggestions: List<Suggestion>,
)

data class Suggestion(
    val query: String,
)


