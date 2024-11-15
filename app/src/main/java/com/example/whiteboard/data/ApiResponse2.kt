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


data class MarsFact(
    @SerializedName("moons") var moons: ArrayList<Moons> = arrayListOf(),
    @SerializedName("mass") var mass: Mass? = Mass(),
    @SerializedName("density") var density: Double? = null,
    @SerializedName("gravity") var gravity: Double? = null,
    @SerializedName("escape") var escape: Int? = null,
    @SerializedName("meanRadius") var meanRadius: Double? = null,
    @SerializedName("avgTemp") var avgTemp: Int? = null,

    )

data class Mass(

    @SerializedName("massValue") var massValue: Double? = null,
    @SerializedName("massExponent") var massExponent: Int? = null

)

data class Moons(

    @SerializedName("moon") var moon: String? = null,
    @SerializedName("rel") var rel: String? = null

)

fun mockMarsFact(): MarsFact {
    return MarsFact(
        moons = arrayListOf(
            Moons(moon = "Phobos", rel = "https://en.wikipedia.org/wiki/Phobos_(moon)"),
            Moons(moon = "Deimos", rel = "https://en.wikipedia.org/wiki/Deimos_(moon)")
        ),
        mass = Mass(massValue = 6.4171, massExponent = 23), // 6.4171 × 10^23 kg
        density = 3.93, // g/cm³
        gravity = 3.721, // m/s²
        escape = 5000, // 5.0 km/s
        meanRadius = 3389.5, // in km
        avgTemp = -63 // in °C
    )
}