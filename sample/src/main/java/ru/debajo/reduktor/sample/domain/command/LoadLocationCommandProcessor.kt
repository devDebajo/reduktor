package ru.debajo.reduktor.sample.domain.command

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.debajo.reduktor.Command
import ru.debajo.reduktor.CommandProcessor
import ru.debajo.reduktor.CommandResult

@OptIn(ExperimentalCoroutinesApi::class)
class LoadLocationCommandProcessor(
    private val context: Context
) : CommandProcessor {

    private val client: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(context) }

    @SuppressLint("MissingPermission")
    override fun invoke(commands: Flow<Command>): Flow<CommandResult> {
        return commands
            .filterIsInstance<GetLocation>()
            .filter { isLocationAllowed() }
            .mapLatest { runCatching { getCurrentLocation() }.getOrNull() }
            .filterNotNull()
            .map { LocationGot(it.first, it.second) }
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private suspend fun getCurrentLocation(): Pair<Double, Double> {
        return suspendCancellableCoroutine { emitter ->
            val callback: LocationCallback = createListener { self, location ->
                if (emitter.isActive) {
                    emitter.resume(location, null)
                    client.removeLocationUpdates(self)
                }
            }

            client.requestLocationUpdates(
                createRequest(),
                callback,
                Looper.getMainLooper()
            )

            emitter.invokeOnCancellation { client.removeLocationUpdates(callback) }
        }
    }

    private fun createListener(onNextLocation: (LocationCallback, Pair<Double, Double>) -> Unit): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(location: LocationResult) {
                onNextLocation(this, location.lastLocation.latitude to location.lastLocation.longitude)
            }
        }
    }

    private fun createRequest(): LocationRequest {
        return LocationRequest.create()
            .setInterval(5000)
            .setFastestInterval(3000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }

    private fun isLocationAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    object GetLocation : Command

    class LocationGot(val latitude: Double, val longitude: Double) : CommandResult
}
