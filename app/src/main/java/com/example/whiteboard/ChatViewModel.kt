package com.example.whiteboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whiteboard.data.Content
import com.example.whiteboard.data.RequestBody
import com.example.whiteboard.data.RetrofitInstance
import com.example.whiteboard.data.SystemInstruction
import com.example.whiteboard.data.TextPart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {


    private val _data: MutableStateFlow<List<Chat>> =
        MutableStateFlow(
            chat,
        )
    val data: StateFlow<List<Chat>> = _data.asStateFlow()


    fun askAi(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedList = _data.value.toMutableList()
            updatedList.add(
                Chat(
                    message = query,
                    suggestions = emptyList(),
                    isBot = false,
                ),
            )
            updatedList.add(
                Chat(
                    isBot = true,
                    isLoading = true,
                    message = "",
                    suggestions = emptyList(),
                ),
            )
            _data.value = updatedList
            try {
                val requestBody =
                    RequestBody(
                        contents = mapChatsToContents(updatedList),
                        systemInstruction = SystemInstruction(
                            role = "user",
                            parts = listOf(TextPart(createSystemInstructionsV2()))
                        ),
                    )
                val response = RetrofitInstance.api.generateText(requestBody = requestBody)
                Log.e("TAG", "Gemini Response: $response")
                val message = response.candidates[0].content?.parts?.get(0)?.text ?: ""
                val updatedList = _data.value.toMutableList()
                updatedList.removeLastOrNull()
                updatedList.add(
                    Chat(
                        message = message,
                        isBot = true,
                        rawAiResponse = message,
                        suggestions = emptyList()
                    ),
                )
                _data.value = updatedList

            } catch (e: Exception) {

            }


        }
    }


    fun createSystemInstructionsV2(): String {
        val prompt =
            """
                The AI assistant, named Masubo, will answer ONLY questions directly related to Mars and other planets along with the known galaxies and their planets.  Responses must be accurate, concise, easy to understand, and avoid overly technical jargon. Include links to reputable sources (NASA, ESA, peer-reviewed publications) where appropriate. The AI should handle various input types:

                Direct Questions: Examples: "What is the atmosphere of Mars like?", "Compare the surface gravity of Mars and Earth.", "Are there any signs of past life on Mars?", "What are the challenges of sending humans to Mars?", "What is the composition of Martian soil?"

                Comparative Requests: Examples: "Compare the size of Mars to Earth.", "What are the differences between Martian and Earth geology?"

                Requests for Images/Videos: Examples: "Show me a picture of Olympus Mons." (Note: The AI provides the query/URL; the app handles display.)

                General Information Requests: Examples: "Tell me about Mars.", "What are the main features of Mars' surface?"

                Questions about the AI:  If asked "Who are you?", "What are you?", "What's your name?", "Who built you?", "Who created you?", or similar questions about the AI's identity or creation, respond: "I am Masubo, an AI assistant specializing in answering questions about Mars. I was built by Mukul Kumar. You can find his LinkedIn profile here: http://www.linkedin.com/in/anxmukul"

                The AI assistant should access and process data from reputable sources such as:

                NASA websites (e.g., JPL, Mars Exploration Program)
                ESA websites
                Peer-reviewed scientific publications
                Other reliable astronomical databases

                Error Handling:

                Insufficient Information: Respond with: "I'm sorry, I don't have enough information to answer that question. You might find relevant information by searching [suggest a relevant search term or website]."

                Ambiguous/Unclear Request: Respond with: "Could you please rephrase your question?" or "I'm not sure I understand your request. Can you be more specific?
        """.trimIndent()
        return prompt
    }

    fun mapChatsToContents(chats: List<Chat>): List<Content> {
        return chats
            .filterIndexed { index, chat ->
                index != 0 && !chat.isLoading // Ignore the first chat and any chat with isLoading = true
            } // Ignore chats with isLoading = true or empty messages
            .map { chat ->
                val role = if (chat.isBot) "model" else "user"
                val text = if (chat.isBot) chat.rawAiResponse else chat.message
                Content(
                    role = role,
                    parts = listOf(TextPart(text = text)),
                )
            }
    }


}