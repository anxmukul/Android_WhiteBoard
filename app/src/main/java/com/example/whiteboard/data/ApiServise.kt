package com.example.whiteboard.data

import retrofit2.http.GET

interface ApiService {
    @GET("/photos")
    suspend fun getData(): List<ApiResponse>

    @GET("/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=XRpLP54ghipbAhSPNdT01yHMEfgrNMcZPJnEg206")
    suspend fun getMarsData(): ApiResponse2
}