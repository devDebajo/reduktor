package ru.debajo.reduktor.sample.ui.main

import ru.debajo.reduktor.Akt
import ru.debajo.reduktor.CommandResult
import ru.debajo.reduktor.Reduktor
import ru.debajo.reduktor.sample.domain.command.LoadCurrentWeatherCommandProcessor
import ru.debajo.reduktor.sample.domain.command.LoadLocationCommandProcessor
import ru.debajo.reduktor.sample.ui.main.model.Event
import ru.debajo.reduktor.sample.ui.main.model.News
import ru.debajo.reduktor.sample.ui.main.model.State

object MainReduktor {
    object EventReduktor : Reduktor<State, Event, News> {
        override fun invoke(state: State, event: Event): Akt<State, News> {
            return when (event) {
                Event.LoadCity -> reduceLoadCity(state)
                Event.Refresh -> reduceRefresh(state)
            }
        }

        private fun reduceLoadCity(state: State): Akt<State, News> {
            return when (state) {
                is State.NoLocation,
                is State.LoadingWeather -> Akt(commands = listOf(LoadLocationCommandProcessor.GetLocation))

                is State.Data -> {
                    Akt(
                        state = State.LoadingWeather(state.latitude, state.longitude),
                        commands = listOf(LoadCurrentWeatherCommandProcessor.LoadWeather(state.latitude, state.longitude))
                    )
                }
            }
        }

        private fun reduceRefresh(state: State): Akt<State, News> {
            return when (state) {
                is State.NoLocation -> Akt(commands = listOf(LoadLocationCommandProcessor.GetLocation))
                is State.LoadingWeather -> Akt(commands = listOf(LoadCurrentWeatherCommandProcessor.LoadWeather(state.latitude, state.longitude)))
                is State.Data -> {
                    Akt(
                        state = State.LoadingWeather(state.latitude, state.longitude),
                        commands = listOf(LoadCurrentWeatherCommandProcessor.LoadWeather(state.latitude, state.longitude))
                    )
                }
            }
        }
    }

    object CommandResultReduktor : Reduktor<State, CommandResult, News> {
        override fun invoke(state: State, event: CommandResult): Akt<State, News> {
            return when (event) {
                is LoadLocationCommandProcessor.LocationGot -> reduceLocationGot(state, event)
                is LoadCurrentWeatherCommandProcessor.LoadWeatherResult.Loaded -> reduceWeatherLoaded(state, event)
                is LoadCurrentWeatherCommandProcessor.LoadWeatherResult.LoadFailed -> reduceWeatherLoadFailed(state, event)
                else -> Akt()
            }
        }

        private fun reduceLocationGot(
            state: State,
            event: LoadLocationCommandProcessor.LocationGot
        ): Akt<State, News> {
            return when (state) {
                is State.NoLocation -> {
                    Akt(
                        state = State.LoadingWeather(
                            latitude = event.latitude,
                            longitude = event.longitude,
                        ),
                        commands = listOf(LoadCurrentWeatherCommandProcessor.LoadWeather(event.latitude, event.longitude))
                    )
                }
                is State.Data,
                is State.LoadingWeather -> Akt()
            }
        }

        private fun reduceWeatherLoaded(
            state: State,
            event: LoadCurrentWeatherCommandProcessor.LoadWeatherResult.Loaded
        ): Akt<State, News> {
            return when (state) {
                is State.NoLocation,
                is State.Data -> Akt()
                is State.LoadingWeather -> Akt(
                    State.Data(
                        latitude = state.latitude,
                        longitude = state.longitude,
                        temp = event.weather.temp,
                        feelsLike = event.weather.feelsLike,
                    )
                )
            }
        }

        private fun reduceWeatherLoadFailed(
            state: State,
            event: LoadCurrentWeatherCommandProcessor.LoadWeatherResult.LoadFailed
        ): Akt<State, News> {
            return when (state) {
                is State.NoLocation,
                is State.Data -> Akt()
                is State.LoadingWeather -> Akt(
                    state = State.NoLocation,
                    news = listOf(News.Error(event.error.message ?: "Error"))
                )
            }
        }
    }
}
