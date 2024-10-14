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

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import io.github.castrokingjames.model.Weather
import io.github.castrokingjames.ui.UiState
import io.github.castrokingjames.usecase.LoadWeatherByLocationUseCase
import io.github.castrokingjames.usecase.LoadWeatherHistoryUseCase
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel constructor(
  private val loadWeatherByLocation: LoadWeatherByLocationUseCase,
  private val loadWeatherHistory: LoadWeatherHistoryUseCase,
  private val componentContext: ComponentContext,
  private val coroutineContext: CoroutineContext,
) : DashboardComponent, ComponentContext by componentContext {

  private val scope = coroutineScope(coroutineContext + SupervisorJob())

  private var _tab: MutableState<Int> = mutableStateOf(0)
  override val tab: State<Int> = _tab

  override fun select(index: Int) {
    _tab.value = index
  }

  override val weatherUiState: StateFlow<UiState<Weather>> = loadWeather()
    .catch { e ->
      emit(UiState.Error(e))
    }
    .stateIn(
      scope,
      started = SharingStarted.WhileSubscribed(5000L),
      initialValue = UiState.Loading,
    )

  override val historyUiState: StateFlow<UiState<List<Weather>>> = loadHistory()
    .catch { e ->
      emit(UiState.Error(e))
    }
    .stateIn(
      scope,
      started = SharingStarted.WhileSubscribed(5000L),
      initialValue = UiState.Loading,
    )

  private fun loadWeather(): Flow<UiState<Weather>> {
    return loadWeatherByLocation()
      .map { result -> UiState.Success(result) }
  }

  private fun loadHistory(): Flow<UiState<List<Weather>>> {
    return loadWeatherHistory()
      .map { result -> UiState.Success(result) }
  }
}
