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
package io.github.castrokingjames.feature.dashboard.history

import androidx.compose.foundation.layout.Box
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
import coil3.compose.AsyncImage
import io.github.castrokingjames.gray
import io.github.castrokingjames.model.Weather
import io.github.castrokingjames.ui.UiState
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@Composable
fun HistoryView(
  modifier: Modifier = Modifier,
  historyUiState: UiState<List<Weather>>,
) {
  var history: List<Weather> = emptyList()
  if (historyUiState is UiState.Success) {
    history = historyUiState.data
  }

  val size = history.size
  LazyColumn(
    modifier = Modifier.padding(
      start = 16.dp,
      end = 16.dp,
    ),
  ) {
    items(
      count = size,
      key = { index ->
        history[index].id
      },
      contentType = { index ->
        history[index].javaClass
      },
    ) { index ->
      val weather = history[index]
      val ago = timeAgo(weather.id)
      val description = weather.description.replaceFirstChar { it.uppercase() }
      val icon = "https://openweathermap.org/img/wn/${weather.icon}@4x.png"
      val real = "${weather.temperature.real.roundToInt()}°"
      val feels = "${weather.temperature.feels.roundToInt()}°"

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp, bottom = 8.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
        ) {
          Column(
            modifier = Modifier.weight(0.8f),
          ) {
            Box(
              modifier = Modifier.fillMaxWidth(),
            ) {
              Text(
                text = ago,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterStart),
              )
              Text(
                text = real,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterEnd),
              )
            }
            Spacer(
              modifier = Modifier.size(4.dp),
            )
            Box(
              modifier = Modifier.fillMaxWidth(),
            ) {
              Text(
                text = description,
                color = gray,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterStart),
              )
              Text(
                text = feels,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterEnd),
              )
            }
          }
          Box(
            modifier = Modifier.weight(0.2f),
          ) {
            AsyncImage(
              model = icon,
              contentDescription = null,
              modifier = Modifier
                .size(44.dp)
                .align(Alignment.Center),
            )
          }
        }
      }
    }
  }
}

fun timeAgo(timestamp: Long): String {
  val now = System.currentTimeMillis()
  val diff = now - timestamp // Difference in milliseconds

  return when {
    diff == 0L -> "Now"
    diff < TimeUnit.MINUTES.toMillis(1) -> "${diff / 1000} seconds ago"
    diff < TimeUnit.HOURS.toMillis(1) -> "${diff / TimeUnit.MINUTES.toMillis(1)} minutes ago"
    diff < TimeUnit.DAYS.toMillis(1) -> "${diff / TimeUnit.HOURS.toMillis(1)} hours ago"
    else -> "${diff / TimeUnit.DAYS.toMillis(1)} days ago"
  }
}
