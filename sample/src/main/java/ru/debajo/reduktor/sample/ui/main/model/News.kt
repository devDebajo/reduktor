package ru.debajo.reduktor.sample.ui.main.model

sealed interface News {
    class Error(val message: String) : News
}
