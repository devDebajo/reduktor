package ru.debajo.reduktor.sample.ui.main

import ru.debajo.reduktor.ReduktorViewModel
import ru.debajo.reduktor.reduktorStore
import ru.debajo.reduktor.sample.domain.command.LoadCurrentWeatherCommandProcessor
import ru.debajo.reduktor.sample.domain.command.LoadLocationCommandProcessor
import ru.debajo.reduktor.sample.ui.main.model.Event
import ru.debajo.reduktor.sample.ui.main.model.News
import ru.debajo.reduktor.sample.ui.main.model.State

class MainViewModel(
    weatherProcessor: LoadCurrentWeatherCommandProcessor,
    locationProcessor: LoadLocationCommandProcessor,
) : ReduktorViewModel<State, Event, News>(
    store = reduktorStore(
        initialState = State.NoLocation,
        eventReduktor = MainReduktor.EventReduktor,
        commandResultReduktor = MainReduktor.CommandResultReduktor,
        commandProcessors = listOf(weatherProcessor, locationProcessor),
    )
)
