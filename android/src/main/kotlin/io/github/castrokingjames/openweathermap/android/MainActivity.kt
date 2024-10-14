/*
 * Copyright 2024, King James Castro and project contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.castrokingjames.openweathermap.android

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.windowsizeclass.LocalWindowSizeClassProvider
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.content.ContextCompat
import com.arkivanov.decompose.retainedComponent
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import io.github.castrokingjames.AppTheme
import io.github.castrokingjames.data.manager.LocationProvider
import io.github.castrokingjames.ui.App
import io.github.castrokingjames.ui.AppComponent
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {

  private val provider by inject<LocationProvider>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val component = retainedComponent { componentContext ->
      val component by inject<AppComponent> {
        parametersOf(componentContext)
      }
      component
    }

    val resolutionLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
      if (result.resultCode == RESULT_OK) {
        getLocation()
      } else {
        Toast.makeText(this, "Location services required", Toast.LENGTH_SHORT).show()
      }
    }

    val locationPermissionLauncher = registerForActivityResult(
      ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
      if (isGranted) {
        requestLocationSettings(resolutionLauncher)
      } else {
        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
      }
    }

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      getLastLocation()
      requestLocationSettings(resolutionLauncher)
    } else {
      provider.post(null)
      locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    setContent {
      val color = android.graphics.Color.parseColor("#FFE1D3FA")
      enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.light(color, color),
      )
      val windowSizeClass: WindowSizeClass = calculateWindowSizeClass(this)
      CompositionLocalProvider(LocalWindowSizeClassProvider provides windowSizeClass) {
        AppTheme {
          App(
            component = component,
          )
        }
      }
    }
  }

  private fun requestLocationSettings(resolutionLauncher: ActivityResultLauncher<IntentSenderRequest>) {
    val locationRequest = LocationRequest
      .create()
      .apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 10000L
        fastestInterval = 5000L
      }

    val builder = LocationSettingsRequest.Builder()
      .addLocationRequest(locationRequest)
      .setAlwaysShow(true)

    val settingsClient = LocationServices.getSettingsClient(this)
    val task = settingsClient.checkLocationSettings(builder.build())

    task
      .addOnSuccessListener {
        getLocation()
      }
      .addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
          try {
            val intentSenderRequest = IntentSenderRequest
              .Builder(exception.resolution)
              .build()
            resolutionLauncher.launch(intentSenderRequest)
          } catch (sendEx: IntentSender.SendIntentException) {
            Toast
              .makeText(this, "Failed to resolve settings", Toast.LENGTH_SHORT)
              .show()
          }
        }
      }
  }

  private fun getLocation() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      val fuse = LocationServices.getFusedLocationProviderClient(this)
      val locationRequest = LocationRequest
        .create()
        .apply {
          priority = LocationRequest.PRIORITY_HIGH_ACCURACY
          interval = 10000L
          fastestInterval = 5000L
        }
      val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
          val location = result.locations.first()
          provider.post(location)
          fuse.removeLocationUpdates(this)
        }
      }
      fuse
        .requestLocationUpdates(locationRequest, locationCallback, null)
        .addOnFailureListener {
          Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
        }
    }
  }

  private fun getLastLocation() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      LocationServices
        .getFusedLocationProviderClient(this)
        .lastLocation
        .addOnSuccessListener { location ->
          provider.post(location)
        }
        .addOnFailureListener {
          Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
        }
    }
  }
}
