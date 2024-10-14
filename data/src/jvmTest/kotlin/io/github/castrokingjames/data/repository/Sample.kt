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

import io.github.castrokingjames.datasource.remote.response.WeatherResponse

fun generateWeatherResponse(): WeatherResponse {
  val weather = generateWeather()
  val info = generateInfo()
  val clouds = generateClouds()
  val wind = generateWind()
  val sys = generateSys()
  val dt = 123456L
  val cityName = "Manila"
  return WeatherResponse(
    weather = listOf(weather),
    info = info,
    wind = wind,
    clouds = clouds,
    sys = sys,
    dt = dt,
    cityName = cityName,
  )
}

fun generateWeather(): WeatherResponse.Weather {
  return WeatherResponse.Weather(
    "Description",
    "10d",
  )
}

fun generateInfo(): WeatherResponse.Info {
  return WeatherResponse.Info(
    12.0,
    12.0,
    12.0,
    12.0,
    12.0,
    12.0,
  )
}

fun generateWind(): WeatherResponse.Wind {
  return WeatherResponse.Wind(
    12.0,
  )
}

fun generateClouds(): WeatherResponse.Clouds {
  return WeatherResponse.Clouds(
    10.0,
  )
}

fun generateSys(): WeatherResponse.Sys {
  return WeatherResponse.Sys(
    "PH",
    123456L,
    123456L,
  )
}
