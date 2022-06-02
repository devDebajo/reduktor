package ru.debajo.reduktor.sample.di

import android.annotation.SuppressLint
import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import ru.debajo.reduktor.sample.BuildConfig
import ru.debajo.reduktor.sample.data.WeatherService
import ru.debajo.reduktor.sample.domain.command.LoadCurrentWeatherCommandProcessor
import ru.debajo.reduktor.sample.domain.command.LoadLocationCommandProcessor
import ru.debajo.reduktor.sample.ui.main.MainViewModel

class Di(private val context: Context) {

    @Suppress("JSON_FORMAT_REDUNDANT")
    @OptIn(ExperimentalSerializationApi::class)
    private fun provideWeatherService(): WeatherService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.OPEN_WEATHER_MAP_HOST)
            .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(MediaType.get("application/json")))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(WeatherService::class.java)
    }

    private fun provideLoadCurrentWeatherCommandProcessor(service: WeatherService = provideWeatherService()): LoadCurrentWeatherCommandProcessor {
        return LoadCurrentWeatherCommandProcessor(service)
    }

    private fun provideLoadLocationCommandProcessor(): LoadLocationCommandProcessor {
        return LoadLocationCommandProcessor(context)
    }

    fun provideMainViewModel(
        weatherProcessor: LoadCurrentWeatherCommandProcessor = provideLoadCurrentWeatherCommandProcessor(),
        locationProcessor: LoadLocationCommandProcessor = provideLoadLocationCommandProcessor(),
    ): MainViewModel {
        return MainViewModel(weatherProcessor, locationProcessor)
    }

    companion object Di {

        @SuppressLint("StaticFieldLeak")
        lateinit var instance: ru.debajo.reduktor.sample.di.Di
            private set

        fun init(context: Context) {
            instance = Di(context.applicationContext)
        }
    }
}
