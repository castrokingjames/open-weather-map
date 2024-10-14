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
package io.github.castrokingjames.feature.dashboard.metrics

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.castrokingjames.Res
import io.github.castrokingjames.ic_humidity_28dp
import io.github.castrokingjames.ic_pressure_28dp
import io.github.castrokingjames.ic_rain_chance_28dp
import io.github.castrokingjames.ic_sunrise_28dp
import io.github.castrokingjames.ic_sunset_28dp
import io.github.castrokingjames.ic_wind_speed_28dp
import io.github.castrokingjames.model.Weather
import io.github.castrokingjames.ui.UiState
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun MetricsView(
  modifier: Modifier = Modifier,
  uiState: UiState<Weather>,
) {
  var windSpeed = ""
  var rainChance = ""
  var pressure = ""
  var humidity = ""
  var sunrise = ""
  var sunset = ""
  if (uiState is UiState.Success) {
    val data = uiState.data
    windSpeed = "${data.windSpeed}km/h"
    rainChance = "${data.cloud.roundToInt()}%"
    pressure = "${data.pressure.roundToInt()} hpa"
    humidity = "${data.humidity.roundToInt()}%"
    val formatter = SimpleDateFormat("hh:mm a")
    sunrise = formatter.format(Date(data.twilight.sunrise * 1000L))
    sunset = formatter.format(Date(data.twilight.sunset * 1000L))
  }
  LazyColumn(
    modifier = modifier
      .fillMaxWidth()
      .padding(
        start = 16.dp,
        end = 16.dp,
      ),
  ) {
    item {
      Row(
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
      ) {
        Metrics(
          modifier = Modifier.weight(0.5f),
          "Wind speed",
          windSpeed,
          Res.drawable.ic_wind_speed_28dp,
        )
        Spacer(
          modifier = Modifier.size(16.dp),
        )
        Metrics(
          modifier = Modifier.weight(0.5f),
          "Rain chance",
          rainChance,
          Res.drawable.ic_rain_chance_28dp,
        )
      }
    }
    item {
      Row(
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
      ) {
        Metrics(
          modifier = Modifier.weight(0.5f),
          "Pressure",
          pressure,
          Res.drawable.ic_pressure_28dp,
        )
        Spacer(
          modifier = Modifier.size(16.dp),
        )
        Metrics(
          modifier = Modifier.weight(0.5f),
          "Humidity",
          humidity,
          Res.drawable.ic_humidity_28dp,
        )
      }
    }
    item {
      Row(
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
      ) {
        Metrics(
          modifier = Modifier.weight(0.5f),
          "Sunrise",
          sunrise,
          Res.drawable.ic_sunrise_28dp,
        )
        Spacer(
          modifier = Modifier.size(16.dp),
        )
        Metrics(
          modifier = Modifier.weight(0.5f),
          "Sunset",
          sunset,
          Res.drawable.ic_sunset_28dp,
        )
      }
    }
  }
}

@Composable
private fun Metrics(
  modifier: Modifier = Modifier,
  caption: String,
  value: String,
  icon: DrawableResource,
) {
  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.secondaryContainer,
    ),
  ) {
    Row(
      modifier = Modifier.padding(
        start = 12.dp,
        top = 8.dp,
        bottom = 8.dp,
        end = 12.dp,
      ),
    ) {
      Image(
        painter = painterResource(icon),
        contentDescription = null,
        modifier = Modifier
          .size(28.dp)
          .align(Alignment.CenterVertically),
      )
      Spacer(
        modifier = Modifier.size(8.dp),
      )
      Column {
        Text(
          text = caption,
          color = MaterialTheme.colorScheme.onSecondaryContainer,
          style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(
          modifier = Modifier.size(8.dp),
        )
        Text(
          text = value,
          color = MaterialTheme.colorScheme.onSecondaryContainer,
          style = MaterialTheme.typography.bodyMedium,
        )
      }
    }
  }
}
