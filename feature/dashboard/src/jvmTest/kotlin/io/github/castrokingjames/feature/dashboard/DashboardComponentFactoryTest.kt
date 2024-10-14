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

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.castrokingjames.usecase.LoadWeatherByLocationUseCase
import io.github.castrokingjames.usecase.LoadWeatherHistoryUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

class DashboardComponentFactoryTest {

  private val lifecycle = LifecycleRegistry()
  private val defaultComponentContext = DefaultComponentContext(lifecycle)
  private val dispatcher = UnconfinedTestDispatcher()
  private lateinit var loadWeatherByLocation: LoadWeatherByLocationUseCase
  private lateinit var loadWeatherHistory: LoadWeatherHistoryUseCase

  private lateinit var factory: DashboardComponent.Factory

  @Before
  fun setup() {
    Dispatchers.setMain(dispatcher)
    lifecycle.resume()
    loadWeatherByLocation = mockk {
      coEvery { this@mockk.invoke() } returns flow {}
    }
    loadWeatherHistory = mockk {
      coEvery { this@mockk.invoke() } returns flow {}
    }
    factory = DashboardComponentFactory(loadWeatherByLocation, loadWeatherHistory, dispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun testCreateComponent() {
    val component = factory(defaultComponentContext)
    assertNotNull(component)
  }
}
