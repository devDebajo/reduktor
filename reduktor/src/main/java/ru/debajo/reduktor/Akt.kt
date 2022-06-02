package ru.debajo.reduktor

/**
 * Reaction to an any event
 */
data class Akt<State : Any, News : Any>(
    /**
     * New state to update on UI
     */
    val state: State? = null,

    /**
     * List of commands that will run in the corresponding handlers
     */
    val commands: List<Command> = emptyList(),

    /**
     * One time actions, like Toast, Alert Dialog and etc.
     */
    val news: List<News> = emptyList(),
)
