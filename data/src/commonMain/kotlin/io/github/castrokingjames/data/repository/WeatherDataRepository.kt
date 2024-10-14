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
package io.github.castrokingjames.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import io.github.castrokingjames.data.mapper.model
import io.github.castrokingjames.data.mapper.weathers
import io.github.castrokingjames.datasource.local.database.WeatherArea
import io.github.castrokingjames.datasource.local.database.WeatherAreaQueries
import io.github.castrokingjames.datasource.local.database.WeatherIcon
import io.github.castrokingjames.datasource.local.database.WeatherIconQueries
import io.github.castrokingjames.datasource.local.database.WeatherTemperature
import io.github.castrokingjames.datasource.local.database.WeatherTemperatureQueries
import io.github.castrokingjames.datasource.local.database.WeatherTwilight
import io.github.castrokingjames.datasource.local.database.WeatherTwilightQueries
import io.github.castrokingjames.datasource.local.database.Weathers
import io.github.castrokingjames.datasource.local.database.WeathersQueries
import io.github.castrokingjames.datasource.remote.onSuccess
import io.github.castrokingjames.datasource.remote.service.WeatherService
import io.github.castrokingjames.model.Weather
import io.github.castrokingjames.repository.WeatherRepository
import java.util.Locale
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class WeatherDataRepository constructor(
  private val weatherService: WeatherService,
  private val weathersQueries: WeathersQueries,
  private val weatherAreaQueries: WeatherAreaQueries,
  private val weatherIconQueries: WeatherIconQueries,
  private val weatherTwilightQueries: WeatherTwilightQueries,
  private val weatherTemperatureQueries: WeatherTemperatureQueries,
  private val io: CoroutineDispatcher,
) : WeatherRepository {

  override suspend fun load(latitude: Double, longitude: Double): Flow<Weather> {
    return channelFlow {
      launch {
        weathersQueries
          .selectLatest()
          .asFlow()
          .mapToOneOrNull(io)
          .flatMapLatest { entity ->
            if (entity == null) {
              flowOf(null)
            } else {
              loadByWeatherId(entity.id)
            }
          }
          .collectLatest { weather ->
            if (weather != null) {
              send(weather)
            }
          }
      }

      weatherService
        .get(latitude, longitude)
        .onSuccess { response ->
          val weathers = response.weathers()
          weathersQueries.upsert(weathers)

          val area = WeatherArea(
            weathers.id,
            response.cityName,
            Locale("", response.sys.country).displayCountry,
          )
          weatherAreaQueries.upsert(area)

          val source = response.weather.first()
          val icon = WeatherIcon(
            weathers.id,
            source.icon,
            source.description,
          )
          weatherIconQueries.upsert(icon)

          val twilight = WeatherTwilight(
            weathers.id,
            response.sys.sunrise,
            response.sys.sunset,
          )
          weatherTwilightQueries.upsert(twilight)

          val temperature = WeatherTemperature(
            weathers.id,
            response.info.temperatureMin,
            response.info.temperatureMax,
            response.info.temperature,
            response.info.temperatureFeels,
          )
          weatherTemperatureQueries.upsert(temperature)
        }
    }
  }

  override suspend fun loadByWeatherId(id: Long): Flow<Weather> {
    return channelFlow {
      weathersQueries
        .selectByWeatherId(id)
        .asFlow()
        .mapToOne(io)
        .flatMapLatest { entity ->
          combine(
            selectWeatherByWeatherId(entity.id),
            selectIconByWeatherId(entity.id),
            selectAreaByWeatherId(entity.id),
            selectTemperatureByWeatherId(entity.id),
            selectTwilightByWeatherId(entity.id),
          ) { weather, icon, area, temperature, twilight ->
            Weather(
              id = weather.id,
              pressure = weather.pressure,
              humidity = weather.humidity,
              cloud = weather.cloud,
              windSpeed = weather.windSpeed,
              description = icon.description,
              icon = icon.icon,
              area = area.model(),
              temperature = temperature.model(),
              twilight = twilight.model(),
            )
          }
        }
        .collectLatest { weather ->
          send(weather)
        }
    }
  }

  override suspend fun loadHistory(): Flow<List<Weather>> {
    return channelFlow {
      weathersQueries
        .selectAll()
        .asFlow()
        .mapToList(io)
        .flatMapLatest { entities ->
          if (entities.isEmpty()) {
            flowOf(emptyList())
          } else {
            entities
              .map { weather ->
                loadByWeatherId(weather.id)
              }
              .let { weathers ->
                combine(weathers) { weathers ->
                  weathers.toList()
                }
              }
          }
        }
        .collectLatest { history ->
          send(history)
        }
    }
  }

  private suspend fun selectTemperatureByWeatherId(weatherId: Long): Flow<WeatherTemperature> {
    return channelFlow {
      weatherTemperatureQueries
        .selectByWeatherId(weatherId)
        .asFlow()
        .mapToOne(io)
        .collectLatest { temperature ->
          send(temperature)
        }
    }
  }

  private suspend fun selectWeatherByWeatherId(weatherId: Long): Flow<Weathers> {
    return channelFlow {
      weathersQueries
        .selectByWeatherId(weatherId)
        .asFlow()
        .mapToOne(io)
        .collectLatest { weather ->
          send(weather)
        }
    }
  }

  private suspend fun selectIconByWeatherId(weatherId: Long): Flow<WeatherIcon> {
    return channelFlow {
      weatherIconQueries
        .selectByWeatherId(weatherId)
        .asFlow()
        .mapToOne(io)
        .collectLatest { icon ->
          send(icon)
        }
    }
  }

  private suspend fun selectAreaByWeatherId(weatherId: Long): Flow<WeatherArea> {
    return channelFlow {
      weatherAreaQueries
        .selectByWeatherId(weatherId)
        .asFlow()
        .mapToOne(io)
        .collectLatest { area ->
          send(area)
        }
    }
  }

  private suspend fun selectTwilightByWeatherId(weatherId: Long): Flow<WeatherTwilight> {
    return channelFlow {
      weatherTwilightQueries
        .selectByWeatherId(weatherId)
        .asFlow()
        .mapToOne(io)
        .collectLatest { twilight ->
          send(twilight)
        }
    }
  }
}
