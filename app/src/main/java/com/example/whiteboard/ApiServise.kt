package com.example.whiteboard

import retrofit2.http.GET
import retrofit2.Call

interface ApiService {
    @GET("/photos")
    suspend fun getData(): List<ApiResponse>

    @GET("/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=XRpLP54ghipbAhSPNdT01yHMEfgrNMcZPJnEg206")
    suspend fun getMarsData(): ApiResponse2
}