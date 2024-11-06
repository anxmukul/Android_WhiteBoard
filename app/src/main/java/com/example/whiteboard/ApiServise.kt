package com.example.whiteboard

import retrofit2.http.GET
import retrofit2.Call

interface ApiService {
    @GET("/photos")
    suspend fun getData(): List<ApiResponse>
}