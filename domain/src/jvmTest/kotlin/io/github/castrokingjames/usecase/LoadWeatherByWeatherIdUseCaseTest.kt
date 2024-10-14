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
package io.github.castrokingjames.usecase

import app.cash.turbine.test
import io.github.castrokingjames.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LoadWeatherByWeatherIdUseCaseTest {

  private lateinit var weatherRepository: WeatherRepository
  private lateinit var loadWeatherByWeatherId: LoadWeatherByWeatherIdUseCase

  @Before
  fun setup() {
    weatherRepository = mockk()
    loadWeatherByWeatherId = LoadWeatherByWeatherIdUseCase(weatherRepository)
  }

  @Test
  fun testLoadByWeatherIdReturnsExpectedWeather() {
    runTest {
      val expectedWeatherId = 99L
      val expectedWeather = generateWeather(expectedWeatherId)
      coEvery { weatherRepository.loadByWeatherId(expectedWeatherId) } returns flowOf(expectedWeather)
      loadWeatherByWeatherId(expectedWeatherId)
        .test {
          val actualWeather = awaitItem()
          assertEquals(expectedWeather, actualWeather)
          awaitComplete()
        }
    }
  }
}
