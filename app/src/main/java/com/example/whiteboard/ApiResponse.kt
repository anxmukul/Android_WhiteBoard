package com.example.whiteboard

import com.google.gson.annotations.SerializedName


data class ApiResponse(

    @SerializedName("id") var id: String? = null,
    @SerializedName("img_src") var imgSrc: String? = null

)