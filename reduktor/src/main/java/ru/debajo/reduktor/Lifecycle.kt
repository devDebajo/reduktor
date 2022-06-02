package ru.debajo.reduktor

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
inline fun <reified VM : ViewModel> ComponentActivity.lazyViewModel(crossinline builder: () -> VM): Lazy<VM> {
    return viewModels(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = builder() as T
        }
    })
}

@MainThread
fun <T> Flow<T>.collect(scope: CoroutineScope, @MainThread collector: (T) -> Unit) {
    scope.launch(Dispatchers.Main) { this@collect.collect(collector) }
}
