package ru.debajo.reduktor.sample.data

import retrofit2.http.GET
import retrofit2.http.Query
import ru.debajo.reduktor.sample.data.model.RemoteCurrentWeather

interface WeatherService {
    @GET("data/2.5/weather?units=metric&lang=en")
    suspend fun getCurrentWeather(
        @Query("lat")
        latitude: Double,
        @Query("lon")
        longitude: Double,
        @Query("appid")
        apiKey: String,
    ): RemoteCurrentWeather
}
