package ru.debajo.reduktor.sample.ui.main.model

sealed interface State {
    object NoLocation : State

    data class LoadingWeather(
        val latitude: Double,
        val longitude: Double,
    ) : State

    data class Data(
        val latitude: Double,
        val longitude: Double,
        val temp: Float,
        val feelsLike: Float,
    ) : State
}
