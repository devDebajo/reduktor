package ru.debajo.reduktor.sample.domain.command

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import ru.debajo.reduktor.Command
import ru.debajo.reduktor.CommandProcessor
import ru.debajo.reduktor.CommandResult
import ru.debajo.reduktor.sample.BuildConfig
import ru.debajo.reduktor.sample.data.WeatherService
import ru.debajo.reduktor.sample.domain.model.DomainCurrentWeather

class LoadCurrentWeatherCommandProcessor(
    private val service: WeatherService
) : CommandProcessor {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(commands: Flow<Command>): Flow<CommandResult> {
        return commands
            .filterIsInstance<LoadWeather>()
            .mapLatest<LoadWeather, LoadWeatherResult> { (latitude, longitude) ->
                service.getCurrentWeather(latitude, longitude, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                    .let { LoadWeatherResult.Loaded(DomainCurrentWeather(it.main.temp, it.main.feelsLike)) }
            }
            .catch { emit(LoadWeatherResult.LoadFailed(it)) }
    }

    data class LoadWeather(val latitude: Double, val longitude: Double) : Command

    sealed interface LoadWeatherResult : CommandResult {
        class Loaded(val weather: DomainCurrentWeather) : LoadWeatherResult
        class LoadFailed(val error: Throwable) : LoadWeatherResult
    }
}
