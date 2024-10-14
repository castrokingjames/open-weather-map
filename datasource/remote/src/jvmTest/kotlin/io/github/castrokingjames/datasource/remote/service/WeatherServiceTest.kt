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
package io.github.castrokingjames.datasource.remote.service

import io.github.castrokingjames.datasource.remote.RESPONSE_WEATHER_200
import io.github.castrokingjames.datasource.remote.RESPONSE_WEATHER_401
import io.github.castrokingjames.datasource.remote.isError
import io.github.castrokingjames.datasource.remote.isSuccess
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherServiceTest {

  @Test
  fun testGetWeatherReturnsSuccess() {
    runTest {
      val expectedContent = RESPONSE_WEATHER_200
      val service = createService(expectedContent, HttpStatusCode.OK)
      val result = service.get(0.0, 0.0)
      assertTrue(result.isSuccess)
    }
  }

  @Test
  fun testGetWeatherReturns401() {
    runTest {
      val expectedContent = RESPONSE_WEATHER_401
      val service = createService(expectedContent, HttpStatusCode.Unauthorized)
      val result = service.get(0.0, 0.0)
      assertTrue(result.isError)
    }
  }

  private fun createService(content: String, status: HttpStatusCode): WeatherService {
    val engine = MockEngine { _ ->
      respond(
        content = content,
        status = status,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
      )
    }
    val client = HttpClient(engine) {
      install(ContentNegotiation) {
        json(
          Json {
            ignoreUnknownKeys = true
            isLenient = true
          },
        )
      }
    }
    return WeatherService(client)
  }
}
