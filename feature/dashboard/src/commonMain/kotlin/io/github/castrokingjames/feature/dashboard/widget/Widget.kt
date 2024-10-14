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
package io.github.castrokingjames.feature.dashboard.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.castrokingjames.model.Weather
import io.github.castrokingjames.model.getDate
import io.github.castrokingjames.model.getIcon
import io.github.castrokingjames.ui.UiState

@Composable
fun WigetView(
  modifier: Modifier = Modifier,
  uiState: UiState<Weather>,
) {
  var area = ""
  var real = ""
  var icon = ""
  var feels = ""
  var description = ""
  var date = ""

  if (uiState is UiState.Success) {
    val data = uiState.data
    area = "${data.area.city}, ${uiState.data.area.country}"
    real = "${Math.round(data.temperature.real)}°"
    icon = data.getIcon()
    feels = "Feels like ${Math.round(data.temperature.feels)}°"
    description = data.description.replaceFirstChar { it.uppercase() }
    date = data.getDate()
  }
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(0, 0, 8, 8),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primary,
    ),
  ) {
    Box(
      modifier = modifier.padding(16.dp),
    ) {
      Text(
        text = area,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.headlineSmall,
      )
      Row(
        modifier = Modifier.align(Alignment.CenterStart),
      ) {
        Text(
          text = real,
          color = MaterialTheme.colorScheme.onPrimary,
          style = MaterialTheme.typography.displayLarge,
        )
        Text(
          text = feels,
          color = MaterialTheme.colorScheme.onPrimary,
          style = MaterialTheme.typography.bodyLarge,
          modifier = Modifier.align(Alignment.Bottom),
        )
      }

      Column(
        modifier = Modifier.align(Alignment.CenterEnd),
      ) {
        AsyncImage(
          model = icon,
          contentDescription = null,
          modifier = Modifier
            .size(120.dp)
            .align(Alignment.CenterHorizontally),
        )
        Text(
          text = description,
          color = MaterialTheme.colorScheme.onPrimary,
          style = MaterialTheme.typography.bodyLarge,
          modifier = Modifier.align(Alignment.CenterHorizontally),
        )
      }
      Text(
        text = date,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.align(Alignment.BottomStart),
      )
    }
  }
}
