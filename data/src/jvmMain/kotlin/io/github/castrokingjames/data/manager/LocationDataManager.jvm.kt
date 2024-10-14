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
package io.github.castrokingjames.data.manager

import io.github.castrokingjames.datasource.remote.onSuccess
import io.github.castrokingjames.datasource.remote.service.LocationService
import io.github.castrokingjames.manager.LocationManager
import io.github.castrokingjames.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

actual class LocationDataManager constructor(
  private val locationService: LocationService,
) : LocationManager {

  override suspend fun load(): Flow<Location> {
    return channelFlow {
      locationService
        .get()
        .onSuccess { response ->
          val location = Location(response.latitude, response.longitude)
          send(location)
        }
    }
  }
}
