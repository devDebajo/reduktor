package ru.debajo.reduktor

/**
 * Decision point how to change state, run side effect and one time news by input UI event and current state
 */
typealias Reduktor<State, Event, News> = (state: State, event: Event) -> Akt<State, News>
