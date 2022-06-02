package ru.debajo.reduktor.sample.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCurrentWeather(
    @SerialName("main")
    val main: Main,
) {
    @Serializable
    data class Main(
        @SerialName("temp")
        val temp: Float,

        @SerialName("feels_like")
        val feelsLike: Float,
    )
}
