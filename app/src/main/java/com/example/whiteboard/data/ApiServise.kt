package com.example.whiteboard.data

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/photos")
    suspend fun getData(): List<ApiResponse>

    @GET("/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=XRpLP54ghipbAhSPNdT01yHMEfgrNMcZPJnEg206")
    suspend fun getMarsData(): ApiResponse2

    @GET("https://api.le-systeme-solaire.net/rest/bodies/mars")
    suspend fun getMarsFact(): MarsFact

    @POST("https://generativelanguage.googleapis.com/v1beta/models/{modelName}:generateContent")
    suspend fun generateText(
        @Path("modelName") modelName: String = "gemini-1.5-flash-latest",
        @Query("key") key: String = "AIzaSyD21cu8hdt4vnn2JKGjtWKdFmtA82BOnc8",
        @Body requestBody: RequestBody,
    ): AiResponseDto

}

data class RequestBody(val contents: List<Content>, val systemInstruction: SystemInstruction)

data class SystemInstruction(
    val role: String,
    val parts: List<TextPart>,
)

data class Content(
    val role: String,
    val parts: List<TextPart>,
)

data class TextPart(val text: String)

data class AiResponseDto(
    @SerializedName("candidates") var candidates: ArrayList<CandidatesDto> = arrayListOf(),
    // @SerializedName("usageMetadata") var usageMetadata: UsageMetadata? = UsageMetadata(),
    // @SerializedName("modelVersion") var modelVersion: String? = null,
)

data class ContentDto(
    @SerializedName("parts") var parts: ArrayList<PartsDto> = arrayListOf(),
    @SerializedName("role") var role: String? = null,
)

data class PartsDto(
    @SerializedName("text") var text: String? = null,
)

data class CandidatesDto(
    @SerializedName("content") var content: ContentDto? = ContentDto(),
    @SerializedName("finishReason") var finishReason: String? = null,
    @SerializedName("avgLogprobs") var avgLogprobs: Double? = null,
)

