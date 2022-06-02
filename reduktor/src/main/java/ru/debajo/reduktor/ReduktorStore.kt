package ru.debajo.reduktor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Central component through which all events pass in Reduktor
 *
 * State - state of your screen
 * Event - UI events
 * News - One time events on UI
 */
interface ReduktorStore<State : Any, Event : Any, News : Any> {

    /**
     * Current state of store
     */
    val currentState: State

    /**
     * Coroutines [StateFlow] of store State
     */
    val state: StateFlow<State>

    /**
     * Coroutines hot [Flow] of one time events
     */
    val news: Flow<News>

    /**
     * Function through which events are transmitted from UI
     */
    fun onEvent(event: Event)

    /**
     * Disposing of all processes in store
     */
    fun dispose()
}
