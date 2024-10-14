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
package io.github.castrokingjames.datasource.remote.response

import io.github.castrokingjames.CITY_NAME
import io.github.castrokingjames.CLOUDS
import io.github.castrokingjames.CLOUDS_ALL
import io.github.castrokingjames.DT
import io.github.castrokingjames.INFO
import io.github.castrokingjames.INFO_HUMIDITY
import io.github.castrokingjames.INFO_PRESSURE
import io.github.castrokingjames.INFO_TEMPERATURE
import io.github.castrokingjames.INFO_TEMPERATURE_FEELS
import io.github.castrokingjames.INFO_TEMPERATURE_MAX
import io.github.castrokingjames.INFO_TEMPERATURE_MIN
import io.github.castrokingjames.SYS
import io.github.castrokingjames.SYS_COUNTRY
import io.github.castrokingjames.SYS_SUNRISE
import io.github.castrokingjames.SYS_SUNSET
import io.github.castrokingjames.WEATHER
import io.github.castrokingjames.WEATHER_DESCRIPTION
import io.github.castrokingjames.WEATHER_ICON
import io.github.castrokingjames.WIND
import io.github.castrokingjames.WIND_SPEED
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
  @SerialName(WEATHER)
  val weather: List<Weather>,
  @SerialName(INFO)
  val info: Info,
  @SerialName(WIND)
  val wind: Wind,
  @SerialName(CLOUDS)
  val clouds: Clouds,
  @SerialName(SYS)
  val sys: Sys,
  @SerialName(DT)
  val dt: Long,
  @SerialName(CITY_NAME)
  val cityName: String,
) {

  @Serializable
  data class Weather(
    @SerialName(WEATHER_DESCRIPTION)
    val description: String,
    @SerialName(WEATHER_ICON)
    val icon: String,
  )

  @Serializable
  data class Info(
    @SerialName(INFO_TEMPERATURE)
    val temperature: Double,
    @SerialName(INFO_TEMPERATURE_MIN)
    val temperatureMin: Double,
    @SerialName(INFO_TEMPERATURE_MAX)
    val temperatureMax: Double,
    @SerialName(INFO_TEMPERATURE_FEELS)
    val temperatureFeels: Double,
    @SerialName(INFO_PRESSURE)
    val pressure: Double,
    @SerialName(INFO_HUMIDITY)
    val humidity: Double,
  )

  @Serializable
  data class Wind(
    @SerialName(WIND_SPEED)
    val speed: Double,
  )

  @Serializable
  data class Clouds(
    @SerialName(CLOUDS_ALL)
    val all: Double,
  )

  @Serializable
  data class Sys(
    @SerialName(SYS_COUNTRY)
    val country: String,
    @SerialName(SYS_SUNRISE)
    val sunrise: Long,
    @SerialName(SYS_SUNSET)
    val sunset: Long,
  )
}
