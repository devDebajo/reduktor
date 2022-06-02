package ru.debajo.reduktor.sample.ui.main

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import ru.debajo.reduktor.collect
import ru.debajo.reduktor.lazyViewModel
import ru.debajo.reduktor.sample.R
import ru.debajo.reduktor.sample.di.Di
import ru.debajo.reduktor.sample.ui.main.model.Event
import ru.debajo.reduktor.sample.ui.main.model.News
import ru.debajo.reduktor.sample.ui.main.model.State

@OptIn(ExperimentalPermissionsApi::class)
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazyViewModel { Di.instance.provideMainViewModel() }
    private var currentToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Di.init(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.news.collect(lifecycleScope, ::onNews)

        setContent {
            val permissionState = rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )
            LaunchedEffect(key1 = permissionState, block = {
                permissionState.launchMultiplePermissionRequest()
            })
            CompositionLocalProvider(
                LocalContentColor provides if (isSystemInDarkTheme()) Color.White else Color.Black
            ) {
                MaterialTheme {
                    PermissionsRequired(
                        multiplePermissionsState = permissionState,
                        permissionsNotGrantedContent = { Text("No location permission") },
                        permissionsNotAvailableContent = { Text("No location available") },
                        content = { Content() }
                    )
                }
            }
        }
    }

    @Composable
    private fun Content() {
        LaunchedEffect(key1 = viewModel, block = {
            viewModel.onEvent(Event.LoadCity)
        })
        val state by viewModel.state.collectAsState()
        state.let {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when (it) {
                    is State.Data -> {
                        Column {
                            Text("Temp: ${it.temp}")
                            Text("Feels like: ${it.feelsLike}")
                            Button(onClick = { viewModel.onEvent(Event.Refresh) }) {
                                Text("Refresh")
                            }
                        }
                    }
                    is State.LoadingWeather -> Text("Loading")
                    is State.NoLocation -> Text("No location")
                }
            }
        }
    }

    private fun onNews(news: News) {
        when (news) {
            is News.Error -> {
                currentToast?.cancel()
                currentToast = Toast.makeText(this, news.message, Toast.LENGTH_SHORT).apply { show() }
            }
        }
    }
}
