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

import io.github.castrokingjames.GEO_CITY
import io.github.castrokingjames.GEO_COUNTRY
import io.github.castrokingjames.GEO_LATITUDE
import io.github.castrokingjames.GEO_LONGITUDE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
  @SerialName(GEO_CITY)
  val city: String,
  @SerialName(GEO_COUNTRY)
  val country: String,
  @SerialName(GEO_LATITUDE)
  val latitude: Double,
  @SerialName(GEO_LONGITUDE)
  val longitude: Double,
)
