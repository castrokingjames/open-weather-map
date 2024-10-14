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
@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.castrokingjames.feature.dashboard

import app.cash.turbine.test
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.castrokingjames.ui.UiState
import io.github.castrokingjames.usecase.LoadWeatherByLocationUseCase
import io.github.castrokingjames.usecase.LoadWeatherHistoryUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before

class DashboardComponentTest {

  private val lifecycle = LifecycleRegistry()
  private val defaultComponentContext = DefaultComponentContext(lifecycle)
  private val dispatcher = UnconfinedTestDispatcher()

  @Before
  fun setup() {
    Dispatchers.setMain(dispatcher)
    lifecycle.resume()
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun testUiStateIsLoadingWhenInitialized() {
    runTest {
      val component = createComponent()
      val weatherUiState = component.weatherUiState.value
      val historyUiState = component.historyUiState.value
      assertTrue(weatherUiState is UiState.Loading)
      assertTrue(historyUiState is UiState.Loading)
    }
  }

  @Test
  fun testWeatherUiStateIsErrorWhenLoading() {
    runTest {
      val expectedException = Exception("Error")
      val component = createComponent(
        loadWeatherByLocation = mockk {
          coEvery { this@mockk.invoke() } returns flow { throw expectedException }
        },
      )
      component
        .weatherUiState
        .test {
          val weatherUiState = awaitItem()
          assertTrue(weatherUiState is UiState.Error)
          assertEquals(expectedException.message, (weatherUiState as UiState.Error).exception.message)
        }
    }
  }

  @Test
  fun testHistoryUiStateIsErrorWhenLoading() {
    runTest {
      val expectedException = Exception("Error")
      val component = createComponent(
        loadWeatherHistory = mockk {
          coEvery { this@mockk.invoke() } returns flow { throw expectedException }
        },
      )
      component
        .historyUiState
        .test {
          val historyUiState = awaitItem()
          assertTrue(historyUiState is UiState.Error)
          assertEquals(expectedException.message, (historyUiState as UiState.Error).exception.message)
        }
    }
  }

  @Test
  fun testSelectTabEmitSelectedTab() {
    runTest {
      val component = createComponent()
      val expectedIndex = 3
      val actualIndex = component.tab
      component.select(expectedIndex)
      assertEquals(expectedIndex, actualIndex.value)
    }
  }

  private fun createComponent(
    loadWeatherByLocation: LoadWeatherByLocationUseCase = mockk {
      coEvery { this@mockk.invoke() } returns flowOf(generateWeather())
    },
    loadWeatherHistory: LoadWeatherHistoryUseCase = mockk {
      coEvery { this@mockk.invoke() } returns flowOf(listOf(generateWeather()))
    },
    componentContext: ComponentContext = defaultComponentContext,
    coroutineContext: CoroutineContext = dispatcher,
  ): DashboardComponent {
    return DashboardViewModel(
      loadWeatherByLocation,
      loadWeatherHistory,
      componentContext,
      coroutineContext,
    )
  }
}
