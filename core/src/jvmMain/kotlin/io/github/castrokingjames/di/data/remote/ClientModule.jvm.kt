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
@file:OptIn(ExperimentalResourceApi::class)

package io.github.castrokingjames.di.data.remote

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.dsl.bind
import org.koin.dsl.module

actual val clientModule = module {

  single {
    Http
  } bind HttpClientEngineFactory::class
}

public object Http : HttpClientEngineFactory<OkHttpConfig> {
  override fun create(block: OkHttpConfig.() -> Unit): HttpClientEngine {
    val certificatePinner = CertificatePinner.Builder()
      .add("api.openweathermap.org", "sha256/CpmBztr3L/AZjANtR+K3vhridQoIsoyqTl5yU5zQQLQ=")
      .add("api.openweathermap.org", "sha256/4a6cPehI7OG6cuDZka5NDZ7FR8a60d3auda+sKfg4Ng=")
      .add("api.openweathermap.org", "sha256/x4QzPSC810K5/cMjb05Qm4k3Bw5zBn4lTdO/nEW/Td4=")
      .add("api.openweathermap.org", "sha256/w4E8rztmGwgrx9k4LFpL8yWYcSSHGUW7uKN+bEqzGvM=")
      .build()
    val okHttpClient = OkHttpClient.Builder()
      .certificatePinner(certificatePinner)
      .build()
    val config = OkHttpConfig()
    block(config)
    config.preconfigured = okHttpClient
    return OkHttpEngine(config)
  }
}
