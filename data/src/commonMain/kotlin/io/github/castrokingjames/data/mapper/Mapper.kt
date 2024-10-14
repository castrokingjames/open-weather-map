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
package io.github.castrokingjames.data.mapper

import io.github.castrokingjames.datasource.local.database.WeatherArea
import io.github.castrokingjames.datasource.local.database.WeatherTemperature
import io.github.castrokingjames.datasource.local.database.WeatherTwilight
import io.github.castrokingjames.datasource.local.database.Weathers
import io.github.castrokingjames.datasource.remote.response.WeatherResponse
import io.github.castrokingjames.model.Area
import io.github.castrokingjames.model.Temperature
import io.github.castrokingjames.model.Twilight

fun WeatherResponse.weathers(): Weathers {
  val id = System.currentTimeMillis()
  val pressure = info.pressure
  val humidity = info.humidity
  val cloud = clouds.all
  val windSpeed = wind.speed
  return Weathers(
    id,
    pressure,
    humidity,
    cloud,
    windSpeed,
  )
}

fun WeatherArea.model(): Area {
  return Area(
    city,
    country,
  )
}

fun WeatherTemperature.model(): Temperature {
  return Temperature(
    min,
    max,
    real,
    feels,
  )
}

fun WeatherTwilight.model(): Twilight {
  return Twilight(
    sunrise,
    sunset,
  )
}
