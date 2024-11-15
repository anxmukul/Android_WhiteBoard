package com.example.whiteboard.data

import com.google.gson.annotations.SerializedName

data class ApiResponse2(

    @SerializedName("photos") var photos: ArrayList<Photos> = arrayListOf()

)

data class Photos(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("sol") var sol: Int? = null,
    @SerializedName("img_src") var imgSrc: String? = null,
    @SerializedName("earth_date") var earthDate: String? = null,
)