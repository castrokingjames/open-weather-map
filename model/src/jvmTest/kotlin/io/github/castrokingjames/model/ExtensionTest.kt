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
package io.github.castrokingjames.model

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.unmockkStatic
import java.util.Calendar
import kotlin.test.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

class ExtensionTest {

  @Before
  fun setup() {
    mockkStatic(Calendar::class)
  }

  @After
  fun teardown() {
    unmockkAll()
  }

  @Test
  fun testWeatherIconOn6amReturnsSunIcon() {
    every { Calendar.getInstance() } returns mockk {
      every { this@mockk.get(Calendar.HOUR_OF_DAY) } returns 6
    }
    val weather = generateWeather()
    val icon = weather.getIcon()
    assertEquals("https://openweathermap.org/img/wn/04d@4x.png", icon)
  }

  @Test
  fun testWeatherIconOn6pmReturnsMoonIcon() {
    every { Calendar.getInstance() } returns mockk {
      every { this@mockk.get(Calendar.HOUR_OF_DAY) } returns 18
    }
    val weather = generateWeather()
    val icon = weather.getIcon()
    assertEquals("https://openweathermap.org/img/wn/04n@4x.png", icon)
  }

  @Test
  fun testDateFormat() {
    unmockkStatic(Calendar::class)
    val now = 1728862580775
    val weather = generateWeather(now)
    val actual = weather.getDate()
    var expected = "October 14, 07:36"
    assertEquals(expected, actual, "$expected == $actual")
  }
}
