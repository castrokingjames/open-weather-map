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

import io.github.castrokingjames.model.Area
import io.github.castrokingjames.model.Location
import io.github.castrokingjames.model.Temperature
import io.github.castrokingjames.model.Twilight
import io.github.castrokingjames.model.Weather

fun generateWeather(id: Long = System.currentTimeMillis()): Weather {
  val pressure = 1021.0
  val humidity = 60.0
  val cloud = 69.0
  val windSpeed = 75.0
  val description = "Broken clouds"
  val icon = "04d"
  val area = generateArea()
  val temperature = generateTemperature()
  val twilight = generateTwilight()
  return Weather(
    id,
    pressure,
    humidity,
    cloud,
    windSpeed,
    description,
    icon,
    area,
    temperature,
    twilight,
  )
}

fun generateArea(): Area {
  return Area(
    city = "Manila",
    country = "Philippines",
  )
}

fun generateTemperature(): Temperature {
  return Temperature(
    min = 304.15,
    max = 305.99,
    real = 305.14,
    feels = 312.14,
  )
}

fun generateTwilight(): Twilight {
  return Twilight(
    sunset = 1728812245,
    sunrise = 1728812245,
  )
}

fun generateLocation(): Location {
  return Location(0.0, 0.0)
}
