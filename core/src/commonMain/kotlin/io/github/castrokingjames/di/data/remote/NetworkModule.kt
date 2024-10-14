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
package io.github.castrokingjames.di.data.remote

import io.github.castrokingjames.BASE_URL
import io.github.castrokingjames.DEBUG
import io.github.castrokingjames.OPEN_WEATHER_MAP_API_KEY
import io.github.castrokingjames.X_OPEN_WEATHER_MAP_API_KEY
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {

  includes(clientModule)
  includes(serviceModule)

  single {
    val debug: Boolean = get(named(DEBUG))
    val baseUrl: String = get(named(BASE_URL))
    val apiKey: String = get(named(OPEN_WEATHER_MAP_API_KEY))
    val factory: HttpClientEngineFactory<*> = get()
    HttpClient(factory) {
      if (debug) {
        install(Logging) {
          logger = Logger.DEFAULT
          level = LogLevel.ALL
        }
      }
      install(ContentNegotiation) {
        json(
          Json {
            ignoreUnknownKeys = true
            isLenient = true
          },
        )
      }
      install(
        createClientPlugin(OPEN_WEATHER_MAP_API_KEY) {
          onRequest { request, _ ->
            request.parameter(X_OPEN_WEATHER_MAP_API_KEY, apiKey)
          }
        },
      )
      defaultRequest {
        url(baseUrl)
        contentType(ContentType.Application.Json)
      }
    }
  }
}
