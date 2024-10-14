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
package io.github.castrokingjames.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.castrokingjames.feature.dashboard.history.HistoryView
import io.github.castrokingjames.feature.dashboard.metrics.MetricsView
import io.github.castrokingjames.feature.dashboard.widget.WigetView

@Composable
fun DashboardScreen(
  modifier: Modifier = Modifier,
  component: DashboardComponent,
) {
  val weatherUiState = component
    .weatherUiState
    .collectAsState()
    .value

  val historyUiState = component
    .historyUiState
    .collectAsState()
    .value

  val tab = component.tab

  Scaffold { padding ->
    Column(
      modifier = modifier
        .padding(padding)
        .background(MaterialTheme.colorScheme.background),
    ) {
      WigetView(
        modifier = Modifier
          .weight(0.4f)
          .fillMaxWidth(),
        weatherUiState,
      )
      Column(
        modifier = Modifier.weight(0.6f),
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
        ) {
          Card(
            modifier = modifier.weight(0.5f),
            colors = CardDefaults.cardColors(
              if (tab.value == 0) MaterialTheme.colorScheme.onSecondary else Color.White,
            ),
            onClick = {
              component.select(0)
            },
          ) {
            Box(
              modifier = Modifier.fillMaxWidth(),
            ) {
              Text(
                text = "Today",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                  .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp,
                  )
                  .align(Alignment.Center),
              )
            }
          }
          Spacer(modifier = Modifier.size(16.dp))
          Card(
            modifier = modifier.weight(0.5f),
            colors = CardDefaults.cardColors(
              if (tab.value == 1) MaterialTheme.colorScheme.onSecondary else Color.White,
            ),
            onClick = {
              component.select(1)
            },
          ) {
            Box(
              modifier = Modifier.fillMaxWidth(),
            ) {
              Text(
                "History",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                  .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp,
                  )
                  .align(Alignment.Center),
              )
            }
          }
        }

        Box {
          when (tab.value) {
            0 -> MetricsView(uiState = weatherUiState)
            1 -> HistoryView(historyUiState = historyUiState)
          }
        }
      }
    }
  }
}
