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

package io.github.castrokingjames.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.turbine.test
import io.github.castrokingjames.data.mapper.weathers
import io.github.castrokingjames.datasource.local.database.WeatherArea
import io.github.castrokingjames.datasource.local.database.WeatherAreaQueries
import io.github.castrokingjames.datasource.local.database.WeatherIcon
import io.github.castrokingjames.datasource.local.database.WeatherIconQueries
import io.github.castrokingjames.datasource.local.database.WeatherTemperature
import io.github.castrokingjames.datasource.local.database.WeatherTemperatureQueries
import io.github.castrokingjames.datasource.local.database.WeatherTwilight
import io.github.castrokingjames.datasource.local.database.WeatherTwilightQueries
import io.github.castrokingjames.datasource.local.database.WeathersQueries
import io.github.castrokingjames.datasource.remote.response.Response
import io.github.castrokingjames.datasource.remote.service.WeatherService
import io.github.castrokingjames.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class WeatherDataRepositoryTest {

  private val dispatcher = UnconfinedTestDispatcher()

  private lateinit var weatherService: WeatherService
  private lateinit var weathersQueries: WeathersQueries
  private lateinit var weatherAreaQueries: WeatherAreaQueries
  private lateinit var weatherIconQueries: WeatherIconQueries
  private lateinit var weatherTwilightQueries: WeatherTwilightQueries
  private lateinit var weatherTemperatureQueries: WeatherTemperatureQueries
  private lateinit var weatherRepository: WeatherRepository

  @Before
  fun setup() {
    Dispatchers.setMain(dispatcher)
    mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    weatherService = mockk()
    weathersQueries = mockk()
    weatherAreaQueries = mockk()
    weatherIconQueries = mockk()
    weatherTwilightQueries = mockk()
    weatherTemperatureQueries = mockk()
    weatherRepository = WeatherDataRepository(
      weatherService,
      weathersQueries,
      weatherAreaQueries,
      weatherIconQueries,
      weatherTwilightQueries,
      weatherTemperatureQueries,
      dispatcher,
    )
  }

  @Test
  fun testLoadWeatherByLocationReturnsLocation() {
    runTest {
      val latitude = 0.0
      val longitude = 0.0
      val weatherResponse = generateWeatherResponse()
      val weather = weatherResponse.weather.first()
      val weathers = weatherResponse.weathers()
      val icon = WeatherIcon(weathers.id, weather.icon, weather.description)
      val area = WeatherArea(weathers.id, weatherResponse.cityName, weatherResponse.sys.country)
      val temperature = WeatherTemperature(weathers.id, weatherResponse.info.temperatureMin, weatherResponse.info.temperatureMax, weatherResponse.info.temperature, weatherResponse.info.temperatureFeels)
      val twilight = WeatherTwilight(weathers.id, weatherResponse.sys.sunrise, weatherResponse.sys.sunset)

      coEvery { weatherService.get(latitude, longitude) } returns Response.Success(
        weatherResponse,
      )
      coEvery { weathersQueries.upsert(any()) } returns Unit
      coEvery { weatherAreaQueries.upsert(any()) } returns Unit
      coEvery { weatherIconQueries.upsert(any()) } returns Unit
      coEvery { weatherTwilightQueries.upsert(any()) } returns Unit
      coEvery { weatherTemperatureQueries.upsert(any()) } returns Unit
      coEvery { weathersQueries.selectLatest() } returns mockk {
        coEvery { asFlow().mapToOneOrNull(dispatcher) } returns flowOf(weathers)
      }

      coEvery { weathersQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(weathers)
      }
      coEvery { weatherIconQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(icon)
      }
      coEvery { weatherAreaQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(area)
      }
      coEvery { weatherTemperatureQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(temperature)
      }
      coEvery { weatherTwilightQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(twilight)
      }

      weatherRepository
        .load(latitude, longitude)
        .test {
          val item = awaitItem()
          assertNotNull(item)
          awaitComplete()
        }
    }
  }

  @Test
  fun testLoadWeatherByLocationEmitNull() {
    runTest {
      val latitude = 0.0
      val longitude = 0.0
      val weatherResponse = generateWeatherResponse()
      val weather = weatherResponse.weather.first()
      val weathers = weatherResponse.weathers()
      val icon = WeatherIcon(weathers.id, weather.icon, weather.description)
      val area = WeatherArea(weathers.id, weatherResponse.cityName, weatherResponse.sys.country)
      val temperature = WeatherTemperature(weathers.id, weatherResponse.info.temperatureMin, weatherResponse.info.temperatureMax, weatherResponse.info.temperature, weatherResponse.info.temperatureFeels)
      val twilight = WeatherTwilight(weathers.id, weatherResponse.sys.sunrise, weatherResponse.sys.sunset)

      coEvery { weatherService.get(latitude, longitude) } returns Response.Success(
        weatherResponse,
      )
      coEvery { weathersQueries.upsert(any()) } returns Unit
      coEvery { weatherAreaQueries.upsert(any()) } returns Unit
      coEvery { weatherIconQueries.upsert(any()) } returns Unit
      coEvery { weatherTwilightQueries.upsert(any()) } returns Unit
      coEvery { weatherTemperatureQueries.upsert(any()) } returns Unit
      coEvery { weathersQueries.selectLatest() } returns mockk {
        coEvery { asFlow().mapToOneOrNull(dispatcher) } returns flowOf(null)
      }
      coEvery { weathersQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(weathers)
      }
      coEvery { weatherIconQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(icon)
      }
      coEvery { weatherAreaQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(area)
      }
      coEvery { weatherTemperatureQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(temperature)
      }
      coEvery { weatherTwilightQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(twilight)
      }

      weatherRepository
        .load(latitude, longitude)
        .test {
          awaitComplete()
        }
    }
  }

  @Test
  fun testLoadHistoryReturnsHistory() {
    runTest {
      val weatherResponse = generateWeatherResponse()
      val weathers = weatherResponse.weathers()
      val weather = weatherResponse.weather.first()
      val icon = WeatherIcon(weathers.id, weather.icon, weather.description)
      val area = WeatherArea(weathers.id, weatherResponse.cityName, weatherResponse.sys.country)
      val temperature = WeatherTemperature(weathers.id, weatherResponse.info.temperatureMin, weatherResponse.info.temperatureMax, weatherResponse.info.temperature, weatherResponse.info.temperatureFeels)
      val twilight = WeatherTwilight(weathers.id, weatherResponse.sys.sunrise, weatherResponse.sys.sunset)
      coEvery { weathersQueries.selectAll() } returns mockk {
        coEvery { asFlow().mapToList(dispatcher) } returns flowOf(listOf(weathers))
      }
      coEvery { weathersQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(weathers)
      }
      coEvery { weatherIconQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(icon)
      }
      coEvery { weatherAreaQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(area)
      }
      coEvery { weatherTemperatureQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(temperature)
      }
      coEvery { weatherTwilightQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(twilight)
      }
      weatherRepository
        .loadHistory()
        .test {
          val item = awaitItem()
          assertNotNull(item)
          awaitComplete()
        }
    }
  }

  @Test
  fun testLoadHistoryReturnsEmpty() {
    runTest {
      val weatherResponse = generateWeatherResponse()
      val weathers = weatherResponse.weathers()
      val weather = weatherResponse.weather.first()
      val icon = WeatherIcon(weathers.id, weather.icon, weather.description)
      val area = WeatherArea(weathers.id, weatherResponse.cityName, weatherResponse.sys.country)
      val temperature = WeatherTemperature(weathers.id, weatherResponse.info.temperatureMin, weatherResponse.info.temperatureMax, weatherResponse.info.temperature, weatherResponse.info.temperatureFeels)
      val twilight = WeatherTwilight(weathers.id, weatherResponse.sys.sunrise, weatherResponse.sys.sunset)
      coEvery { weathersQueries.selectAll() } returns mockk {
        coEvery { asFlow().mapToList(dispatcher) } returns flowOf(emptyList())
      }
      coEvery { weathersQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(weathers)
      }
      coEvery { weatherIconQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(icon)
      }
      coEvery { weatherAreaQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(area)
      }
      coEvery { weatherTemperatureQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(temperature)
      }
      coEvery { weatherTwilightQueries.selectByWeatherId(any()) } returns mockk {
        coEvery { asFlow().mapToOne(dispatcher) } returns flowOf(twilight)
      }
      weatherRepository
        .loadHistory()
        .test {
          val item = awaitItem()
          assertNotNull(item)
          assertTrue(item.isEmpty())
          awaitComplete()
        }
    }
  }
}
