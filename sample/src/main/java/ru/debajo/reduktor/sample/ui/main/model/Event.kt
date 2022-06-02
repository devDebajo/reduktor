package ru.debajo.reduktor.sample.ui.main.model

sealed interface Event {
    object LoadCity : Event
    object Refresh : Event
}
